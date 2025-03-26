package io.github.onlinechess.server;

import java.util.UUID;

import com.github.bhlangonijr.chesslib.Side;

public class Player {
    private String username;
    @SuppressWarnings("unused")
    private final UUID UUID;
    private final Side side;
    @SuppressWarnings("unused")
    private final Boolean host;
                    
    public Player(String username, UUID uuid, Side side, Boolean host) {
        this.username = username;
        this.UUID = uuid; //Internal UserID
        this.side = side; //White or Black
        this.host = host; //Host or Client
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public String getUsername() {return username;}
    public Side getSide() {return side;}
}
 