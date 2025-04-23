package com.presto.onlinechess.packets;

public class ChatPacket {
    private String message;
    private String username;

    public ChatPacket() {}

    public ChatPacket(String message, String username) {
        this.message = message;
        this.username = username;
    }
}
