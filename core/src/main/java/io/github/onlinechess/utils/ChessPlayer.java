package io.github.onlinechess.utils;

import java.util.UUID;

import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.game.Player;
import com.github.bhlangonijr.chesslib.game.PlayerType;

public class ChessPlayer implements Player {
    private String id;
    private String name;
    private int elo;
    private PlayerType type;
    private String description;
    private final UUID uuid;
    private final Side side;
    private final boolean isHost;
    
    public ChessPlayer(String name, UUID uuid, Side side, boolean isHost) {
        this.name = name;
        this.id = uuid.toString();
        this.uuid = uuid;
        this.side = side;
        this.isHost = isHost;
        this.elo = 1200; // Default ELO
        this.type = PlayerType.HUMAN;
        this.description = "";
    }
    
    // Player interface implementation
    @Override
    public String getId() { return id; }
    
    @Override
    public void setId(String id) { this.id = id; }
    
    @Override
    public int getElo() { return elo; }
    
    @Override
    public void setElo(int elo) { this.elo = elo; }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public void setName(String name) { this.name = name; }
    
    @Override
    public PlayerType getType() { return type; }
    
    @Override
    public void setType(PlayerType type) { this.type = type; }
    
    @Override
    public String getDescription() { return description; }
    
    @Override
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String getLongDescription() {
        return name + " (" + elo + ") - " + (isHost ? "Host" : "Guest") + 
               " - Playing as " + side.name();
    }
    
    // Custom methods
    public Side getSide() { return side; }
    
    public boolean isHost() { return isHost; }
    
    public UUID getUUID() { return uuid; }
}