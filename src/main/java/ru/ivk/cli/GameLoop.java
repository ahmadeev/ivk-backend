package ru.ivk.cli;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.ivk.cli.io.commands.game.dto.DTO;
import ru.ivk.cli.io.commands.game.dto.GameDTO;
import ru.ivk.cli.io.commands.game.dto.MoveDTO;
import ru.ivk.common.game.GameBoard;
import ru.ivk.common.game.GameEngine;
import ru.ivk.common.game.GameManager;
import ru.ivk.common.game.model.GameState;
import ru.ivk.common.game.model.Player;
import ru.ivk.common.game.model.UserColor;
import ru.ivk.common.game.model.UserType;
import ru.ivk.common.math.Coordinates;

import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class GameLoop {
    private final LinkedBlockingQueue<DTO> queue;

    private final GameEngine engine;

    private GameManager gm = null;

    @Getter
    @Setter
    private GameState state;

    public GameLoop(LinkedBlockingQueue<DTO> queue, GameEngine engine) {
        this.queue = queue;
        this.engine = engine;
        this.state = GameState.IDLE;
    }

    public void run() {
        Runnable r = () -> {
            Coordinates move = null;
            while (true) {
                log.debug("Состояние игры: {}", state);
                if (gm != null) log.debug("Состояние доски: {}", gm.getGameBoard().getBoard());
                if (gm == null && !(this.state.equals(GameState.IDLE))) {
                    // если приходим сюда, то что-то точно сломалось. нет необходимости отлавливать и обрабатывать
                    throw new RuntimeException("Что-то пошло не так... Игра не существует");
                }
                switch (state) {
                    case IDLE:
                        try {
                            DTO dto = queue.take();
                            if (dto instanceof MoveDTO) throw new RuntimeException("Ошибка: попытка сделать ход до создания игры");
                            GameDTO gameDTO = (GameDTO) dto;
                            log.debug("Полученный {}: {}", gameDTO.getClass().getSimpleName(), gameDTO);
                            this.gm = new GameManager(
                                    new GameBoard(gameDTO.getN()),
                                    new Player(
                                            UserType.valueOf(gameDTO.getPlayer1Type()),
                                            UserColor.valueOf(gameDTO.getPlayer1Color())
                                    ),
                                    new Player(
                                            UserType.valueOf(gameDTO.getPlayer2Type()),
                                            UserColor.valueOf(gameDTO.getPlayer2Color())
                                    )
                            );
                            setState(GameState.WAITING_FOR_PLAYER_MOVE);
                            System.out.println("Новая игра была создана");
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                            Thread.currentThread().interrupt();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case WAITING_FOR_PLAYER_MOVE:
                        log.debug("Ход игрока: {}", gm.getCurrentPlayer());
                        Player currentPlayer = gm.getCurrentPlayer();
                        // Coordinates move = null; // в скоупе метода теперь
                        if (currentPlayer.getType().equals(UserType.USER)) {
                            while (getState().equals(GameState.WAITING_FOR_PLAYER_MOVE)) {
                                try {
                                    DTO dto = queue.take(); // TODO: МЫ ТУТ ЗАЦИКЛИЛИСЬ (.poll() будет лучше?)
                                    if (dto instanceof GameDTO) throw new RuntimeException("Невозможно создать игру. Игра уже существует");
                                    MoveDTO moveDTO = (MoveDTO) dto;
                                    log.debug("Полученный {}: {}", moveDTO.getClass().getSimpleName(), moveDTO);
                                    move = new Coordinates(moveDTO.getX(), moveDTO.getY());
                                    setState(GameState.COMPUTING_PLAYERS_MOVE);
                                } catch (InterruptedException e) {
                                    System.out.println(e.getMessage());
                                    Thread.currentThread().interrupt();
                                    break;
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                    break;
                                }
                            }
                        } else if (currentPlayer.getType().equals(UserType.COMP)) {
                            setState(GameState.COMPUTING_PLAYERS_MOVE);
                            move = engine.findBestMove(gm.getGameBoard(), gm.getCurrentPlayer().getColor());
                            // было бы хорошо во время думанья будем отклонять игровые команды и чистить очередь
                        }
                        if (move == null) throw new RuntimeException("Что-то пошло не так... Ход не был выбран");
                        log.debug("Полученный/посчитанный {}: {}", move.getClass().getSimpleName(), move);
                        try {
                            if (!gm.getGameBoard().isFree(move)) throw new RuntimeException("Невозможный ход. Поле уже занято");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            setState(GameState.WAITING_FOR_PLAYER_MOVE);
                            move = null;
                            break;
                        }
                        gm.getGameBoard().move(move, gm.getCurrentPlayer().getColor().getColor());
                        System.out.printf("%s (%d, %d)%n", gm.getCurrentPlayer().getColor().getColor(), move.getX(), move.getY());
                        setState(GameState.CHECKING_CONDITIONS);
                        break;
                    case CHECKING_CONDITIONS:
                        boolean isWin = engine.isWin(gm.getGameBoard(), move, gm.getCurrentPlayer().getColor());
                        log.debug("Победа игрока {}: {}", gm.getCurrentPlayer().getColor().getColor(), isWin);
                        if (isWin) {
                            System.out.printf("Победа игрока %s%n", gm.getCurrentPlayer());
                            setState(GameState.END);
                            break;
                        }
                        if (gm.getGameBoard().getEmptySquaresCount() == 0) {
                            System.out.println("Игра окончена. Ничья");
                            setState(GameState.END);
                            break;
                        }
                        gm.switchCurrentPlayer();
                        move = null;
                        setState(GameState.WAITING_FOR_PLAYER_MOVE);
                        break;
                    case END:
                        gm = null;
                        move = null;
                        setState(GameState.IDLE);
                        break;
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
