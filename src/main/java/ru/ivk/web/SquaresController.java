package ru.ivk.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/squares")
@RequiredArgsConstructor
public class SquaresController {
    @GetMapping("/nextMove")
    public ResponseEntity<String> getMove() {
        return ResponseEntity.ok().body("Hello World!");
    }
}
