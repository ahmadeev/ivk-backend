package ru.ivk.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ivk.web.dto.BoardDTO;
import ru.ivk.web.dto.GameStateDTO;
import ru.ivk.web.dto.SimpleMoveDTO;
import ru.ivk.web.exceptions.ComputerWinGameStateException;
import ru.ivk.web.exceptions.DrawGameStateException;
import ru.ivk.web.exceptions.PlayerWinGameStateException;
import ru.ivk.web.utils.CustomResponseEntity;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/squares")
@RequiredArgsConstructor
public class GameController {
    protected final GameService gameService;

    @PostMapping("/nextMove")
    public ResponseEntity<CustomResponseEntity<?>> getMove(@Valid @RequestBody BoardDTO boardDTO) {
        try {
            SimpleMoveDTO move = gameService.getMove(boardDTO);
            return ResponseEntity.ok().body(
                    new CustomResponseEntity<>("", GameState.IN_PROCESS.toString(), move)
            );
        } catch (PlayerWinGameStateException e) {
            return ResponseEntity.ok().body(
                    new CustomResponseEntity<>(e.getMessage(), GameState.END.toString(),new GameStateDTO(EndGameState.PLAYER_WIN.toString()))
            );
        } catch (ComputerWinGameStateException e) {
            return ResponseEntity.ok().body(
                    new CustomResponseEntity<>(e.getMessage(), GameState.END.toString(),new GameStateDTO(EndGameState.COMPUTER_WIN.toString()))
            );
        } catch (DrawGameStateException e) {
            return ResponseEntity.ok().body(
                    new CustomResponseEntity<>(e.getMessage(), GameState.END.toString(), new GameStateDTO(EndGameState.DRAW.toString()))
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new CustomResponseEntity<>(e.getMessage(), GameState.INVALID.toString(), null)
            );
        }

    }
}
