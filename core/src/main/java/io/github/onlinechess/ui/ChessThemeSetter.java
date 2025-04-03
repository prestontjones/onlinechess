package io.github.onlinechess.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;

public class ChessThemeSetter {
    private final ChessBoardActor chessBoardActor;
    
    // Current theme state
    private boolean isPerspective = false;
    private String currentTheme = "";
    
    /*
     * There are 5 different boards, with a flat and perspective version
     * 
     * For each flat color there are 2 different piece sets, wood and plain, 
     * each with a normal and simplified version.
     * 
     * For the perspective boards there is a Normal and Wood options only.
     */

    // Flat Board Theme Textures
    private final Texture flatBoardTheme1 = new Texture("pixel chess_v1.2/chess boards/board_plain_01.png");
    private final Texture flatBoardTheme2 = new Texture("pixel chess_v1.2/chess boards/board_plain_02.png");
    private final Texture flatBoardTheme3 = new Texture("pixel chess_v1.2/chess boards/board_plain_03.png");
    private final Texture flatBoardTheme4 = new Texture("pixel chess_v1.2/chess boards/board_plain_04.png");
    private final Texture flatBoardTheme5 = new Texture("pixel chess_v1.2/chess boards/board_plain_05.png");

    // Perspective Board Theme Textures
    private final Texture perspectiveBoardTheme1 = new Texture("pixel chess_v1.2/perspective chess boards/board_persp_01.png");
    private final Texture perspectiveBoardTheme2 = new Texture("pixel chess_v1.2/perspective chess boards/board_persp_02.png");
    // Commented out as they are currently unused:
    // private final Texture perspectiveBoardTheme3 = new Texture("pixel chess_v1.2/perspective chess boards/boards_plain_03.png");
    // private final Texture perspectiveBoardTheme4 = new Texture("pixel chess_v1.2/perspective chess boards/boards_plain_04.png");
    // private final Texture perspectiveBoardTheme5 = new Texture("pixel chess_v1.2/perspective chess boards/boards_plain_05.png");

    // White Flat Piece Textures
    private final Texture whiteFlatPieces = new Texture("pixel chess_v1.2/16x16 pieces/WhitePieces.png");
    private final Texture whiteWoodFlatPieces = new Texture("pixel chess_v1.2/16x16 pieces/blackPieces_Wood.png");
    private final Texture whiteSimplifiedPieces = new Texture("pixel chess_v1.2/16x16 pieces/WhitePieces_Simplified.png");
    private final Texture whiteWoodSimplifiedPieces = new Texture("pixel chess_v1.2/16x16 pieces/WhitePieces_WoodSimplified.png");

    // Black Flat Piece Textures
    private final Texture blackFlatPieces = new Texture("pixel chess_v1.2/16x16 pieces/BlackPieces.png");
    private final Texture blackSimplifiedPieces = new Texture("pixel chess_v1.2/16x16 pieces/BlackPieces_Simplified.png");
    private final Texture blackWoodFlatPieces = new Texture("pixel chess_v1.2/16x16 pieces/BlackPieces_Wood.png");
    private final Texture blackWoodSimplifiedPieces = new Texture("pixel chess_v1.2/16x16 pieces/BlackPieces_WoodSimplified.png");

    // White Perspective Piece Textures
    private final Texture whitePerspectivePiece = new Texture("pixel chess_v1.2/16x32 pieces/WhitePieces-Sheet.png");
    private final Texture whiteWoodPerspectivePiece = new Texture("pixel chess_v1.2/16x32 pieces/WhitePiecesWood-Sheet.png");

    // Black Perspective Piece Textures
    private final Texture blackPerspectivePiece = new Texture("pixel chess_v1.2/16x32 pieces/BlackPieces-Sheet.png");
    private final Texture blackWoodPerspectivePiece = new Texture("pixel chess_v1.2/16x32 pieces/BlackPiecesWood-Sheet.png");
    
    // Current textures
    private Texture currentWhitePieceTexture;
    private Texture currentBlackPieceTexture;

    public ChessThemeSetter(ChessBoardActor boardActor) {
        this.chessBoardActor = boardActor;
        // Default to flat1 theme
        setTheme("flat1");
    }

