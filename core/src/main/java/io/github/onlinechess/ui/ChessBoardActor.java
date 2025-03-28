package io.github.onlinechess.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Disposable;
import com.github.bhlangonijr.chesslib.Square;

/**
 * ChessBoardRenderer handles rendering a chess board texture and mapping
 * between screen coordinates and chess squares.
 */
public class ChessBoardRenderer extends Actor implements Disposable {
    // Board states
    private Texture boardTexture;
    private boolean flipped = false;
    private int boardWidth, boardHeight;
    private float boardScale = 1.0f;
    private float minSize = 320f; // Minimum board size in pixels
    private boolean debugMode = false;
    
    // Square mapping
    private Map<Square, Rectangle> squareRectangles = new HashMap<>();
    
    // Board style - could be expanded to support multiple styles
    private String boardTexturePath = "chess boards/board_plain_01.png";
    
    // Border constant - adjust based on your pixel art
    private final int BORDER_SIZE = 7;
    
    // Creates a new chess board renderer.
    public ChessBoardRenderer() {
        loadBoardTexture();
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // This could handle square selection when clicked
                Square square = screenToSquare(x, y);
                if (square != null) {
                    Gdx.app.log("ChessBoard", "Square clicked: " + square.name());
                    return true;
                }
                return false;
            }
        });
    }

    // Loads the board texture and sets initial dimensions.
    private void loadBoardTexture() {
        boardTexture = new Texture(Gdx.files.internal(boardTexturePath));
        boardWidth = boardTexture.getWidth();
        boardHeight = boardTexture.getHeight();
        setSize(boardWidth, boardHeight);
        calculateSquarePositions();
    }
    

    //Calculates the positions of all 64 squares on the board.
    private void calculateSquarePositions() {
        squareRectangles.clear();
        
        // Calculate the size of a single square
        float effectiveBoardSize = getWidth() - (2 * BORDER_SIZE * boardScale);
        float squareSize = effectiveBoardSize / 8f;
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Calculate the corresponding Square enum value
                // The Square enum in ChessLib is ordered A1, B1, ..., H8 (from white's perspective)
                int squareIndex = (flipped ? 7 - row : row) * 8 + (flipped ? 7 - col : col);
                Square square = Square.values()[squareIndex];
                
                // Calculate square position within the board
                float x = BORDER_SIZE * boardScale + col * squareSize;
                float y = BORDER_SIZE * boardScale + (7 - row) * squareSize;
                
                // Store the rectangle representing this square
                squareRectangles.put(square, new Rectangle(x, y, squareSize, squareSize));
            }
        }
    }
    
    /**
     * Converts screen coordinates to a chess square.
     * 
     * @param screenX X coordinate relative to the board
     * @param screenY Y coordinate relative to the board
     * @return The Square at the given coordinates, or null if outside the board
     */
    public Square screenToSquare(float screenX, float screenY) {
        for (Map.Entry<Square, Rectangle> entry : squareRectangles.entrySet()) {
            if (entry.getValue().contains(screenX, screenY)) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    /**
     * Gets the screen position of a chess square.
     * 
     * @param square The chess square
     * @return Vector2 with the screen coordinates of the square's top-left corner
     */
    public Vector2 squareToScreen(Square square) {
        Rectangle rect = squareRectangles.get(square);
        if (rect != null) {
            return new Vector2(rect.x, rect.y);
        }
        return null;
    }
    
    /**
     * Gets the rectangle representing a chess square.
     *
     * @param square The chess square
     * @return Rectangle representing the square's bounds
     */
    public Rectangle getSquareRectangle(Square square) {
        return squareRectangles.get(square);
    }
    
    public void flipBoard() {
        flipped = !flipped;
        calculateSquarePositions();
    }
    
    public boolean isFlipped() {
        return flipped;
    }
    
    /**
     * Sets a new board texture.
     * 
     * @param texturePath Path to the board texture
     */
    public void setBoardTexture(String texturePath) {
        if (boardTexture != null) {
            boardTexture.dispose();
        }
        
        boardTexturePath = texturePath;
        loadBoardTexture();
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1, 1, 1, parentAlpha);
        
        // Draw the board texture
        batch.draw(
            boardTexture, 
            getX(), getY(),
            getWidth(), getHeight()
        );

        if (debugMode) {
            drawDebugSquares(batch);
        }
    }
    
    @Override
    public void setSize(float width, float height) {
        // Ensure board remains a square and respects minimum size
        float size = Math.max(Math.min(width, height), minSize);
        super.setSize(size, size);
        
        // Calculate the scale factor
        boardScale = size / boardWidth;
        
        // Recalculate square positions with the new size
        calculateSquarePositions();
    }
    
    @Override
    public void setBounds(float x, float y, float width, float height) {
        // Ensure board remains a square and respects minimum size
        float size = Math.max(Math.min(width, height), minSize);
        super.setBounds(x, y, size, size);
        
        // Calculate the scale factor
        boardScale = size / boardWidth;
        
        // Recalculate square positions with the new size
        calculateSquarePositions();
    }

    public void toggleDebugMode() {
        debugMode = !debugMode;
    }

    private void drawDebugSquares(Batch batch) {
        
        // Simple 1-pixel white texture for drawing lines
        Texture debugTexture = new Texture(Gdx.files.internal("raw os8ui/white.png"));
        
        try {
            // Draw each square outline
            for (Map.Entry<Square, Rectangle> entry : squareRectangles.entrySet()) {
                Rectangle rect = entry.getValue();
                Square square = entry.getKey();
                
                // Different colors for ranks and files for better visualization
                // Files (A-H) alternate between red and blue
                // Ranks (1-8) have increasing green component
                
                int file = square.getFile().ordinal();
                int rank = square.getRank().ordinal();
                
                // Set color based on square position
                if ((file + rank) % 2 == 0) {
                    batch.setColor(1, 0, 0, 0.5f); // Red for light squares
                } else {
                    batch.setColor(0, 0, 1, 0.5f); // Blue for dark squares
                }
                
                float lineWidth = 2f;
                
                // Draw rectangle outline
                float x = getX() + rect.x;
                float y = getY() + rect.y;
                
                // Top line
                batch.draw(debugTexture, x, y + rect.height - lineWidth, rect.width, lineWidth);
                // Bottom line
                batch.draw(debugTexture, x, y, rect.width, lineWidth);
                // Left line
                batch.draw(debugTexture, x, y, lineWidth, rect.height);
                // Right line
                batch.draw(debugTexture, x + rect.width - lineWidth, y, lineWidth, rect.height);
            }
        } finally {
            // No need to dispose debugTexture here as it could be reused
            // In a real implementation, you might want to create and dispose this texture once
        }
    }
    
    @Override
    public void dispose() {
        if (boardTexture != null) {
            boardTexture.dispose();
        }
    }
}