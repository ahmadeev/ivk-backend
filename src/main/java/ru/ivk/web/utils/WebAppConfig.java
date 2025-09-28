package ru.ivk.web.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.ivk.common.game.GameEngine;

@Configuration
public class WebAppConfig {
    @Bean
    public GameEngine gameEngine() {
        return new GameEngine();
    }
}
