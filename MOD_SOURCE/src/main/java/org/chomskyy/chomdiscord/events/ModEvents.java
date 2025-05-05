package org.chomskyy.chomdiscord.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.chomskyy.chomdiscord.Chomdiscord;
import org.chomskyy.chomdiscord.commands.ConnectCommand;
import org.chomskyy.chomdiscord.commands.DisconnectCommand;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = Chomdiscord.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new ConnectCommand(event.getDispatcher());
        new DisconnectCommand(event.getDispatcher());

//        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerChat(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        String message = event.getMessage().getString();
        if (Chomdiscord.chomSocket != null) {
            try {
                Chomdiscord.chomSocket.send(("<" + player.getName().getString() + "> " + message).replaceAll("`", ""));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
