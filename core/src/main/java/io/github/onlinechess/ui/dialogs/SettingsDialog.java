package io.github.onlinechess.ui.dialogs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

import io.github.onlinechess.Main;
import io.github.onlinechess.screens.MainMenuScreen;

/**
 * Settings dialog window that appears over the main screen
 */
public class SettingsDialog {
    
    private static final float DIALOG_WIDTH = 500;
    private static final float DIALOG_HEIGHT = 450;
    
    /**
     * Creates and returns a settings dialog window
     */
    public static Window create(final Main game, Stage stage, Skin skin, final MainMenuScreen parent) {
        // Create base dialog
        Object[] dialogElements = BaseDialog.createDialog("Settings", DIALOG_WIDTH, DIALOG_HEIGHT, game, stage, skin);
        final Window window = (Window)dialogElements[0];
        Button restoreButton = (Button)dialogElements[1];
        
        // Settings container
        final Table settingsTable = new Table();
        settingsTable.defaults().space(5.0f);
        
        // Player settings section
        Label playerSettingsLabel = new Label("Player Settings", skin, "medium");
        settingsTable.add(playerSettingsLabel).colspan(2).left().padTop(10.0f);
        
        settingsTable.row();
        settingsTable.add(new Label("Username:", skin)).right().padRight(10);
        
        final TextField usernameField = new TextField(game.getGameSettings().getUsername(), skin);
        settingsTable.add(usernameField).width(200).left().growX();
        
        // Audio settings section
        settingsTable.row().padTop(15);
        Label audioSettingsLabel = new Label("Audio Settings", skin, "medium");
        settingsTable.add(audioSettingsLabel).colspan(2).left();
        
        // Sound checkbox
        settingsTable.row();
        final CheckBox soundCheckbox = new CheckBox(" Sound Effects", skin);
        soundCheckbox.setChecked(game.getGameSettings().isSoundEnabled());
        settingsTable.add(soundCheckbox).colspan(2).left();
        
        // Music volume
        settingsTable.row();
        settingsTable.add(new Label("Music Volume:", skin)).right().padRight(10);
        
        final Slider musicSlider = new Slider(0, 1, 0.1f, false, skin, "volume-horizontal");
        musicSlider.setValue(game.getGameSettings().getMusicVolume());
        settingsTable.add(musicSlider).growX();
        
        // SFX volume
        settingsTable.row();
        settingsTable.add(new Label("SFX Volume:", skin)).right().padRight(10);
        
        final Slider sfxSlider = new Slider(0, 1, 0.1f, false, skin, "volume-horizontal");
        sfxSlider.setValue(game.getGameSettings().getSfxVolume());
        settingsTable.add(sfxSlider).growX();
        
        // Game settings
        settingsTable.row().padTop(15);
        Label gameSettingsLabel = new Label("Game Settings", skin, "medium");
        settingsTable.add(gameSettingsLabel).colspan(2).left();
        
        // AI difficulty
        settingsTable.row();
        settingsTable.add(new Label("AI Difficulty:", skin)).right().padRight(10);
        
        final SelectBox<String> difficultySelect = new SelectBox<>(skin);
        Array<String> difficulties = new Array<>(new String[]{"Easy", "Medium", "Hard"});
        difficultySelect.setItems(difficulties);
        difficultySelect.setSelectedIndex(game.getGameSettings().getAiDifficulty() - 1);
        settingsTable.add(difficultySelect).growX();
        
        // AI commentary checkbox
        settingsTable.row();
        final CheckBox commentaryCheckbox = new CheckBox(" AI Commentary", skin);
        commentaryCheckbox.setChecked(game.getGameSettings().isAiCommentaryEnabled());
        settingsTable.add(commentaryCheckbox).colspan(2).left();
        
        // Buttons
        settingsTable.row().padTop(20);
        Table buttonTable = new Table();
        
        buttonTable.defaults().space(10.0f).minWidth(75.0f);
        TextButton saveButton = new TextButton("Save", skin);
        buttonTable.add(saveButton);
        
        TextButton cancelButton = new TextButton("Cancel", skin);
        buttonTable.add(cancelButton);
        
        settingsTable.add(buttonTable).colspan(2).right();
        
        // Add settings to window
        window.add(settingsTable).grow().pad(5);
        
        // Set up minimize/restore functionality
        BaseDialog.setupMinimizeRestore(window, settingsTable, restoreButton, DIALOG_HEIGHT);
        
        // Add listeners for save and cancel buttons
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
                
                window.remove();
            }
        });
        
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                window.remove();
            }
        });
        
        return window;
    }
}