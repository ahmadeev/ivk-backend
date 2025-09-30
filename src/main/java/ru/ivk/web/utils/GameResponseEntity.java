package ru.ivk.web.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ivk.web.dto.SimpleMoveDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameResponseEntity {
    private GameState status;
    private GameResult result;
    private String details;
    private SimpleMoveDTO move;

    public static GameResponseEntity error(String details) {
        return new GameResponseEntity(GameState.INVALID, null, details, null);
    }
}
