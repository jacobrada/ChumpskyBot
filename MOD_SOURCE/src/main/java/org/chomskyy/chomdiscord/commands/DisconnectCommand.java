package org.chomskyy.chomdiscord.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.chomskyy.chomdiscord.Chomdiscord;

import java.io.IOException;
import java.util.Objects;

public class DisconnectCommand {
    public DisconnectCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chom:disconnect").executes((command) -> execute(command.getSource())));
    }

    private int execute(CommandSourceStack source) throws CommandSyntaxException {
        if (Chomdiscord.chomSocket == null) {
            Objects.requireNonNull(source.getPlayer()).sendSystemMessage(Component.literal("Nothing to Disconnect :("));
        } else {
            try {
                Chomdiscord.chomSocket.close();
                Chomdiscord.chomSocket = null;
                Objects.requireNonNull(source.getPlayer()).sendSystemMessage(Component.literal("Disconnected from server"));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return 0;
    }
}
