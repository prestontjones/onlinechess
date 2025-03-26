package io.github.onlinechess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.onlinechess.Main;

/**
 * Base screen class that handles common functionality for all screens
 */
public abstract class BaseScreen implements Screen {
    protected final Main game;
    protected final Stage stage;
    protected final Skin skin;
    
    public BaseScreen(final Main game) {
        this.game = game;
        
        // Use ScreenViewport for flexible UI that scales with window size
        this.stage = new Stage(new ScreenViewport());
        this.skin = game.getSkin();
        
        // Set this stage as the input processor
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void show() {
        // Called when this screen becomes the current screen
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Update and draw the stage
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        // Update viewport when the screen size changes
        stage.getViewport().update(width, height, true);
    }
    
    @Override
    public void pause() {
        // Called when the game is paused
    }
    
    @Override
    public void resume() {
        // Called when the game is resumed
    }
    
    @Override
    public void hide() {
        // Called when this screen is no longer the current screen
    }
    
    @Override
    public void dispose() {
        // Dispose stage to prevent memory leaks
        stage.dispose();
    }
}