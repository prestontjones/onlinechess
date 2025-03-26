package io.github.onlinechess.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

import io.github.onlinechess.Main;

/**
 * Screen that displays the chess board and game UI
 */
public class ChessBoardScreen extends BaseScreen {
    // Board dimensions
    private final float BOARD_SIZE = 480f;
    private final float SQUARE_SIZE = BOARD_SIZE / 8f;
    
    // Game state
    private Board chessBoard;
    private boolean boardFlipped = false;
    private boolean isOnlineMode = false;
    
    // UI Components
    private Table boardTable;
    private Table chatPanel;
    
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
        
        // Create a fresh chess board (this would come from the server in practice)
        this.chessBoard = new Board();
        
        // Create UI
        createUI();
    }
    
    /**
     * Creates the screen UI
     */
    private void createUI() {
        // Create a main table that fills the screen
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(20);
        stage.addActor(mainTable);
        
        // Create status label at the top
        Label statusLabel = new Label("Chess Game", skin);
        statusLabel.setAlignment(Align.center);
        statusLabel.setWrap(true);
        mainTable.add(statusLabel).colspan(2).expandX().fillX().pad(10).row();
        
        // Create board table for the chess board
        boardTable = new Table();
        boardTable.setBackground(skin.getDrawable("window"));
        
        // Create chat panel if in online mode
        if (isOnlineMode) {
            chatPanel = new Table();
            chatPanel.setBackground(skin.getDrawable("window"));
            
            // Chat header
            Label chatLabel = new Label("Chat", skin, "medium");
            chatPanel.add(chatLabel).expandX().fillX().pad(10).row();
            
            // Placeholder for chat content
            Table chatContent = new Table();
            chatContent.setBackground(skin.getDrawable("white-rect"));
            chatPanel.add(chatContent).expand().fill().pad(10).row();
            
            // Chat input (placeholder)
            Table chatInput = new Table();
            chatInput.setBackground(skin.getDrawable("textfield"));
            chatPanel.add(chatInput).expandX().fillX().height(40).pad(10);
            
            // Add board and chat to the main table
            mainTable.add(boardTable).width(BOARD_SIZE).height(BOARD_SIZE).pad(10);
            mainTable.add(chatPanel).width(300).height(BOARD_SIZE).pad(10).fillY();
        } else {
            // Just the board for offline mode
            mainTable.add(boardTable).width(BOARD_SIZE).height(BOARD_SIZE).pad(10).expand();
        }
        
        // Add a flip board button
        TextButton flipBoardButton = new TextButton("Flip Board", skin);
        flipBoardButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boardFlipped = !boardFlipped;
            }
        });
        
        // Add back to menu button
        TextButton menuButton = new TextButton("Main Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        
        // Add buttons to a control panel
        Table controlPanel = new Table();
        controlPanel.defaults().pad(5).fillX();
        controlPanel.add(flipBoardButton).row();
        controlPanel.add(menuButton).row();
        
        // Add the control panel if not in online mode
        if (!isOnlineMode) {
            mainTable.add(controlPanel).width(200).top().pad(10);
        }
    }
    
    @Override
    public void render(float delta) {
        // Clear the screen (handled by BaseScreen)
        super.render(delta);
        
        // Draw the chess board and pieces using SpriteBatch
        SpriteBatch batch = game.getBatch();
        batch.begin();
        
        // Calculate board position (centered in the boardTable)
        float boardX = boardTable.getX() + (boardTable.getWidth() - BOARD_SIZE) / 2;
        float boardY = boardTable.getY() + (boardTable.getHeight() - BOARD_SIZE) / 2;
        
        // Draw the board
        batch.draw(boardTexture, boardX, boardY, BOARD_SIZE, BOARD_SIZE);
        
        // Draw the pieces
        for (Square square : Square.values()) {
            Piece piece = chessBoard.getPiece(square);
            if (piece != Piece.NONE) {
                // Calculate square position
                int file = square.getFile().ordinal();
                int rank = square.getRank().ordinal();
                
                // Flip coordinates if board is flipped
                if (boardFlipped) {
                    file = 7 - file;
                    rank = 7 - rank;
                }
                
                float squareX = boardX + file * SQUARE_SIZE;
                float squareY = boardY + rank * SQUARE_SIZE;
                
                // Get the piece texture
                String regionName = getPieceRegionName(piece);
                TextureAtlas.AtlasRegion region = pieceAtlas.findRegion(regionName);
                
                if (region != null) {
                    batch.draw(region, squareX, squareY, SQUARE_SIZE, SQUARE_SIZE);
                }
            }
        }
        
        batch.end();
    }
    
    /**
     * Gets the texture region name for a chess piece
     * 
     * @param piece The chess piece
     * @return The name of the texture region
     */
    private String getPieceRegionName(Piece piece) {
        switch (piece) {
            case WHITE_PAWN: return "W_Pawn";
            case WHITE_KNIGHT: return "W_Knight";
            case WHITE_BISHOP: return "W_Bishop";
            case WHITE_ROOK: return "W_Rook";
            case WHITE_QUEEN: return "W_Queen";
            case WHITE_KING: return "W_King";
            case BLACK_PAWN: return "B_Pawn";
            case BLACK_KNIGHT: return "B_Knight";
            case BLACK_BISHOP: return "B_Bishop";
            case BLACK_ROOK: return "B_Rook";
            case BLACK_QUEEN: return "B_Queen";
            case BLACK_KING: return "B_King";
            default: return null;
        }
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        // Assets are managed by the AssetManager, which is disposed elsewhere
    }
}