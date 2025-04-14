package io.github.onlinechess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.github.bhlangonijr.chesslib.Side;

import io.github.onlinechess.Main;
import io.github.onlinechess.ui.ChessBoardActor;
import io.github.onlinechess.ui.ChessThemeSetter;
import io.github.onlinechess.ui.dialogs.ConfirmDialog;
import io.github.onlinechess.utils.BoardManager;
import io.github.onlinechess.utils.ChessBoard;

/**
 * Screen that renders a chess board and provides controls for playing chess.
 */
public class ChessBoardScreen extends BaseScreen {
    private final ChessBoardActor boardActor;
    private final ChessThemeSetter themeSetter;
    private final Table boardContainer;
    private final Label gameStateLabel;
    private final TextField chatInputField;
    private final Table chatMessagesTable;
    private final ChessBoard chessBoard;
    private final BoardManager boardManager;
    private final Group pieceContainer;
    private Window confirmDialog;
    
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
        
        // Main layout structure - using a horizontal layout
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(10);
        
        // Create three columns: controls, board area, chat area
        Table controlPanel = createControlPanel();
        mainTable.add(controlPanel).width(180).fillY().padRight(10);
        
        // Center content containing board and player info
        Table centerContent = new Table();
        
        // Top player info (Black)
        Table blackPlayerInfo = createPlayerInfoPanel("Black Player", Side.BLACK);
        centerContent.add(blackPlayerInfo).fillX().padBottom(5).row();
        
        // Board container (allows proper centering and sizing)
        boardContainer = new Table();
        //boardContainer.setBackground(skin.getDrawable("window"));
        
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
        boardContainer.add(boardActor).expand().fill();
        boardContainer.addActor(pieceContainer); // Add pieces on top of the board
        
        // Add board container to the center content
        centerContent.add(boardContainer).expand().fill().row();
        
        // Bottom player info (White)
        Table whitePlayerInfo = createPlayerInfoPanel("White Player", Side.WHITE);
        centerContent.add(whitePlayerInfo).fillX().padTop(5);
        
        // Add center content to main table
        mainTable.add(centerContent).expand().fill();
        
        // Chat and game state area on the right
        Table rightPanel = new Table();
        rightPanel.setBackground(skin.getDrawable("window"));
        rightPanel.pad(10);
        
        // Game state information display
        gameStateLabel = new Label("White's turn to move", skin);
        gameStateLabel.setWrap(true);
        rightPanel.add(gameStateLabel).fillX().expandX().pad(5).row();
        
        // Divider
        Table divider = new Table();
        divider.setBackground(skin.getDrawable("white"));
        rightPanel.add(divider).fillX().height(2).padTop(5).padBottom(10).row();
        
        // Chat area title
        Label chatTitle = new Label("Chat", skin, "medium");
        rightPanel.add(chatTitle).left().padBottom(5).row();
        
        // Chat messages area with scroll pane
        chatMessagesTable = new Table();
        chatMessagesTable.top().left();
        ScrollPane chatScrollPane = new ScrollPane(chatMessagesTable, skin);
        chatScrollPane.setFadeScrollBars(false);
        chatScrollPane.setScrollbarsOnTop(true);
        rightPanel.add(chatScrollPane).expand().fill().row();
        
        // Chat input field and send button
        Table chatInputArea = new Table();
        chatInputField = new TextField("", skin);
        chatInputArea.add(chatInputField).expandX().fillX().padRight(5);
        
