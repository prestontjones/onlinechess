package io.github.onlinechess;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import io.github.onlinechess.screens.MainMenuScreen;

/** Main game class that manages screens and global resources */
public class Main extends Game {
    private SpriteBatch batch;
    private Skin skin;
    private GameSettings gameSettings;
    
    @Override
    public void create() {
        batch = new SpriteBatch();
        
        // Load OS Eight skin
        skin = new Skin(Gdx.files.internal("os8ui/OS Eight.json"));
        
        // Enable markup for title fonts if needed
        skin.get("title-color", LabelStyle.class).font.getData().markupEnabled = true;
        
        // Initialize game settings
        gameSettings = new GameSettings();
        
        // Set initial screen
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Let the parent handle the screen rendering
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        skin.dispose();
        
        // Also dispose the current screen
        if (screen != null) {
            screen.dispose();
        }
    }
    
    /**
     * Get the sprite batch for drawing
     */
    public SpriteBatch getBatch() {
        return batch;
    }
    
    /**
     * Get the UI skin
     */
    public Skin getSkin() {
        return skin;
    }
    
    /**
     * Get the game settings
     */
    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public AssetManager getAssetManager() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAssetManager'");
    }
}