package org.chomskyy.chomdiscord;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ChomSocket {
    private final int loopDelayMs = 100;
    private volatile Socket socket;
    BufferedReader in;
    private DataOutputStream out;
    private volatile ArrayList<String> outputQueue = new ArrayList<>();

    private volatile boolean closed;

    private MinecraftServer server;

    // Thread for sending messages
    private Thread outputThread = new Thread(new Runnable() {
        public void run() {
            int emergency = 0;
            while (!closed) {
                if (emergency > 20) return;
                Thread.onSpinWait();
                if (socket.isClosed()) break;
                if (!outputQueue.isEmpty()) {
                    try {
                        out.writeChars(outputQueue.remove(0));
                    } catch (IOException e) {
                        emergency++;
                        System.err.println("Error: " + e);
                    }
                }
            }
            try { close(); } catch (IOException | InterruptedException e) { throw new RuntimeException(e); }
        }
    });

    // Thread for receiving messages
    private Thread inputThread = new Thread(new Runnable() {
        public void run() {
            int emergency = 0;
            while (!closed) {
                if (emergency > 20) return;
                Thread.onSpinWait();
                try {
                    String s = in.readLine();
                    if (s != null) {
                        broadcast(s);
                    }
                } catch (IOException e) {
                    emergency++;
                    System.err.println("Error: " + e);
                }
            }
            try { close(); } catch (IOException | InterruptedException e) { throw new RuntimeException(e); }
        }
    });


    public ChomSocket(String ip, int port, MinecraftServer server) throws IOException {
        this.server = server;
        socket = new Socket(ip, port);
        closed = false;


        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());

        inputThread.start();
        outputThread.start();
    }

    public void close() throws IOException, InterruptedException {
        if (closed) return;
        closed = true;

        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();
        in.close();
        out.close();

        socket = null;
    }

   public void send(String msg) throws IOException {
        outputQueue.add(msg + '\n');
   }

   public void broadcast(String msg) {
       for (ServerPlayer player : server.getPlayerList().getPlayers()) {
           player.sendSystemMessage(Component.literal(msg));
       }
       server.sendSystemMessage(Component.literal(msg));
   }
}
