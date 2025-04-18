// src/main/java/io/github/onlinechess/server/NetworkRegistry.java
package io.github.onlinechess.shared;

import com.esotericsoftware.kryo.Kryo;
import static com.esotericsoftware.minlog.Log.error;
import static com.esotericsoftware.minlog.Log.info;

// TODO: Import your actual network message classes here

/**
 * Helper class to register classes for Kryo serialization across the network.
 */
public class NetworkRegistry {

    public static void register(Kryo kryo) {
        if (kryo == null) {
            error("Kryo instance is null. Cannot register classes.");
            return;
        }
        info("Registering network classes...");

        // It's crucial to register classes with Kryo on both
        // the client and server side in the SAME ORDER.

        // Register primitive types and common Java types if needed (often handled automatically, but explicit can be safer)
        // kryo.register(int.class);
        // kryo.register(String.class);
        // kryo.register(boolean.class);
        // kryo.register(java.util.ArrayList.class);

        // TODO: Register your shared NetworkMessage base class and all subclasses
        // Example:
        // kryo.register(io.github.onlinechess.shared.network.NetworkMessage.class); // Assuming a shared module
        // kryo.register(io.github.onlinechess.shared.network.messages.ClientLoginRequest.class);
        // kryo.register(io.github.onlinechess.shared.network.messages.ServerLoginResponse.class);
        // kryo.register(io.github.onlinechess.shared.network.messages.MakeMoveRequest.class);
        // kryo.register(io.github.onlinechess.shared.network.messages.GameStateUpdate.class);
        // kryo.register(io.github.onlinechess.shared.network.messages.GameSessionInfo.class);
        // kryo.register(io.github.onlinechess.shared.network.messages.PlayerInfo.class); // If sending player objects

        // Register any complex objects used within your messages if not automatically handled
        // Example: If GameStateUpdate contains a custom BoardState object:
        // kryo.register(io.github.onlinechess.shared.BoardState.class);

        info("Network class registration complete.");
    }
}