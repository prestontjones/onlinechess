package io.github.onlinechess.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

/**
 * Renders chess pieces on top of a ChessBoardRenderer
 */
public class ChessPieceRenderer extends Actor implements Disposable {
    private final ChessBoardRenderer boardRenderer;
    private final Board chessBoard;
    private final TextureAtlas pieceAtlas;
    
    /**
     * Creates a new chess piece renderer.
     * 
     * @param boardRenderer The board renderer to draw pieces on
     * @param chessBoard The chess board model with piece positions
     * @param pieceAtlas TextureAtlas containing piece textures
     */
    public ChessPieceRenderer(ChessBoardRenderer boardRenderer, Board chessBoard, TextureAtlas pieceAtlas) {
        this.boardRenderer = boardRenderer;
        this.chessBoard = chessBoard;
        this.pieceAtlas = pieceAtlas;
        
        // Match the size and position of the board
        setBounds(
            boardRenderer.getX(),
            boardRenderer.getY(),
            boardRenderer.getWidth(),
            boardRenderer.getHeight()
        );
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Draw each piece on the board
        for (Square square : Square.values()) {
            Piece piece = chessBoard.getPiece(square);
            if (piece != Piece.NONE) {
                // Get the texture for this piece
                TextureRegion pieceTexture = getPieceTexture(piece);
                if (pieceTexture != null) {
                    // Get the screen coordinates for this square
                    Rectangle rect = boardRenderer.getSquareRectangle(square);
                    if (rect != null) {
                        // Get texture's aspect ratio (16x32 -> 1:2)
                        float textureAspect = (float) pieceTexture.getRegionWidth() / pieceTexture.getRegionHeight(); // 16 / 32 = 0.5

                        // Fit within the square while maintaining aspect ratio
                        float targetWidth = rect.width;
                        float targetHeight = targetWidth / textureAspect;

                        if (targetHeight > rect.height) {
                            // If height exceeds square, scale based on height
                            targetHeight = rect.height;
                            targetWidth = targetHeight * textureAspect;
                        }

                        // Center the piece in the square
                        float pieceX = getX() + rect.x + (rect.width - targetWidth) / 2;
                        float pieceY = getY() + rect.y + (rect.height - targetHeight) / 2;

                        // Draw the piece with correct aspect ratio
                        batch.draw(
                            pieceTexture,
                            pieceX,
                            pieceY,
                            targetWidth,
                            targetHeight
                        );
                    }
                }
            }
        }
    }

    
    /**
     * Gets the texture for a specific chess piece.
     * 
     * @param piece The chess piece
     * @return TextureRegion for the piece
     */
    private TextureRegion getPieceTexture(Piece piece) {
        String regionName = "";
        
        switch (piece) {
            case WHITE_PAWN:
                regionName = "W_Pawn";
                break;
            case WHITE_KNIGHT:
                regionName = "W_Knight";
                break;
            case WHITE_BISHOP:
                regionName = "W_Bishop";
                break;
            case WHITE_ROOK:
                regionName = "W_Rook";
                break;
            case WHITE_QUEEN:
                regionName = "W_Queen";
                break;
            case WHITE_KING:
                regionName = "W_King";
                break;
            case BLACK_PAWN:
                regionName = "B_Pawn";
                break;
            case BLACK_KNIGHT:
                regionName = "B_Knight";
                break;
            case BLACK_BISHOP:
                regionName = "B_Bishop";
                break;
            case BLACK_ROOK:
                regionName = "B_Rook";
                break;
            case BLACK_QUEEN:
                regionName = "B_Queen";
                break;
            case BLACK_KING:
                regionName = "B_King";
                break;
            default:
                return null;
        }
        
        return pieceAtlas.findRegion(regionName);
    }
    
    /**
     * Updates the bounds to match the board renderer.
     */
    public void updateBounds() {
        setBounds(
            boardRenderer.getX(),
            boardRenderer.getY(),
            boardRenderer.getWidth(),
            boardRenderer.getHeight()
        );
    }
    
    @Override
    public void dispose() {
        // The atlas should be disposed by whoever created it
    }
}