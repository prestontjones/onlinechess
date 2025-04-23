package com.presto.onlinechess;

import com.esotericsoftware.kryo.Kryo;
import static com.esotericsoftware.minlog.Log.error;
import static com.esotericsoftware.minlog.Log.info;


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
        // kryo.register(io.github.onlinechess.shared.packets.ChatPacket.class);


        // Register any complex objects used within your messages if not automatically handled
        // Example: If GameStateUpdate contains a custom BoardState object:
        // kryo.register(io.github.onlinechess.shared.BoardState.class);

        info("Network class registration complete.");
    }
}
