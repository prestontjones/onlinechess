package com.presto.onlinechess.screens.ui.dialogs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.presto.onlinechess.Main;
import com.presto.onlinechess.screens.MainMenuScreen;

/**
 * Dialog for hosting a new online game
 */
public class HostGameDialog {

    private static final float DIALOG_WIDTH = 400;
    private static final float DIALOG_HEIGHT = 300;

    /**
     * Creates and returns a host game dialog window
     */
    public static Window create(final Main game, Stage stage, Skin skin, final MainMenuScreen parent) {
        // Generate a random game code
        final int gameCode = 100000 + (int)(Math.random() * 900000);
        
        // Create base dialog
        Object[] dialogElements = BaseDialog.createDialog("Host Game", DIALOG_WIDTH, DIALOG_HEIGHT, game, stage, skin);
        final Window window = (Window)dialogElements[0];
        Button restoreButton = (Button)dialogElements[1];
        
        // Host game settings
        Table contentTable = new Table();
        contentTable.defaults().space(5.0f);
        
        // Game code label
        contentTable.add(new Label("Your game code is:", skin)).left().padTop(10);
        
        contentTable.row();
        // Game code display
        Label codeLabel = new Label(String.valueOf(gameCode), skin, "title");
        contentTable.add(codeLabel).center().padTop(5).padBottom(15);
        
        contentTable.row();
        // Instructions
        Label instructionsLabel = new Label("Share this code with your opponent so they can join your game.", skin);
        instructionsLabel.setWrap(true);
        contentTable.add(instructionsLabel).width(350).padBottom(10);
        
        contentTable.row();
        // Status message
        final Label statusLabel = new Label("Waiting for player to join...", skin);
        statusLabel.setWrap(true);
        contentTable.add(statusLabel).width(350).padTop(10).padBottom(15);
        
        contentTable.row();
        // Buttons
        Table buttonTable = new Table();
        
        buttonTable.defaults().space(10.0f).minWidth(75.0f);
        TextButton startButton = new TextButton("Start Game", skin);
        buttonTable.add(startButton);
        
        TextButton cancelButton = new TextButton("Cancel", skin);
        buttonTable.add(cancelButton);
        
        contentTable.add(buttonTable).right().padTop(15);
        
        // Add content to window
        window.add(contentTable).grow().pad(5);
        
        // Set up minimize/restore functionality
        BaseDialog.setupMinimizeRestore(window, contentTable, restoreButton, DIALOG_HEIGHT);
        
        // Add listeners for buttons
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // In a real implementation, this would start the game if a player has joined
                // For now, just show a message
                statusLabel.setText("No player has joined yet.");
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