        TextButton sendButton = new TextButton("Send", skin);
        sendButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sendChatMessage();
            }
        });
        chatInputArea.add(sendButton).width(60);
        
        rightPanel.add(chatInputArea).fillX().padTop(5);
        
        // Add right panel to main layout
        mainTable.add(rightPanel).width(250).fillY().padLeft(10);
        
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
     * Creates a player info panel
     */
    private Table createPlayerInfoPanel(String playerName, Side side) {
        Table playerTable = new Table();
        playerTable.setBackground(skin.getDrawable("window"));
        playerTable.pad(5);
        
        // Player name and side
        Label nameLabel = new Label(playerName, skin);
        playerTable.add(nameLabel).left().expandX();
        
        // Add clock or status indicators if needed
        Label timeLabel = new Label("10:00", skin);
        playerTable.add(timeLabel).right();
        
        return playerTable;
    }
    
    /**
     * Creates the control panel with buttons and info
     */
    private Table createControlPanel() {
        Table controlPanel = new Table();
        controlPanel.setBackground(skin.getDrawable("window"));
        controlPanel.pad(10);
        
        // Title for panel
        Label titleLabel = new Label("Controls", skin, "medium");
        controlPanel.add(titleLabel).expandX().center().padBottom(15).row();
        
        // Main control buttons
        TextButton backButton = new TextButton("Main Menu", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showExitConfirmation();
            }
        });
        controlPanel.add(backButton).expandX().fillX().padBottom(15).row();
        
        // Game controls section
        Label gameControlsLabel = new Label("Game Controls", skin);
        gameControlsLabel.setAlignment(Align.left);
        controlPanel.add(gameControlsLabel).expandX().fillX().padBottom(5).row();
        
        TextButton resignButton = new TextButton("Resign", skin);
        controlPanel.add(resignButton).expandX().fillX().padBottom(5).row();
        
        TextButton offerDrawButton = new TextButton("Offer Draw", skin);
        controlPanel.add(offerDrawButton).expandX().fillX().padBottom(15).row();
        
        // Visual controls section
        Label visualLabel = new Label("Visual Settings", skin);
        visualLabel.setAlignment(Align.left);
        controlPanel.add(visualLabel).expandX().fillX().padBottom(5).row();
        
        // Theme selection - simplified to a dropdown in the future
        TextButton themeButton = new TextButton("Change Theme", skin);
        themeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Cycle through themes
                cycleTheme();
            }
        });
        controlPanel.add(themeButton).expandX().fillX().padBottom(5).row();
        
        TextButton flipBoardButton = new TextButton("Flip Board", skin);
        controlPanel.add(flipBoardButton).expandX().fillX().padBottom(15).row();
        
        // Debug tools - could be hidden in production
        Label debugLabel = new Label("Debug Tools", skin);
        debugLabel.setAlignment(Align.left);
        controlPanel.add(debugLabel).expandX().fillX().padBottom(5).row();
        
        TextButton debugButton = new TextButton("Toggle Debug", skin);
        debugButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boardActor.toggleDebugMode();
            }
        });
        controlPanel.add(debugButton).expandX().fillX().padBottom(5).row();
        
        // Add spacer at bottom to push content to top
        controlPanel.add().expand().fill().row();
        
        // Game version at bottom
        Label versionLabel = new Label("Chess v1.0", skin);
        versionLabel.setAlignment(Align.center);
        controlPanel.add(versionLabel).expandX().fillX().padTop(15);
        
        return controlPanel;
    }
    
    /**
     * Cycles through available themes
     */
    private void cycleTheme() {
        String[] themes = {"flat1", "flat2", "flat3", "flat4", "flat5", "perspective1", "perspective2"};
        int currentIndex = 0;
        
        // Find current theme index
        for (int i = 0; i < themes.length; i++) {
            if (themes[i].equals(currentTheme)) {
                currentIndex = i;
                break;
            }
        }
        
        // Move to next theme (with wrapping)
        currentIndex = (currentIndex + 1) % themes.length;
        currentTheme = themes[currentIndex];
        
        // Apply the new theme
        themeSetter.setTheme(currentTheme);
        setStatusMessage("Theme changed to: " + currentTheme);
        boardManager.refreshPieceTextures();
    }
    
    /**
     * Shows a confirmation dialog when exiting to main menu
     */
    private void showExitConfirmation() {
        // Remove any existing dialog
        if (confirmDialog != null) {
            confirmDialog.remove();
        }
        
        // Create a new confirmation dialog
        confirmDialog = ConfirmDialog.create(
            "Return to Main Menu?",
            "Are you sure you want to exit the game? Any unsaved progress will be lost.",
            new ConfirmDialog.ConfirmListener() {
                @Override
                public void onConfirm() {
                    game.setScreen(new MainMenuScreen(game));
                }
            },
            stage,
            skin
        );
    }
    
    /**
     * Handles sending a chat message
     */
    private void sendChatMessage() {
        String message = chatInputField.getText().trim();
        if (!message.isEmpty()) {
            // Add the message to the chat area
            addChatMessage("You", message);
            
            // Clear the input field
            chatInputField.setText("");
            chatInputField.setCursorPosition(0);
        }
    }
    
    /**
     * Adds a message to the chat area
     */
    private void addChatMessage(String sender, String message) {
        // Create message entry
        Table messageEntry = new Table();
        messageEntry.left();
        
        // Sender name in bold
        Label senderLabel = new Label(sender + ": ", skin);
        messageEntry.add(senderLabel).left();
        
        // Message content
        Label messageLabel = new Label(message, skin);
        messageLabel.setWrap(true);
        messageEntry.add(messageLabel).left().expandX().fillX();
        
        // Add to messages table
        chatMessagesTable.add(messageEntry).left().expandX().fillX().padBottom(2).row();
        
        // Scroll to bottom
        chatMessagesTable.invalidate();
    }
    
    /**
     * Sets the status message displayed in the game state area
     */
    public void setStatusMessage(String message) {
        gameStateLabel.setText(message);
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
            stage.getWidth() * 0.6f,  // Max 60% of width
            stage.getHeight() * 0.7f   // Max 70% of height
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
        
        // Handle Enter key in chat
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && stage.getKeyboardFocus() == chatInputField) {
            sendChatMessage();
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