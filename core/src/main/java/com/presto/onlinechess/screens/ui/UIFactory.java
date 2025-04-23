package com.presto.onlinechess.screens.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Scaling;

/**
 * Factory class for creating consistent UI elements
 */
public class UIFactory {
    
    /**
     * Creates a Mac-style title bar with window controls
     */
    public static Table createTitleBar(Skin skin, String title) {
        Table headerTable = new Table();
        headerTable.setBackground(skin.getDrawable("window"));
        
        // Window controls
        Table controls = new Table();
        Button closeButton = new Button(skin, "close");
        Button minimizeButton = new Button(skin, "minimize");
        Button restoreButton = new Button(skin, "restore");
        
        headerTable.add(closeButton).padLeft(10).padTop(5).padBottom(5);
        
        // Create title image
        Image titleBarImage = new Image(skin.getDrawable("white"));
        titleBarImage.setScaling(Scaling.stretchX);
        headerTable.add(titleBarImage).growX();
        
        // Title label
        Label titleLabel = new Label(title, skin);
        headerTable.add(titleLabel).expandX().center();
        
        // Right side image
        Image rightTitleBarImage = new Image(skin.getDrawable("white"));
        rightTitleBarImage.setScaling(Scaling.stretchX);
        headerTable.add(rightTitleBarImage).growX();

        headerTable.add(restoreButton).padRight(5);
        headerTable.add(minimizeButton).padRight(10);
        
        return headerTable;
    }
    
    /**
     * Creates a footer bar with version information
     */
    public static Table createFooter(Skin skin, String version) {
        Table footerTable = new Table();
        footerTable.setBackground(skin.getDrawable("window"));
        
        Label versionLabel = new Label(version, skin);
        versionLabel.setColor(Color.DARK_GRAY);
        
        footerTable.add(versionLabel).expandX().center().pad(5);
        
        return footerTable;
    }
    
    /**
     * Creates a standard menu button with proper sizing
     */
    public static TextButton createMenuButton(String text, Skin skin) {
        TextButton button = new TextButton(text, skin);
        return button;
    }
    
    /**
     * Creates a content window with styled background
     */
    public static Table createContentTable(Skin skin) {
        Table contentTable = new Table();
        contentTable.setBackground(skin.getDrawable("white-rect"));
        return contentTable;
    }
}
