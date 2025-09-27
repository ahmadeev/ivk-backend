package ru.ivk.game;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.ivk.game.model.GameState;
import ru.ivk.game.model.Player;
import ru.ivk.game.model.UserColor;
import ru.ivk.game.model.UserType;
import ru.ivk.utils.Utilities;
import ru.ivk.utils.io.commands.game.dto.DTO;
import ru.ivk.utils.io.commands.game.dto.GameDTO;
import ru.ivk.utils.io.commands.game.dto.MoveDTO;
import ru.ivk.utils.math.Coordinates;

import java.util.LinkedList;
import java.util.List;
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
                log.info("Состояние игры: {}", state);
                if (gm != null) log.info("Состояние доски: {}", gm.getGameBoard().getBoard());
                switch (state) {
                    case IDLE:
                        try {
                            DTO dto = queue.take();
                            if (dto instanceof MoveDTO) throw new RuntimeException("Ошибка: попытка сделать ход до создания игры");
                            GameDTO gameDTO = (GameDTO) dto;
                            log.info("Полученный {}: {}", gameDTO.getClass().getSimpleName(), gameDTO);
                            gm = new GameManager(
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
                            state = GameState.WAITING_FOR_PLAYER_MOVE;
                            System.out.println("Новая игра была создана");
                            break;
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                            Thread.currentThread().interrupt();
                            break;
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            break;
                        }
                    case WAITING_FOR_PLAYER_MOVE:
                        if (gm == null) throw new RuntimeException("Невозможно сделать ход. Игра не была создана");
                        log.info("Ход игрока: {}", gm.getCurrentPlayer());
                        Player currentPlayer = gm.getCurrentPlayer();
                        // Coordinates move = null; // в скоупе метода теперь
                        if (currentPlayer.getType().equals(UserType.USER)) {
                            while (getState().equals(GameState.WAITING_FOR_PLAYER_MOVE)) {
                                try {
                                    DTO dto = queue.take(); // TODO: МЫ ТУТ ЗАЦИКЛИЛИСЬ (.poll() будет лучше?)
                                    if (dto instanceof GameDTO) throw new RuntimeException("Невозможно создать игру. Игра уже существует");
                                    MoveDTO moveDTO = (MoveDTO) dto;
                                    log.info("Полученный {}: {}", moveDTO.getClass().getSimpleName(), moveDTO);
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

                            List<Coordinates> threats = new LinkedList<>();
                            UserColor otherPlayerColor = UserColor.getOtherColor(currentPlayer.getColor());
                            for (Coordinates point : gm.getGameBoard().getMovesByColor(otherPlayerColor).keySet()) {
                                List<Coordinates> pm = engine.findFramesOfThree(
                                        gm.getGameBoard(),
                                        point,
                                        otherPlayerColor
                                );
                                threats.addAll(pm);
                            }
                            move = Utilities.findMostFrequent(threats);

                            if (move == null) {
                                List<Coordinates> moves = new LinkedList<>();
                                for (Coordinates point : gm.getGameBoard().getMovesByColor(gm.getCurrentPlayer().getColor()).keySet()) {
                                    List<Coordinates> pm = engine.findFramesOfThree(
                                            gm.getGameBoard(),
                                            point,
                                            currentPlayer.getColor()
                                    );
                                    moves.addAll(pm);
                                }
                                move = Utilities.findMostFrequent(moves);
                                if (move == null) move = gm.getGameBoard().findRandomEmpty();
                            }
/*                            try {
                                // во время думанья будем отклонять игровые команды и чистить очередь
                                log.info("SLEEPING");
                                Thread.sleep(10_000);
                                log.info("NOT SLEEPING");
                            } catch (InterruptedException e) {
                                log.info("INTERRUPTED");
                            }*/
                        }
                        if (move == null) throw new RuntimeException("Что-то пошло не так... Ход не был выбран");
                        log.info("Полученный/посчитанный {}: {}", move.getClass().getSimpleName(), move);
                        try {
                            if (!gm.getGameBoard().isFree(move)) throw new RuntimeException("Невозможный ход. Поле уже занято");
                        } catch (RuntimeException e) {
                            System.out.println(e.getMessage());
                        }
                        gm.getGameBoard().move(move, gm.getCurrentPlayer().getColor().getColor());
                        System.out.printf("%s (%d, %d)%n", gm.getCurrentPlayer().getColor().getColor(), move.getX(), move.getY());
                        setState(GameState.CHECKING_CONDITIONS);
                        break;
                    case CHECKING_CONDITIONS:
                        if (gm == null) throw new RuntimeException("Что-то пошло не так... Игра не была создана");
                        boolean isWin = engine.isWin(gm.getGameBoard(), move, gm.getCurrentPlayer().getColor());
                        log.info("Победа игрока {}: {}", gm.getCurrentPlayer().getColor().getColor(), isWin);
                        if (isWin) {
                            setState(GameState.END);
                        } else {
                            gm.switchCurrentPlayer();
                            setState(GameState.WAITING_FOR_PLAYER_MOVE);
                        }
                        break;
                    case END:
                        if (gm == null) throw new RuntimeException("Что-то пошло не так... Игра не была создана");
                        System.out.printf("Победа игрока %s%n", gm.getCurrentPlayer());
                        gm = null;
                        setState(GameState.IDLE);
                        break;
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
