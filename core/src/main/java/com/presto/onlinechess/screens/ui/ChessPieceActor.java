package com.presto.onlinechess.screens.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.presto.onlinechess.utils.ChessPlayer;



/**
 * Renders a chess piece and handles its movement
 */
public class ChessPieceActor extends Actor {
    private BoardManager boardManager;
    private ChessBoardActor chessBoardActor;
    private ChessThemeSetter themeSetter;
    private TextureRegion pieceTexture;
    private Square currentSquare; // Logical square where this piece is located
    private Piece chessPiece; // The actual chess piece type
    private ChessPlayer player;
    private boolean isDragging = false;
    private float originalX, originalY; // Original position before drag

    /**
     * Creates a new chess piece actor.
     * 
     * @param boardActor The board actor to draw pieces on
     * @param themeSetter The theme setter for obtaining piece textures
     * @param piece The chess piece type
     * @param startSquare The starting square for this piece
     * @param player The player who controls this piece
     */
    public ChessPieceActor(ChessBoardActor boardActor, ChessThemeSetter themeSetter, 
                          Piece piece, Square startSquare, ChessPlayer player, BoardManager boardManager) {
        this.chessBoardActor = boardActor;
        this.themeSetter = themeSetter;
        this.chessPiece = piece;
        this.currentSquare = startSquare;
        this.player = player;
        this.boardManager = boardManager;
        
        // Get the appropriate texture for this piece
        this.pieceTexture = themeSetter.getPieceTextureRegion(piece);
        
        // Add an input listener to handle click/drag events
        addListener(new InputListener() {
            private float offsetX, offsetY;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // Only allow dragging if it's this player's turn and piece
                if (player.getSide() != chessPiece.getPieceSide()) {
                    return false;
                }
                
                // Capture the touch offset relative to the actor for smooth dragging
                offsetX = x;
                offsetY = y;
                
                // Store original position for snapping back if needed
                originalX = getX();
                originalY = getY();
                
                isDragging = true;
                
                // Bring this piece to the front
                toFront();
                
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                // Update the actor's position so it follows the drag
                float newX = getX() + x - offsetX;
                float newY = getY() + y - offsetY;
                setPosition(newX, newY);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDragging = false;
                
                // Get absolute screen coordinates
                float screenX = getX() + x;
                float screenY = getY() + y;
                
                // Find the destination square
                Square destinationSquare = chessBoardActor.screenToSquare(screenX, screenY);
                
                // If the board manager is set, request the move
                if (boardManager != null && destinationSquare != null && currentSquare != destinationSquare) {
                    Move move = new Move(currentSquare, destinationSquare);
                    boolean moveMade = boardManager.attemptMove(player, move);
                    if (!moveMade) {
                        // If move is not valid, snap back to original position
                        snapToSquare(currentSquare);
                    }
                } else {
                    // If no board manager or invalid square, just snap back
                    snapToSquare(currentSquare);
                }
            }
        });
        
        // Initialize position
        snapToSquare(startSquare);
    }
    
    /**
     * Snaps this piece to a specific square on the board
     */
    public void snapToSquare(Square square) {
        if (chessBoardActor != null && square != null) {
            // Update the current square
            currentSquare = square;
            
            // Get the rectangle for the square
            Rectangle squareRect = chessBoardActor.getSquareRectangle(square);
            if (squareRect != null) {
                // Calculate the position relative to the board
                float pieceX = chessBoardActor.getX() + squareRect.x;
                float pieceY = chessBoardActor.getY() + squareRect.y;
                
                // Position the piece
                setPosition(pieceX, pieceY);
                
                // Adjust the piece size to fit the square
                setSize(squareRect.width, squareRect.height);
            }
        }
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (pieceTexture == null) return;
        
        batch.setColor(1, 1, 1, parentAlpha);
        
        // Calculate the height based on whether this is a perspective view
        float drawHeight = themeSetter.isPerspective() ? getHeight() * 2 : getHeight();
        
        // For perspective pieces, draw taller and adjust position
        float yOffset = themeSetter.isPerspective() ? getHeight() : 0;
        
        // Draw the piece texture
        batch.draw(
            pieceTexture,
            getX(), getY() - yOffset,
            getWidth(), drawHeight
        );
    }
    
    /**
     * Updates the piece texture based on the current theme
     */
    public void updateTexture() {
        this.pieceTexture = themeSetter.getPieceTextureRegion(chessPiece);
    }
    
    /**
     * Get the current square this piece is on
     */
    public Square getCurrentSquare() {
        return currentSquare;
    }
    
    /**
     * Set the current square this piece is on
     */
    public void setCurrentSquare(Square square) {
        this.currentSquare = square;
        snapToSquare(square);
    }
    
    /**
     * Get the chess piece
     */
    public Piece getChessPiece() {
        return chessPiece;
    }
    
    /**
     * Get the side (color) of this piece
     */
    public Side getPieceSide() {
        return chessPiece.getPieceSide();
    }

    public Piece getPiece() {
        return chessPiece;
    }
    
    /**
     * Get the player who controls this piece
     */
    public ChessPlayer getPlayer() {
        return player;
    }
}
