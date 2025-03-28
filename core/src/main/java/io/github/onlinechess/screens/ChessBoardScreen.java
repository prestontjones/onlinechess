package io.github.onlinechess.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.github.bhlangonijr.chesslib.Board;

import io.github.onlinechess.Main;
import io.github.onlinechess.ui.ChessBoardRenderer;
import io.github.onlinechess.ui.ChessPieceRenderer;

public class ChessBoardScreen extends BaseScreen {
    // Layout settings
    private final float BOARD_PADDING = 20f;
    private final float SIDE_PANEL_WIDTH = 280f;
    private final float PLAYER_INFO_HEIGHT = 90f;
    
    // Game state
    private Board chessBoard;
    private boolean boardFlipped = false;
    private boolean isOnlineMode = false;
    
    // UI Components
    private Table boardContainer;
    private Table leftPanel;
    private Table rightPanel;
    private Table topPlayerArea;
    private Table bottomPlayerArea;
    private Table moveHistoryArea;
    private Table chatArea;
    
    // Chess renderers
    private ChessBoardRenderer boardRenderer;
    private ChessPieceRenderer pieceRenderer;
    
    // Resources
    private Texture boardTexture;
    private TextureAtlas pieceAtlas;
    
    /**
     * Creates a new chess board screen
     * 
     * @param game The game instance
     * @param isOnlineMode Whether the game is in online mode
     */
    public ChessBoardScreen(Main game, boolean isOnlineMode) {
        super(game);
        this.isOnlineMode = isOnlineMode;
        
        // Create a fresh chess board
        this.chessBoard = new Board();
        
        // Load resources
        loadResources();
        
        // Create UI
        createUI();
        
        // Initialize renderers
        initializeRenderers();
    }
    
    /**
     * Loads required resources from the asset manager
     */
    private void loadResources() {
        // Load the board texture directly
        boardTexture = new Texture("chess boards/board_plain_01.png");
        
        // Load the piece atlas directly
        pieceAtlas = new TextureAtlas("chess pieces/chess pieces.atlas");
    }
    
    /**
     * Creates the screen UI with modern chess.com-like styling
     */
    private void createUI() {
        // Create a main table that fills the screen
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        
        // Create left panel for player info and controls
        createLeftPanel();
        
        // Create the board container
        boardContainer = new Table();
        boardContainer.setBackground(skin.getDrawable("white-rect"));
        
        // Create right panel for move history and chat
        createRightPanel();
        
        // Add all components to the main table with proper layout
        mainTable.add(leftPanel).width(SIDE_PANEL_WIDTH).fillY();
        mainTable.add(boardContainer).expand().fill();
        mainTable.add(rightPanel).width(SIDE_PANEL_WIDTH).fillY();
    }
    
