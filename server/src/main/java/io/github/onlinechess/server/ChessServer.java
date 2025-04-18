// src/main/java/io/github/onlinechess/server/ChessServer.java
package io.github.onlinechess.server;

// import com.esotericsoftware.kryo.Kryo;
import java.io.IOException;

import com.esotericsoftware.kryonet.Server; // Import Log itself for setting level
import com.esotericsoftware.minlog.Log; // Import IOException
import static com.esotericsoftware.minlog.Log.LEVEL_DEBUG; // Keep static imports for info, error etc.
import static com.esotericsoftware.minlog.Log.error;
import static com.esotericsoftware.minlog.Log.info;

public class ChessServer {
    // Server Object
    private static Server server;
    // Ports to listen on:
    private final static int udpPort = 54777; 
    private final static int tcpPort = 54555;

    public static void main(String[] args) {
        // Set logging level
        Log.set(LEVEL_DEBUG);

        info("Starting the Chess Server...");

        // 1. Create Server Instance
        server = new Server();

        try {
            // 4. Bind to Ports
            server.bind(tcpPort, udpPort);
            info("Server bound to TCP port: " + tcpPort + " and UDP port: " + udpPort);

            // 5. Start the Server (in a new thread)
            server.start();
            info("Server started successfully and listening for connections.");

            // The server runs in its own thread(s).
            // The main thread can exit here, or you could add logic
            // for graceful shutdown, console commands, etc. if needed.
            // For example, keep the main thread alive until interrupted:
            // Thread.currentThread().join();

        } catch (IOException e) {
            error("Could not bind server to ports (" + tcpPort + " TCP, " + udpPort + " UDP). Is another instance running?", e);
            // Perform any necessary cleanup before exiting
            server.stop(); // Ensure server resources are released
            System.exit(1); // Exit with an error code
        } catch (Exception e) {
            error("An unexpected error occurred during server startup.", e);
            server.stop();
            System.exit(1);
        }

        // Optional: Add shutdown hook for graceful closing
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            info("Shutting down server...");
            server.stop(); // Stops listening and disconnects clients
            info("Server stopped.");
        }));
    }
}