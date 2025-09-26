package ru.ivk.utils.io.commands.game;

import lombok.AllArgsConstructor;
import ru.ivk.utils.io.commands.Command;
import ru.ivk.utils.io.commands.CommandResult;
import ru.ivk.utils.io.commands.game.dto.MoveDTO;

@AllArgsConstructor
public class MoveCommand extends Command<MoveDTO> {
    private final String[] args;

    @Override
    public CommandResult<MoveDTO> execute() {
        MoveDTO dto = this.convertArgs(this.args);
        return CommandResult.success(true, dto);
    }

    @Override
    protected MoveDTO convertArgs(String[] args) {
        int x = Integer.parseInt(args[0]);
        int y = Integer.parseInt(args[1]);
        return new MoveDTO(x, y);
    }
}
