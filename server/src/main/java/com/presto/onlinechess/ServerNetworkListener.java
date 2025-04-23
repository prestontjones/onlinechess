package com.presto.onlinechess;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import static com.esotericsoftware.minlog.Log.info;
import static com.esotericsoftware.minlog.Log.warn;

// TODO: Import your message classes

/**
 * Handles network events for the ChessServer.
 * Delegates processing of messages to appropriate handlers.
 */
public class ServerNetworkListener extends Listener {

    private final Server server;
    // TODO: Add references to game managers or session handlers
    // Example: private final GameManager gameManager;

    public ServerNetworkListener(Server server /*, GameManager gameManager */) {
        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null.");
        }
        this.server = server;
        // this.gameManager = gameManager; // Initialize dependencies
        info("ServerNetworkListener initialized.");
    }

    @Override
    public void connected(Connection connection) {
        // Called when a new client connects.
        info("[" + connection.getID() + "] Client connected: " + connection.getRemoteAddressTCP());
        // TODO: Implement logic for new connections
        // - Maybe send a welcome message or request authentication
        // - Add connection to a list of waiting players, etc.
        // Example: server.sendToTCP(connection.getID(), new ServerWelcomeMessage("Welcome!"));
        // Example: PlayerManager.addWaitingPlayer(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        // Called when a client disconnects.
        info("[" + connection.getID() + "] Client disconnected: " + connection.getRemoteAddressTCP());
        // TODO: Implement logic for disconnections
        // - Remove player from any active game session
        // - Notify opponent if in a game
        // - Clean up resources associated with the connection
        // Example: gameManager.handleDisconnect(connection);
        // Example: PlayerManager.removePlayer(connection);
    }

    @Override
    public void received(Connection connection, Object object) {
        // Called when a message is received from a client.
        // This is the main routing logic.
        if (object == null) {
            warn("[" + connection.getID() + "] Received null object.");
            return;
        }

        info("[" + connection.getID() + "] Received message: " + object.getClass().getSimpleName());

        // TODO: Delegate message handling based on the type of 'object'
        // This is where you route commands to your game logic/session managers

        // Example routing:
        /*
        if (object instanceof ClientLoginRequest) {
            AuthenticationService.handleLogin(connection, (ClientLoginRequest) object);
        } else if (object instanceof FindMatchRequest) {
            MatchmakingService.handleFindMatch(connection, (FindMatchRequest) object);
        } else if (object instanceof MakeMoveRequest) {
            gameManager.handlePlayerMove(connection, (MakeMoveRequest) object);
        } else if (object instanceof ChatMessage) {
            ChatService.broadcastMessage(connection, (ChatMessage) object);
        } else {
            warn("[" + connection.getID() + "] Received unhandled message type: " + object.getClass().getName());
            // Optionally send an error back to the client
            // server.sendToTCP(connection.getID(), new ErrorMessage("Unknown command"));
        }
        */
    }

    @Override
    public void idle(Connection connection) {
        // Called when a connection has been idle (no messages received) for a configured time.
        // Useful for detecting dead connections, though TCP keep-alive is often better.
        // trace("[" + connection.getID() + "] Connection idle: " + connection.getRemoteAddressTCP());
    }

    // Add private helper methods here to handle specific message types if desired,
    // though delegation to separate service/manager classes is often cleaner.
    // private void handleLogin(Connection c, ClientLoginRequest msg) { ... }
}
