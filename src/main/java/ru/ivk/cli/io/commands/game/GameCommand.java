package ru.ivk.cli.io.commands.game;

import lombok.AllArgsConstructor;
import ru.ivk.cli.io.commands.Command;
import ru.ivk.cli.io.commands.CommandResult;
import ru.ivk.cli.io.commands.game.dto.GameDTO;

@AllArgsConstructor
public class GameCommand extends Command<GameDTO> {
    private final String[] args;

    @Override
    public CommandResult<GameDTO> execute() {
        GameDTO dto = this.convertArgs(this.args);
        return CommandResult.success(true, dto);
    }

    @Override
    protected GameDTO convertArgs(String[] args) {
        int n = Integer.parseInt(args[0]);
        String player1Type = args[1].split("\\s++")[0];
        String player1Color = args[1].split("\\s++")[1];
        String player2Type = args[2].split("\\s++")[0];
        String player2Color = args[2].split("\\s++")[1];
        return new GameDTO(
                n,
                player1Type,
                player1Color,
                player2Type,
                player2Color
        );
    }
}
