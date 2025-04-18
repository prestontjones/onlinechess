package io.github.onlinechess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import io.github.onlinechess.Main;
import io.github.onlinechess.ui.ChessBoardActor;
import io.github.onlinechess.ui.ChessThemeSetter;
import io.github.onlinechess.utils.BoardManager;
import io.github.onlinechess.utils.ChessBoard;

/**
 * Screen that renders a chess board and provides controls for playing chess.
 */
public class ChessBoardScreen extends BaseScreen {
    private final ChessBoardActor boardActor;
    private final ChessThemeSetter themeSetter;
    private final Table boardContainer;
    private final Label statusLabel;
    private final ChessBoard chessBoard;
    private final BoardManager boardManager;
    private final Group pieceContainer;
    
    // Current theme
    private String currentTheme = "flat1";
    
    // Track fullscreen state
    private boolean wasFullscreen = false;
    
    /**
     * Creates a new ChessBoardScreen.
     * 
     * @param game The main game instance
     * @param isOnline Whether this is an online game
     */
    public ChessBoardScreen(final Main game, boolean isOnline) {
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
        
        // Create the board manager
        boardActor = new ChessBoardActor();
        pieceContainer = new Group();
        themeSetter = new ChessThemeSetter(boardActor);
        themeSetter.setTheme(currentTheme);
        chessBoard = new ChessBoard(isOnline);
        boardManager = new BoardManager(
            pieceContainer,
            boardActor,
            themeSetter,
            chessBoard
        );
        
        // Add the board to its container
        boardContainer.add(boardActor).expand().fill().pad(10);
        boardContainer.addActor(pieceContainer); // Add pieces on top of the board
        
        // Add board container to the content table
        contentTable.add(boardContainer).expand().fill().pad(5);
        
        // Control panel on the right
        Table controlPanel = createControlPanel();
        contentTable.add(controlPanel).width(200).fillY().pad(5);
        
        // Add content table to main layout
        mainTable.add(contentTable).expand().fill().row();
        
        // Add main table to stage
        stage.addActor(mainTable);
        
        // Initialize the board
        boardManager.initializeStandardBoard();
        
        // Immediately update board size to ensure pieces are correctly positioned
        updateBoardSize();
        
        // Set initial status message
        updateStatusMessage();
        
        // Track current fullscreen state
        wasFullscreen = Gdx.graphics.isFullscreen();
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
        
        // Game status info
        Label gameStatusLabel = new Label("Game Status:", skin);
        controlPanel.add(gameStatusLabel).expandX().left().padTop(5).padBottom(5).row();
        
        Label currentTurnLabel = new Label("Current Turn: White", skin);
        controlPanel.add(currentTurnLabel).expandX().left().padBottom(15).row();
        
        // Debug mode button
        TextButton debugButton = new TextButton("Toggle Debug", skin);
        debugButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boardActor.toggleDebugMode();
            }
        });
        controlPanel.add(debugButton).expandX().fillX().padBottom(10).row();
        
        // Update pieces button
        TextButton updateButton = new TextButton("Update Pieces", skin);
        updateButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boardManager.updateAllPiecePositions();
            }
        });
        controlPanel.add(updateButton).expandX().fillX().padBottom(20).row();
        
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
                boardManager.refreshPieceTextures();
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
    
    /**
     * Updates the status message with the current game state
     */
    private void updateStatusMessage() {
        String gameState = boardManager.getGameStateMessage();
        setStatusMessage(gameState);
    }
    
    /**
     * Calculates and applies the ideal board size based on screen dimensions
     */
    private void updateBoardSize() {
        // Calculate the ideal board size based on the available space
        float boardSize = Math.min(
            stage.getWidth() * 0.7f,  // Max 70% of width
            stage.getHeight() * 0.8f   // Max 80% of height
        );
        
        // Update board container size to maintain aspect ratio
        boardContainer.getCell(boardActor).size(boardSize);
        
        // Now tell the board manager to resize the board and update pieces
        boardManager.updateBoardSize(boardSize);
    }
    
    @Override
    public void render(float delta) {
        super.render(delta);
        
        // Check for fullscreen toggle
        boolean isFullscreen = Gdx.graphics.isFullscreen();
        if (wasFullscreen != isFullscreen) {
            wasFullscreen = isFullscreen;
            // Force an update after fullscreen toggle
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    updateBoardSize();
                }
            });
        }
        
        // Handle F11 key for fullscreen toggle
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1280, 720);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
        
        // Update the status message to reflect current game state
        updateStatusMessage();
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        
        // Update board size and piece positions
        updateBoardSize();
    }
    
    @Override
    public void show() {
        super.show();
        
        // Ensure pieces are correctly positioned when the screen is shown
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                updateBoardSize();
            }
        });
    }
    
    @Override
    public void dispose() {
        super.dispose();
        boardActor.dispose();
        themeSetter.dispose();
    }
}