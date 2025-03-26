package io.github.onlinechess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;

import io.github.onlinechess.Main;

/**
 * Settings dialog that appears over the main screen
 */
public class SettingsScreen extends BaseScreen {
    private MainMenuScreen parentScreen;
    private Window settingsWindow;
    private Table settingsTable;
    private boolean minimized = false;
    
    public SettingsScreen(final Main game) {
        super(game);
        createDialog();
    }
    
    /**
     * Sets the parent screen that will be shown when this dialog is closed
     */
    public void setParentScreen(MainMenuScreen parentScreen) {
        this.parentScreen = parentScreen;
    }
    
    private void createDialog() {
        // Create a window with OS Eight style
        settingsWindow = new Window("", skin);
        settingsWindow.setSize(450, 450);
        settingsWindow.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);
        settingsWindow.setKeepWithinStage(true);
        settingsWindow.setMovable(true);
        stage.addActor(settingsWindow);
        
        // Setup title bar
        settingsWindow.getTitleTable().clearChildren();
        settingsWindow.getTitleTable().defaults().space(5.0f);
        
        // Close button
        Button closeButton = new Button(skin, "close");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });
        settingsWindow.getTitleTable().add(closeButton).padLeft(5).padTop(5);
        
        // Title bar image
        Image titleBarImage = new Image(skin, "title-bar-bg");
        titleBarImage.setScaling(Scaling.stretchX);
        settingsWindow.getTitleTable().add(titleBarImage).growX();
        
        // Title
        Label titleLabel = new Label("Settings", skin);
        settingsWindow.getTitleTable().add(titleLabel).padLeft(20.0f).padRight(20.0f);
        
        // Right side image
        Image rightTitleBarImage = new Image(skin, "title-bar-bg");
        rightTitleBarImage.setScaling(Scaling.stretchX);
        settingsWindow.getTitleTable().add(rightTitleBarImage).growX();
        
        // Window controls
        Button restoreButton = new Button(skin, "restore");
        restoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleMinimize();
            }
        });
        settingsWindow.getTitleTable().add(restoreButton).padRight(2);
        
        Button minimizeButton = new Button(skin, "minimize");
        minimizeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleMinimize();
            }
        });
        settingsWindow.getTitleTable().add(minimizeButton).padRight(5);
        
        // Make title bar draggable
        settingsWindow.getTitleTable().addListener(new DragListener() {
            private float startX, startY;
            
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                startX = x;
                startY = y;
            }
            
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                settingsWindow.moveBy(x - startX, y - startY);
            }
        });
        
        // Settings container
        settingsTable = new Table();
        settingsTable.defaults().space(5);
        settingsTable.pad(10);
        
        // Player settings
        Label playerSettingsLabel = new Label("Player Settings", skin, "medium");
        settingsTable.add(playerSettingsLabel).colspan(2).pad(5).left().row();
        
        // Username field
        settingsTable.add(new Label("Username:", skin)).padRight(10).padTop(5).right();
        final TextField usernameField = new TextField(game.getGameSettings().getUsername(), skin);
        settingsTable.add(usernameField).width(200).padTop(5).left().row();
        
        // Audio settings
        Label audioSettingsLabel = new Label("Audio Settings", skin, "medium");
        settingsTable.add(audioSettingsLabel).colspan(2).padTop(15).pad(5).left().row();
        
        // Sound checkbox
        final CheckBox soundCheckbox = new CheckBox(" Sound Effects", skin);
        soundCheckbox.setChecked(game.getGameSettings().isSoundEnabled());
        settingsTable.add(soundCheckbox).colspan(2).pad(3).left().row();
        
        // Music volume
        settingsTable.add(new Label("Music Volume:", skin)).padRight(10).padTop(5).right();
        final Slider musicSlider = new Slider(0, 1, 0.1f, false, skin);
        musicSlider.setValue(game.getGameSettings().getMusicVolume());
        settingsTable.add(musicSlider).width(200).padTop(5).left().row();
        
        // SFX volume
        settingsTable.add(new Label("SFX Volume:", skin)).padRight(10).padTop(5).right();
        final Slider sfxSlider = new Slider(0, 1, 0.1f, false, skin);
        sfxSlider.setValue(game.getGameSettings().getSfxVolume());
        settingsTable.add(sfxSlider).width(200).padTop(5).left().row();
        
        // Game settings
        Label gameSettingsLabel = new Label("Game Settings", skin, "medium");
        settingsTable.add(gameSettingsLabel).colspan(2).padTop(15).pad(5).left().row();
        
        // AI difficulty
        settingsTable.add(new Label("AI Difficulty:", skin)).padRight(10).padTop(5).right();
        final SelectBox<String> difficultySelect = new SelectBox<>(skin);
        Array<String> difficulties = new Array<>(new String[]{"Easy", "Medium", "Hard"});
        difficultySelect.setItems(difficulties);
        difficultySelect.setSelectedIndex(game.getGameSettings().getAiDifficulty() - 1);
        settingsTable.add(difficultySelect).width(200).padTop(5).left().row();
        
        // AI commentary checkbox
        final CheckBox commentaryCheckbox = new CheckBox(" AI Commentary", skin);
        commentaryCheckbox.setChecked(game.getGameSettings().isAiCommentaryEnabled());
        settingsTable.add(commentaryCheckbox).colspan(2).pad(3).left().row();
        
        // Control buttons
        Table buttonTable = new Table();
        TextButton saveButton = new TextButton("Save", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);
        
        buttonTable.add(saveButton).minWidth(100).pad(10);
        buttonTable.add(cancelButton).minWidth(100).pad(10);
        
        settingsTable.add(buttonTable).colspan(2).padTop(20).row();
        
        // Add settings to window
        settingsWindow.add(settingsTable).grow().pad(10);
        
        // Add listeners
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Save all settings
                game.getGameSettings().setUsername(usernameField.getText());
                game.getGameSettings().setSoundEnabled(soundCheckbox.isChecked());
                game.getGameSettings().setMusicVolume(musicSlider.getValue());
                game.getGameSettings().setSfxVolume(sfxSlider.getValue());
                game.getGameSettings().setAiDifficulty(difficultySelect.getSelectedIndex() + 1);
                game.getGameSettings().setAiCommentaryEnabled(commentaryCheckbox.isChecked());
                
                close();
            }
        });
        
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                close();
            }
        });
    }
    
    /**
     * Close this dialog and return to parent screen
     */
    private void close() {
        settingsWindow.remove();
        if (parentScreen != null) {
            parentScreen.showSettingsDialog();
        }
    }
    
    /**
     * Toggle between minimized and normal states
     */
    private void toggleMinimize() {
        minimized = !minimized;
        
        if (minimized) {
            settingsWindow.getCell(settingsTable).height(0).padBottom(0).padTop(0);
            settingsWindow.invalidate();
            settingsWindow.pack();
        } else {
            settingsWindow.getCell(settingsTable).height(450).pad(10);
            settingsWindow.invalidate();
            settingsWindow.setHeight(450);
            settingsWindow.validate();
        }
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        
        // Adjust window position to stay within the screen
        float x = settingsWindow.getX();
        float y = settingsWindow.getY();
        
        if (x < 0) x = 0;
        if (x > width - settingsWindow.getWidth()) x = width - settingsWindow.getWidth();
        if (y < 0) y = 0;
        if (y > height - settingsWindow.getHeight()) y = height - settingsWindow.getHeight();
        
        settingsWindow.setPosition(x, y);
    }
    
    @Override
    public void dispose() {
        super.dispose();
    }
}