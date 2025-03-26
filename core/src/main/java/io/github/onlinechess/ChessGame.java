package io.github.onlinechess;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.github.bhlangonijr.chesslib.Side;

import io.github.onlinechess.screens.ChessBoardScreen;
import io.github.onlinechess.screens.MainMenuScreen;
import io.github.onlinechess.server.Player;
import io.github.onlinechess.server.Server;

import java.util.UUID;

public class ChessGame extends Game {
    // Asset management
    private AssetManager assetManager;
    private SpriteBatch batch;
    
    // Game components
    private Server server;
    private Player localPlayer;
    
    @Override
    public void create() {
        // Initialize asset management
        assetManager = new AssetManager();
        batch = new SpriteBatch();
        
        // Load assets
        loadAssets();
        
        // Create server and local player (will be used when starting a game)
        server = new Server();
        localPlayer = new Player("Player", UUID.randomUUID(), Side.WHITE, true);
        
        // Start with the main menu screen
        setScreen(new MainMenuScreen(this)); //The constructor MainMenuScreen(ChessGame) is undefined
        // incompatible types: ChessGame cannot be converted to Main
    }
    
    private void loadAssets() {
        // Load chess board texture
        assetManager.load("pixle chess/chess boards/board_plain_01.png", Texture.class);
        
        // Load the chess piece atlas
        assetManager.load("pixle chess/chess peices/chess peices.atlas", TextureAtlas.class);
        
        // Load the white texture for coloring squares
        assetManager.load("textures/white.png", Texture.class);
        
        // Wait for all assets to finish loading
        assetManager.finishLoading();
    }
    
    public void startOfflineGame() {
        // Reset the server for a new game
        server = new Server();
        
        // Set up the local player as white (for offline games)
        localPlayer = new Player("Player", UUID.randomUUID(), Side.WHITE, true);
        
        // Switch to the chess board screen
        setScreen(new ChessBoardScreen(this));
    }
    
    public void startOnlineGame(boolean isHost) {
        // Reset the server
        server = new Server();
        
        // Set up the local player based on whether they're the host
        Side playerSide = isHost ? Side.WHITE : Side.BLACK;
        localPlayer = new Player("Player", UUID.randomUUID(), playerSide, isHost);
        
        // Switch to the chess board screen
        setScreen(new ChessBoardScreen(this));
        
        // Note: In a real implementation, you would establish network connection here
    }
    
    public void showMainMenu() {
        setScreen(new MainMenuScreen(this)); //The constructor MainMenuScreen(ChessGame) is undefined
    }
    
    public AssetManager getAssetManager() {
        return assetManager;
    }
    
    public SpriteBatch getBatch() {
        return batch;
    }
    
    public Server getServer() {
        return server;
    }
    
    public Player getLocalPlayer() {
        return localPlayer;
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
        if (screen != null) {
            screen.dispose();
        }
    }
}