    /**
     * Creates the left panel with player info and game controls
     */
    private void createLeftPanel() {
        leftPanel = new Table();
        leftPanel.setBackground(skin.getDrawable("white-rect"));
        leftPanel.top().padTop(10).padLeft(10).padRight(10);
        
        // Top player area (opponent)
        topPlayerArea = new Table();
        topPlayerArea.setBackground(skin.getDrawable("white-rect"));
        
        // Placeholder for opponent avatar
        Image opponentAvatar = new Image(skin.getDrawable("radio"));
        topPlayerArea.add(opponentAvatar).size(50).pad(5);
        
        // Placeholder for opponent info
        Table opponentInfo = new Table();
        Label opponentName = new Label("Opponent", skin);
        Label opponentTimer = new Label("10:00", skin, "medium");
        opponentInfo.add(opponentName).left().row();
        opponentInfo.add(opponentTimer).left();
        topPlayerArea.add(opponentInfo).expandX().left().pad(5);
        
        leftPanel.add(topPlayerArea).expandX().fillX().height(PLAYER_INFO_HEIGHT).row();
        
        // Game controls area
        Table controlsArea = new Table();
        controlsArea.top().padTop(15);
        
        // Placeholder for game control buttons (will be replaced with custom components)
        Table gameControlsPlaceholder = new Table();
        gameControlsPlaceholder.setBackground(skin.getDrawable("white-rect"));
        gameControlsPlaceholder.add(new Label("Game Controls", skin)).pad(20);
        controlsArea.add(gameControlsPlaceholder).expandX().fillX().height(150).row();
        
        // Placeholder for additional controls
        TextButton flipBoardButton = new TextButton("Flip Board", skin);
        flipBoardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                flipBoard();
            }
        });
        controlsArea.add(flipBoardButton).expandX().fillX().padTop(15).row();
        
        TextButton menuButton = new TextButton("Main Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        controlsArea.add(menuButton).expandX().fillX().padTop(10).row();
        
        leftPanel.add(controlsArea).expandX().fillX().row();
        
        // Move history section (moved from right panel)
        Label moveHistoryLabel = new Label("Move History", skin);
        moveHistoryLabel.setAlignment(Align.center);
        leftPanel.add(moveHistoryLabel).expandX().fillX().padTop(20).row();
        
        moveHistoryArea = new Table();
        moveHistoryArea.setBackground(skin.getDrawable("white-rect"));
        // Empty container for move history - will be filled by a separate class
        
        leftPanel.add(moveHistoryArea).expandX().fillX().expand().row();
        
        // Bottom player area (you)
        bottomPlayerArea = new Table();
        bottomPlayerArea.setBackground(skin.getDrawable("white-rect"));
        
        // Placeholder for your avatar
        Image yourAvatar = new Image(skin.getDrawable("radio"));
        bottomPlayerArea.add(yourAvatar).size(50).pad(5);
        
        // Placeholder for your info
        Table yourInfo = new Table();
        Label yourName = new Label("You", skin);
        Label yourTimer = new Label("10:00", skin, "medium");
        yourInfo.add(yourName).left().row();
        yourInfo.add(yourTimer).left();
        bottomPlayerArea.add(yourInfo).expandX().left().pad(5);
        
        leftPanel.add(bottomPlayerArea).expandX().fillX().height(PLAYER_INFO_HEIGHT).padBottom(10);
    }
    
    /**
     * Creates the right panel with move history and chat
     */
    private void createRightPanel() {
        rightPanel = new Table();
        rightPanel.setBackground(skin.getDrawable("white-rect"));
        rightPanel.top().padTop(10).padRight(10).padLeft(10);
        
        // Chat section (only if in online mode)
        if (isOnlineMode) {
            Label chatLabel = new Label("Chat", skin);
            chatLabel.setAlignment(Align.center);
            rightPanel.add(chatLabel).expandX().fillX().row();
            
            chatArea = new Table();
            chatArea.setBackground(skin.getDrawable("white-rect"));
            // Empty container for chat - will be filled by a separate class
            
            rightPanel.add(chatArea).expand().fill();
        } else {
            // If not in online mode, just add an empty placeholder
            Label offlineLabel = new Label("Chat not available in offline mode", skin);
            offlineLabel.setAlignment(Align.center);
            rightPanel.add(offlineLabel).expand().fill();
        }
    }
    
    /**
     * Initializes chess renderers
     */
    private void initializeRenderers() {
        // Create board renderer
        boardRenderer = new ChessBoardRenderer();
        
        // Calculate the boardContainer's dimensions
        float boardContainerWidth = boardContainer.getWidth();
        float boardContainerHeight = boardContainer.getHeight();
        
        // Calculate appropriate board size to fit in the container
        float boardSize = Math.min(boardContainerWidth, boardContainerHeight) - (BOARD_PADDING * 2);
        
        // Position the board renderer centered in the boardContainer
        float boardX = (boardContainerWidth - boardSize) / 2;
        float boardY = (boardContainerHeight - boardSize) / 2;
        
        boardRenderer.setBounds(boardX, boardY, boardSize, boardSize);
        
        // Create piece renderer
        pieceRenderer = new ChessPieceRenderer(boardRenderer, chessBoard, pieceAtlas);
        
        // Add renderers to the boardContainer
        boardContainer.addActor(boardRenderer);
        boardContainer.addActor(pieceRenderer);
    }
    
    
    @Override
    public void render(float delta) {
        // Clear the screen (handled by BaseScreen)
        super.render(delta);
        
        // Check if we need to update board size (continuous resize)
        updateBoardSize();
        
        // The actual rendering is handled by the ChessBoardRenderer and ChessPieceRenderer
        // They are automatically rendered as part of the stage's actors
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        
        // Let the stage handle the viewport update
        stage.getViewport().update(width, height, true);
        
        // Update the chess board size and position
        updateBoardSize();
        
        // Log for debugging
        // Gdx.app.log("ChessBoardScreen", "Resized to " + width + "x" + height);
    }
    
    /**
     * Flips the board orientation
     * This method can be called from outside to flip the board
     */
    public void flipBoard() {
        boardFlipped = !boardFlipped;
        if (boardRenderer != null) {
            boardRenderer.flipBoard();
        }
        if (pieceRenderer != null) {
            pieceRenderer.updateBounds();
        }
        
        // Update the board coordinates to reflect the flip
        updateBoardSize();
    }

    /**
     * Updates the chess board size and position based on current screen dimensions
     * Called during resize and continuously during render to ensure proper scaling
     */
    private void updateBoardSize() {
        if (boardRenderer != null && pieceRenderer != null && boardContainer != null) {
            // Remove old coordinate labels
            for (Actor actor : new Array<>(boardContainer.getChildren())) {
                if (actor instanceof Label) {
                    actor.remove();
                }
            }
            
            // Calculate the boardContainer's dimensions
            float boardContainerWidth = boardContainer.getWidth();
            float boardContainerHeight = boardContainer.getHeight();
            
            // Safety check for valid dimensions
            if (boardContainerWidth <= 0 || boardContainerHeight <= 0) return;
            
            // Calculate appropriate board size to fit in the container
            float boardSize = Math.min(boardContainerWidth, boardContainerHeight) - (BOARD_PADDING * 2);
            
            // Position the board renderer centered in the boardContainer
            float boardX = (boardContainerWidth - boardSize) / 2;
            float boardY = (boardContainerHeight - boardSize) / 2;
            
            boardRenderer.setBounds(boardX, boardY, boardSize, boardSize);
            pieceRenderer.updateBounds();
        }
    }
}