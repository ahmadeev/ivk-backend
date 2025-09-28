package ru.ivk.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ivk.web.dto.BoardDTO;
import ru.ivk.web.dto.SimpleMoveDTO;
import ru.ivk.web.utils.CustomResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/squares")
@RequiredArgsConstructor
public class GameController {
    protected final GameService gameService;

    @PostMapping("/nextMove")
    public ResponseEntity<CustomResponseEntity<SimpleMoveDTO>> getMove(@Valid @RequestBody BoardDTO boardDTO) {
        SimpleMoveDTO move = null;
        try {
            move = gameService.getMove(boardDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomResponseEntity<>(e.getMessage(), null)
            );
        }
        return ResponseEntity.ok().body(
                new CustomResponseEntity<>("", move)
        );
    }
}
