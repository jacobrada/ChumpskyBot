package org.chomskyy.chomdiscord.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.chomskyy.chomdiscord.ChomSocket;
import org.chomskyy.chomdiscord.Chomdiscord;

import java.io.IOException;

public class ConnectCommand {
    public ConnectCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("chom:connect")
                .requires((command) -> command.hasPermission(2))
                .then(Commands.argument("ip", StringArgumentType.word())
                        .then(Commands.argument("port", IntegerArgumentType.integer(3000, 65535))
                                .executes(this::execute)
                        )
                )
        );
    }

    private int execute(CommandContext<CommandSourceStack> command) throws CommandSyntaxException {
        CommandSourceStack source = command.getSource();
        if (source.isPlayer()) {
            ServerPlayer player = source.getPlayer();
            assert player != null;
            MinecraftServer server = player.getServer();

            player.sendSystemMessage(Component.literal("Attempting to start Web Socket Thread..."));
            if (Chomdiscord.chomSocket != null) {
                player.sendSystemMessage(Component.literal("§cAlready Connected!"));
            } else {
                try {
                    Chomdiscord.chomSocket = new ChomSocket(StringArgumentType.getString(command, "ip") ,IntegerArgumentType.getInteger(command, "port") , server);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return 1;
    }
}
