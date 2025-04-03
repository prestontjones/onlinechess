package io.github.onlinechess.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import io.github.onlinechess.Main;
import io.github.onlinechess.ui.ChessBoardActor;
import io.github.onlinechess.ui.ChessThemeSetter;

/**
 * Screen that renders a chess board and provides a back button to return to the main menu.
 * Handles only UI rendering and layout, not game logic.
 */
public class ChessBoardScreen extends BaseScreen {
    private final ChessBoardActor boardActor;
    private final ChessThemeSetter themeSetter;
    private final Table boardContainer;
    private final Label statusLabel;
    
    // Current theme
    private String currentTheme = "flat1";
    
    /**
     * Creates a new ChessBoardScreen.
     * 
     * @param game The main game instance
     */
    public ChessBoardScreen(final Main game) {
        super(game);
        
        // Main layout structure
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(10);
        
        // Game status area at the top
        statusLabel = new Label("", skin);
        statusLabel.setAlignment(Align.center);
        statusLabel.setWrap(true);
        mainTable.add(statusLabel).expandX().fillX().pad(5).row();
        
        // Center content with board on the left and control panel on the right
        Table contentTable = new Table();
        
        // Board container (allows proper centering and sizing)
        boardContainer = new Table();
        boardContainer.setBackground(skin.getDrawable("window"));
        
        // Create the chess board actor
        boardActor = new ChessBoardActor();
        
        // Create the theme setter
        themeSetter = new ChessThemeSetter(boardActor);
        
        // Set the default theme
        themeSetter.setTheme(currentTheme);
        
        // Add the board to its container
        boardContainer.add(boardActor).expand().fill().pad(10);
        
        // Add board container to the content table
        contentTable.add(boardContainer).expand().fill().pad(5);
        
        // Control panel on the right
        Table controlPanel = createControlPanel();
        contentTable.add(controlPanel).width(200).fillY().pad(5);
        
        // Add content table to main layout
        mainTable.add(contentTable).expand().fill().row();
        
        // Add main table to stage
        stage.addActor(mainTable);
        
        // Set initial status message
        setStatusMessage("Chess Board Ready - Theme: " + currentTheme);
    }
    
    /**
     * Creates the control panel with buttons and info
     */
    private Table createControlPanel() {
        Table controlPanel = new Table();
        controlPanel.setBackground(skin.getDrawable("window"));
        controlPanel.pad(10);
        
        // Title for panel
        Label titleLabel = new Label("Board Controls", skin, "medium");
        controlPanel.add(titleLabel).expandX().center().padBottom(15).row();
        
        // Debug mode button
        TextButton debugButton = new TextButton("Toggle Debug", skin);
        debugButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boardActor.toggleDebugMode();
            }
        });
        controlPanel.add(debugButton).expandX().fillX().padBottom(10).row();
        
        // Theme selection buttons
        Label themeLabel = new Label("Board Themes:", skin);
        controlPanel.add(themeLabel).expandX().left().padTop(20).padBottom(5).row();
        
        // Flat themes
        addThemeButton(controlPanel, "Flat Theme 1", "flat1");
        addThemeButton(controlPanel, "Flat Theme 2", "flat2");
        addThemeButton(controlPanel, "Flat Theme 3", "flat3");
        addThemeButton(controlPanel, "Flat Theme 4", "flat4");
        addThemeButton(controlPanel, "Flat Theme 5", "flat5");
        
        // Perspective themes
        Label perspectiveLabel = new Label("Perspective Themes:", skin);
        controlPanel.add(perspectiveLabel).expandX().left().padTop(20).padBottom(5).row();
        
        addThemeButton(controlPanel, "Perspective 1", "perspective1");
        addThemeButton(controlPanel, "Perspective 2", "perspective2");
        
        // Back to menu button
        TextButton backButton = new TextButton("Back to Menu", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        controlPanel.add(backButton).expandX().fillX().padTop(30).row();
        
        return controlPanel;
    }
    
    /**
     * Adds a theme selection button to the control panel
     */
    private void addThemeButton(Table panel, String label, final String theme) {
        TextButton button = new TextButton(label, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentTheme = theme;
                themeSetter.setTheme(theme);
                setStatusMessage("Theme changed to: " + theme);
            }
        });
        panel.add(button).expandX().fillX().padBottom(5).row();
    }
    
    /**
     * Sets the status message displayed above the board
     */
    public void setStatusMessage(String message) {
        statusLabel.setText(message);
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        
        // Calculate the ideal board size based on the available space
        // This ensures the board stays centered and properly sized
        float boardSize = Math.min(
            stage.getWidth() * 0.7f,  // Max 70% of width
            stage.getHeight() * 0.8f   // Max 80% of height
        );
        
        // Update board container size to maintain aspect ratio
        boardContainer.getCell(boardActor).size(boardSize);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        boardActor.dispose();
        themeSetter.dispose();
    }
}