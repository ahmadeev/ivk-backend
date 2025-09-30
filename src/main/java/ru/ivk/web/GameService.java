package ru.ivk.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ivk.common.game.GameBoard;
import ru.ivk.common.game.GameEngine;
import ru.ivk.common.game.model.UserColor;
import ru.ivk.common.math.Coordinates;
import ru.ivk.web.dto.BoardDTO;
import ru.ivk.web.dto.SimpleMoveDTO;
import ru.ivk.web.exceptions.ComputerWinGameStateException;
import ru.ivk.web.exceptions.DrawGameStateException;
import ru.ivk.web.exceptions.PlayerWinGameStateException;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    protected final GameEngine gameEngine;

    public SimpleMoveDTO getMove(BoardDTO boardDTO) {
        log.debug("Полученные данные: {}", boardDTO.toString());
        int size = boardDTO.getSize();
        String parsedColor = boardDTO.getNextPlayerColor().equalsIgnoreCase("w") ? "WHITE" : "BLACK";
        UserColor userColor = UserColor.valueOf(parsedColor);
        String[] parsedBoard = boardDTO.getData().split("");

        if (parsedBoard.length != size * size) {
            throw new IllegalArgumentException("Ошибка: переданы несогласованные аргументы size и data");
        }

        Map<Coordinates, String> board = new HashMap<>();
        for (int i = 0; i < parsedBoard.length; i++) {
            String color = parsedBoard[i].trim();
            if (color.isEmpty()) continue;
            if (!color.matches("[wbWB]")) throw new IllegalArgumentException("Ошибка: передан неверный аргумент data (цвет одного из ходов обозначен неверно)");
            board.put(new Coordinates(i % size, i / size), color.toUpperCase());
        }

        log.debug("Обработанные ходы: {}", board);

        GameBoard gameBoard = new GameBoard(size, board);

        int blackMovesCount = gameBoard.getMovesByColor(UserColor.BLACK).size();
        int whiteMovesCount = gameBoard.getMovesByColor(UserColor.WHITE).size();

        if (Math.abs(blackMovesCount - whiteMovesCount) != 1 && Math.abs(blackMovesCount - whiteMovesCount) != 0) {
            throw new IllegalArgumentException("Ошибка: передан неверный аргумент data (неверное соотношение ходов сторон)");
        }
        if (blackMovesCount > whiteMovesCount && userColor.equals(UserColor.BLACK) ||
                        whiteMovesCount > blackMovesCount && userColor.equals(UserColor.WHITE)) {
            throw new IllegalArgumentException("Ошибка: переданы несогласованные аргументы data и nextPlayerColor");
        }

        // проверяем корректность ввода: проверка, не победил ли компьютер до своего хода
        if (gameEngine.isWin(gameBoard, userColor)) {
            throw new IllegalStateException(String.format("Ошибка: найдена победа игрока %s", userColor));
        }
        // проверяем, не было ли победы после хода игрока
        UserColor otherUserColor = UserColor.getOtherColor(userColor);
        if (gameEngine.isWin(gameBoard, otherUserColor)) {
            throw new PlayerWinGameStateException(String.format("Победа игрока %s", otherUserColor)); // на самом деле не ошибка :(
        }
        if (board.size() == size * size) {
            throw new DrawGameStateException("Ничья"); // на самом деле не ошибка :(
        }

        Coordinates move = gameEngine.findBestMove(gameBoard, userColor);
        log.debug("Наилучший ход: {}", move.toString());

        // проверяем, не было ли победы после хода компьютера
        gameBoard.move(move, userColor.getColor());
        log.debug("Состояние доски после хода компьютера: {}", gameBoard.getBoard());
        if (gameEngine.isWin(gameBoard, userColor)) {
            throw new ComputerWinGameStateException(String.format("Победа игрока %s", userColor));
        }
        if (board.size() == size * size) {
            throw new DrawGameStateException("Ничья"); // на самом деле не ошибка :(
        }

        return new SimpleMoveDTO(move.getX(), move.getY(), userColor.getColor());
    }
}
