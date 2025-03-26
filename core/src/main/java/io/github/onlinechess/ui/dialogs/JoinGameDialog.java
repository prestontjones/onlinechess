package io.github.onlinechess.ui.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.github.onlinechess.Main;
import io.github.onlinechess.screens.MainMenuScreen;

/**
 * Dialog for joining an existing online game
 */
public class JoinGameDialog {

    private static final float DIALOG_WIDTH = 400;
    private static final float DIALOG_HEIGHT = 250;

    /**
     * Creates and returns a join game dialog window
     */
    public static Window create(final Main game, Stage stage, Skin skin, final MainMenuScreen parent) {
        // Create base dialog
        Object[] dialogElements = BaseDialog.createDialog("Join Game", DIALOG_WIDTH, DIALOG_HEIGHT, game, stage, skin);
        final Window window = (Window)dialogElements[0];
        Button restoreButton = (Button)dialogElements[1];
        
        // Join game content
        Table contentTable = new Table();
        contentTable.defaults().space(5.0f);
        
        // Instructions
        Label instructionsLabel = new Label("Enter the game code provided by the host to join their game.", skin);
        instructionsLabel.setWrap(true);
        contentTable.add(instructionsLabel).width(350).left().padTop(10).padBottom(15);
        
        // Game code input
        contentTable.row();
        Table inputTable = new Table();
        
        inputTable.add(new Label("Game code:", skin)).right().padRight(10);
        
        final TextField codeField = new TextField("", skin);
        codeField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        codeField.setMaxLength(6);
        inputTable.add(codeField).width(150);
        
        contentTable.add(inputTable).left();
        
        // Status message
        contentTable.row();
        final Label statusLabel = new Label("", skin);
        statusLabel.setColor(1, 0, 0, 1); // Red for errors
        statusLabel.setWrap(true);
        contentTable.add(statusLabel).width(350).padTop(10).padBottom(15);
        
        // Buttons
        contentTable.row();
        Table buttonTable = new Table();
        
        buttonTable.defaults().space(10.0f).minWidth(75.0f);
        TextButton joinButton = new TextButton("Join", skin);
        buttonTable.add(joinButton);
        
        TextButton cancelButton = new TextButton("Cancel", skin);
        buttonTable.add(cancelButton);
        
        contentTable.add(buttonTable).right().padTop(15);
        
        // Add content to window
        window.add(contentTable).grow().pad(5);
        
        // Set up minimize/restore functionality
        BaseDialog.setupMinimizeRestore(window, contentTable, restoreButton, DIALOG_HEIGHT);
        
        // Add listeners for buttons
        joinButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String code = codeField.getText();
                
                // Validate the game code
                if (code.length() != 6) {
                    statusLabel.setText("Game code must be 6 digits.");
                    return;
                }
                
                // In a real implementation, this would attempt to join the game
                // For now, just show random success/error messages
                double random = Math.random();
                if (random < 0.3) {
                    statusLabel.setText("Game not found. Please check the code and try again.");
                } else if (random < 0.6) {
                    statusLabel.setText("The game is already full or in progress.");
                } else {
                    statusLabel.setText("Connecting to game...");
                    statusLabel.setColor(0, 0.7f, 0, 1); // Green for success
                    
                    // In a real implementation, we would connect to the host
                    // For demo purposes, we'll just close the dialog after a delay
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                Gdx.app.postRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        window.remove();
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
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