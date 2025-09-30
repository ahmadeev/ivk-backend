package ru.ivk.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ivk.web.dto.BoardDTO;
import ru.ivk.web.utils.GameResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/squares")
@RequiredArgsConstructor
public class GameController {
    protected final GameService gameService;

    @PostMapping("/nextMove")
    public ResponseEntity<GameResponseEntity> getMove(@Valid @RequestBody BoardDTO boardDTO) {
        try {
            GameResponseEntity res = gameService.getMove(boardDTO);
            return ResponseEntity.ok().body(res);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GameResponseEntity.error(e.getMessage()));
        }

    }
}
