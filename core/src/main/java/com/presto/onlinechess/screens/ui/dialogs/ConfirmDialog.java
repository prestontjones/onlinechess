package com.presto.onlinechess.screens.ui.dialogs;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * A modern styled confirmation dialog with Yes/No options
 */
public class ConfirmDialog {
    
    /**
     * Interface for confirm dialog callbacks
     */
    public interface ConfirmListener {
        void onConfirm();
    }
    
    /**
     * Creates and returns a confirmation dialog window
     * 
     * @param title The dialog title
     * @param message The dialog message/question
     * @param listener Callback for when user confirms
     * @param stage The stage to add the dialog to
     * @param skin The UI skin to use
     * @return The created window
     */
    public static Window create(String title, String message, final ConfirmListener listener, 
                               Stage stage, Skin skin) {
        // Base dialog size
        final float DIALOG_WIDTH = 400;
        final float DIALOG_HEIGHT = 200;
        
        // Create base dialog
        Object[] dialogElements = BaseDialog.createDialog(title, DIALOG_WIDTH, DIALOG_HEIGHT, null, stage, skin);
        final Window window = (Window)dialogElements[0];
        Button restoreButton = (Button)dialogElements[1];
        
        // Content table
        Table contentTable = new Table();
        contentTable.defaults().space(5.0f);
        
        // Message with proper padding and wrapping
        Label messageLabel = new Label(message, skin);
        messageLabel.setWrap(true);
        contentTable.add(messageLabel).width(350).pad(20).padBottom(30).row();
        
        // Buttons
        Table buttonTable = new Table();
        buttonTable.defaults().space(15.0f).minWidth(100.0f).height(38f);
        
        // Confirm button with effects
        TextButton confirmButton = new TextButton("Yes", skin);
        addButtonEffects(confirmButton);
        confirmButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Add closing animation
                window.addAction(Actions.sequence(
                    Actions.fadeOut(0.2f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onConfirm();
                            }
                            window.remove();
                        }
                    })
                ));
            }
        });
        buttonTable.add(confirmButton);
        
        // Cancel button with effects
        TextButton cancelButton = new TextButton("No", skin);
        addButtonEffects(cancelButton);
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Add closing animation
                window.addAction(Actions.sequence(
                    Actions.fadeOut(0.2f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            window.remove();
                        }
                    })
                ));
            }
        });
        buttonTable.add(cancelButton);
        
        contentTable.add(buttonTable).padBottom(20);
        
        // Add content to window
        window.add(contentTable).grow().pad(5);
        
        // Set up minimize/restore functionality
        BaseDialog.setupMinimizeRestore(window, contentTable, restoreButton, DIALOG_HEIGHT);
        
        // Add an appearance animation
        window.getColor().a = 0f;
        window.addAction(Actions.fadeIn(0.3f));
        
        // Center the window
        window.setPosition(
            stage.getWidth() / 2 - window.getWidth() / 2,
            stage.getHeight() / 2 - window.getHeight() / 2
        );
        
        return window;
    }
    
    /**
     * Adds hover effects to a button
     */
    private static void addButtonEffects(final TextButton button) {
        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                button.addAction(Actions.color(button.getColor().cpy().mul(1.1f, 1.1f, 1.2f, 1f), 0.15f));
            }
            
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                button.addAction(Actions.color(button.getColor().cpy().mul(1/1.1f, 1/1.1f, 1/1.2f, 1f), 0.15f));
            }
        });
    }
}