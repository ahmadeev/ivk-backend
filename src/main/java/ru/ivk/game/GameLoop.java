package ru.ivk.game;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.ivk.game.model.GameState;
import ru.ivk.game.model.Player;
import ru.ivk.game.model.UserColor;
import ru.ivk.game.model.UserType;
import ru.ivk.utils.io.commands.game.dto.DTO;
import ru.ivk.utils.io.commands.game.dto.GameDTO;
import ru.ivk.utils.io.commands.game.dto.MoveDTO;
import ru.ivk.utils.math.Coordinates;

import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class GameLoop {
    private final LinkedBlockingQueue<DTO> queue;

    private static GameManager gm = null;

    @Setter
    private static GameState state;

    public GameLoop(LinkedBlockingQueue<DTO> queue) {
        this.queue = queue;
        state = GameState.IDLE;
    }

    public void run() {
        Runnable r = () -> {
            while (true) {
                switch (state) {
                    case IDLE:
                        log.info("Состояние игры: {}", state);
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
                        log.info("Состояние игры: {}", state);
                        log.info("Ход игрока: {}", gm.getCurrentPlayer());
                        if (!hasActiveGame()) throw new RuntimeException("Невозможно сделать ход. Игра не была создана");
                        Player currentPlayer = gm.getCurrentPlayer();
                        Coordinates move = null;
                        if (currentPlayer.getType().equals(UserType.USER)) {
                            while (canAcceptMove()) {
                                try {
                                    DTO dto = queue.take(); // TODO: МЫ ТУТ ЗАЦИКЛИЛИСЬ
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
                            // считаем в GameEngine
                            move = new Coordinates(1,1); // TODO: WA: заглушка
                            setState(GameState.COMPUTING_PLAYERS_MOVE);
                            try {
                                log.info("SLEEPING");
                                Thread.sleep(10_000);
                                log.info("NOT SLEEPING");
                            } catch (InterruptedException e) {
                                log.info("INTERRUPTED");
                            }
                        }
                        if (move == null) throw new RuntimeException("Что-то пошло не так... Ход не был выбран");
                        log.info("Полученный/посчитанный {}: {}", move.getClass().getSimpleName(), move);
                        gm.getGameBoard().move(move);
                        System.out.printf("%s (%d, %d)%n", gm.getCurrentPlayer().getColor().getColor(), move.getX(), move.getY());
                        gm.switchCurrentPlayer();
                        setState(GameState.CHECKING_CONDITIONS);
                        break;
                    case CHECKING_CONDITIONS:
                        log.info("Состояние игры: {}", state);
                        if (true) {
                            setState(GameState.WAITING_FOR_PLAYER_MOVE);
                        } else {
                            setState(GameState.END);
                        }
                        break;
                    case END:
                        log.info("Состояние игры: {}", state);
                        setState(GameState.IDLE);
                        break;
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    public boolean hasActiveGame() {
        return gm != null;
    }

    public boolean canAcceptMove() {
        return state.equals(GameState.WAITING_FOR_PLAYER_MOVE);
    }

    public boolean canStartGame() {
        return state.equals(GameState.IDLE);
    }
}
