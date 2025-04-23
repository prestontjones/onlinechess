package com.presto.onlinechess.packets;

public class GameStatusPacket {
    private String message;

    // Required no-arg constructor for Kryo serialization
    public GameStatusPacket () {}

    public GameStatusPacket (String message) {
        this.message = message;
    }
}
