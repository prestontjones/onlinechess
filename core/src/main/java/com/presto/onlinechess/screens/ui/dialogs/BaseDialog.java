package com.presto.onlinechess.screens.ui.dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.presto.onlinechess.Main;

//Base dialog utility class that provides common dialog window setup functionality
public class BaseDialog {
    
    /**
     * Creates a base dialog window with OS Eight styling
     * 
     * @param title Dialog title
     * @param width Window width
     * @param height Window height
     * @param game Game instance
     * @param stage Stage to add the window to
     * @param skin UI skin
     * @return Configured window with [0]=window, [1]=restore button
     */
    public static Object[] createDialog(String title, float width, float height, Main game, Stage stage, Skin skin) {
        // Create a standard window with OS Eight style
        final Window window = new Window("", skin);
        window.setSize(width, height);
        window.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, Align.center);
        window.setKeepWithinStage(true);
        window.setMovable(false); // We'll handle this manually
        
        stage.addActor(window);
        
        // Clear and set up title bar
        window.getTitleTable().clearChildren();
        window.getTitleTable().defaults().space(5.0f);
        
        // Close button
        Button closeButton = new Button(skin, "close");
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
            }
        });
        window.getTitleTable().add(closeButton);
        
        // Title bar image left
        Image titleBarBgLeft = new Image(skin, "title-bar-bg");
        titleBarBgLeft.setScaling(Scaling.stretchX);
        window.getTitleTable().add(titleBarBgLeft).growX();
        
        // Title
        Label titleLabel = new Label(title, skin);
        window.getTitleTable().add(titleLabel).padLeft(20.0f).padRight(20.0f);
        
        // Title bar image right
        Image titleBarBgRight = new Image(skin, "title-bar-bg");
        titleBarBgRight.setScaling(Scaling.stretchX);
        window.getTitleTable().add(titleBarBgRight).growX();
        
        // Restore button
        Button restoreButton = new Button(skin, "restore");
        window.getTitleTable().add(restoreButton);
        
        // Minimize button
        Button minimizeButton = new Button(skin, "minimize");
        minimizeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.remove();
            }
        });
        window.getTitleTable().add(minimizeButton);
        
        // Make title bar draggable
        window.getTitleTable().addListener(new DragListener() {
            private float startX, startY;
            
            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                startX = x;
                startY = y;
            }
            
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                window.moveBy(x - startX, y - startY);
            }
        });
        
        // Return both the window and the restore button
        return new Object[] { window, restoreButton };
    }
    
    /**
     * Sets up minimize/restore functionality for a window
     * 
     * @param window The window
     * @param contentTable The content table to minimize/restore
     * @param restoreButton The restore button
     * @param height The normal height of the window
     */
    public static void setupMinimizeRestore(final Window window, final Table contentTable, 
                                            Button restoreButton, final float height) {
        final boolean[] minimized = {false};
        
        restoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                minimized[0] = !minimized[0];
                
                if (minimized[0]) {
                    // Minimize
                    window.getCell(contentTable).height(0).padBottom(0).padTop(0);
                    window.invalidate();
                    window.pack();
                } else {
                    // Restore
                    window.getCell(contentTable).height(height - 30).pad(5);
                    window.invalidate();
                    window.setHeight(height);
                    window.validate();
                }
            }
        });
    }
}