    public void setTheme(String theme) {
        // Skip if same theme is already set to avoid unnecessary texture switching
        if (theme.equals(currentTheme)) {
            return;
        }
        
        currentTheme = theme;
        isPerspective = theme.startsWith("perspective");
        
        // Set the board texture based on theme
        Texture boardTexture = null;
        
        switch (theme) {
            case "flat1":
                boardTexture = flatBoardTheme1;
                currentWhitePieceTexture = whiteFlatPieces;
                currentBlackPieceTexture = blackFlatPieces;
                break;
            case "flat2":
                boardTexture = flatBoardTheme2;
                currentWhitePieceTexture = whiteWoodFlatPieces;
                currentBlackPieceTexture = blackWoodFlatPieces;
                break;
            case "flat3":
                boardTexture = flatBoardTheme3;
                currentWhitePieceTexture = whiteSimplifiedPieces;
                currentBlackPieceTexture = blackSimplifiedPieces;
                break;
            case "flat4":
                boardTexture = flatBoardTheme4;
                currentWhitePieceTexture = whiteWoodSimplifiedPieces;
                currentBlackPieceTexture = blackWoodSimplifiedPieces;
                break;
            case "flat5":
                boardTexture = flatBoardTheme5;
                currentWhitePieceTexture = whiteFlatPieces;
                currentBlackPieceTexture = blackFlatPieces;
                break;
            case "perspective1":
                boardTexture = perspectiveBoardTheme1;
                currentWhitePieceTexture = whitePerspectivePiece;
                currentBlackPieceTexture = blackPerspectivePiece;
                break;
            case "perspective2":
                boardTexture = perspectiveBoardTheme2;
                currentWhitePieceTexture = whiteWoodPerspectivePiece;
                currentBlackPieceTexture = blackWoodPerspectivePiece;
                break;
            default:
                System.out.println("Invalid theme name, using flat1");
                boardTexture = flatBoardTheme1;
                currentWhitePieceTexture = whiteFlatPieces;
                currentBlackPieceTexture = blackFlatPieces;
                break;
        }
        
        // Verify the texture is valid before setting it
        if (boardTexture != null) {
            // Set the board texture - we're NOT disposing here as we're reusing the same textures
            chessBoardActor.setTexture(boardTexture);
            // Force a resize to ensure the board correctly fits the screen
            float currentWidth = chessBoardActor.getWidth();
            float currentHeight = chessBoardActor.getHeight();
            chessBoardActor.setSize(currentWidth, currentHeight);
        } else {
            System.err.println("Unable to load texture for theme: " + theme);
        }
    }
    
    /**
     * Gets a texture region for a specific piece
     * @param piece The chess piece type
     * @return TextureRegion for the piece
     */
    public TextureRegion getPieceTextureRegion(Piece piece) {
        if (piece == null || piece == Piece.NONE) {
            return null;
        }
        
        // Determine the index for the piece (order is PAWN, KNIGHT, ROOK, BISHOP, QUEEN, KING)
        int pieceIndex;
        switch (piece.getPieceType()) {
            case PAWN:
                pieceIndex = 0;
                break;
            case KNIGHT:
                pieceIndex = 1;
                break;
            case ROOK:
                pieceIndex = 2;
                break;
            case BISHOP:
                pieceIndex = 3;
                break;
            case QUEEN:
                pieceIndex = 4;
                break;
            case KING:
                pieceIndex = 5;
                break;
            default:
                return null;
        }
        
        int pieceWidth = 16;
        int pieceHeight = isPerspective ? 32 : 16;
        
        // Select the appropriate texture based on the piece's side (color)
        Texture textureToUse = (piece.getPieceSide() == Side.WHITE) 
            ? currentWhitePieceTexture 
            : currentBlackPieceTexture;
        
        return new TextureRegion(textureToUse, pieceIndex * pieceWidth, 0, pieceWidth, pieceHeight);
    }
    
    /**
     * @return true if the current theme is perspective
     */
    public boolean isPerspective() {
        return isPerspective;
    }
    
    /**
     * Disposes of all textures
     */
    public void dispose() {
        // Dispose all textures to prevent memory leaks
        flatBoardTheme1.dispose();
        flatBoardTheme2.dispose();
        flatBoardTheme3.dispose();
        flatBoardTheme4.dispose();
        flatBoardTheme5.dispose();
        
        perspectiveBoardTheme1.dispose();
        perspectiveBoardTheme2.dispose();
        
        whiteFlatPieces.dispose();
        whiteWoodFlatPieces.dispose();
        whiteSimplifiedPieces.dispose();
        whiteWoodSimplifiedPieces.dispose();
        
        blackFlatPieces.dispose();
        blackWoodFlatPieces.dispose();
        blackSimplifiedPieces.dispose();
        blackWoodSimplifiedPieces.dispose();
        
        whitePerspectivePiece.dispose();
        whiteWoodPerspectivePiece.dispose();
        
        blackPerspectivePiece.dispose();
        blackWoodPerspectivePiece.dispose();
    }
    
    /**
     * Gets the current white piece texture
     */
    public Texture getWhitePieceTexture() {
        return currentWhitePieceTexture;
    }
    
    /**
     * Gets the current black piece texture
     */
    public Texture getBlackPieceTexture() {
        return currentBlackPieceTexture;
    }
}