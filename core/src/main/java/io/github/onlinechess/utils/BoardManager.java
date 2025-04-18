package io.github.onlinechess.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import io.github.onlinechess.ui.ChessBoardActor;
import io.github.onlinechess.ui.ChessPieceActor;
import io.github.onlinechess.ui.ChessThemeSetter;

/**
 * Manages chess piece actors and their relationship with the chess board model.
 * Handles creation, movement, and deletion of pieces.
 * Acts as the bridge between UI and game logic.
 */
public class BoardManager {
    private final Group pieceContainer;
    private final ChessBoardActor boardActor;
    private final ChessThemeSetter themeSetter;
    private final ChessBoard chessBoard;
    private final ChessPlayer whitePlayer;
    private final ChessPlayer blackPlayer;
    
    // Track all pieces by their square position
    private final Map<Square, ChessPieceActor> piecesBySquare = new HashMap<>();
    // Track all pieces for each player
    private final List<ChessPieceActor> whitePieces = new ArrayList<>();
    private final List<ChessPieceActor> blackPieces = new ArrayList<>();
    
    /**
     * Creates a new board manager
     * 
     * @param pieceContainer The group that will contain all piece actors
     * @param boardActor The chess board actor
     * @param themeSetter The theme setter for pieces
     * @param chessBoard The chess board logic
     * @param whitePlayer The white player
     * @param blackPlayer The black player
     */
    public BoardManager(Group pieceContainer, ChessBoardActor boardActor, 
                        ChessThemeSetter themeSetter, ChessBoard chessBoard,
                        ChessPlayer whitePlayer, ChessPlayer blackPlayer) {
        this.pieceContainer = pieceContainer;
        this.boardActor = boardActor;
        this.themeSetter = themeSetter;
        this.chessBoard = chessBoard;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
    }

    // Method for creating an easy offline game, where two generic players are created.
    public BoardManager(Group pieceContainer, ChessBoardActor boardActor, 
                        ChessThemeSetter themeSetter, ChessBoard chessBoard) {
        this.pieceContainer = pieceContainer;
        this.boardActor = boardActor;
        this.themeSetter = themeSetter;
        this.chessBoard = chessBoard;
        this.whitePlayer = new ChessPlayer("White Player", Side.WHITE, true);
        this.blackPlayer = new ChessPlayer("Black Player", Side.BLACK, false);
    }
    
    /**
     * Attempt to make a move - this is called from the ChessPieceActor when a piece is dragged
     */
    public boolean attemptMove(ChessPlayer player, Move move) {
        boolean moveSuccessful = chessBoard.makeMove(player, move);
        
        if (moveSuccessful) {
            // Update the visual representation
            updateAfterMove(move);
        }
        
        return moveSuccessful;
    }
    
    /**
     * Initializes the board with standard chess starting positions
     */
    public void initializeStandardBoard() {
        // Clear any existing pieces
        clearAllPieces();
        
        // Create pieces for each square on the board based on the chess board model
        for (Square square : Square.values()) {
            Piece piece = chessBoard.getPiece(square);
            if (piece != Piece.NONE) {
                createPiece(piece, square);
            }
        }
    }
    
    /**
     * Creates a piece at the specified square
     */
    public ChessPieceActor createPiece(Piece piece, Square square) {
        // Determine which player controls this piece
        ChessPlayer piecePlayer = (piece.getPieceSide() == Side.WHITE) ? whitePlayer : blackPlayer;
        
        // Create the piece actor
        ChessPieceActor pieceActor = new ChessPieceActor(boardActor, themeSetter, piece, square, piecePlayer, this);
        
        // Add to appropriate lists
        piecesBySquare.put(square, pieceActor);
        if (piece.getPieceSide() == Side.WHITE) {
            whitePieces.add(pieceActor);
        } else {
            blackPieces.add(pieceActor);
        }

        // Add to stage
        pieceContainer.addActor(pieceActor);
        
        return pieceActor;
    }

    /**
     * Updates piece textures based on the current theme
     * Call this whenever the theme changes
     */
    public void refreshPieceTextures() {
        updateAllPiecesTextures();
    }
    
    /**
     * Removes a piece from a square
     */
    public void removePiece(Square square) {
        ChessPieceActor piece = piecesBySquare.remove(square);
        if (piece != null) {
            // Remove from appropriate lists
            if (piece.getPieceSide() == Side.WHITE) {
                whitePieces.remove(piece);
            } else {
                blackPieces.remove(piece);
            }
            
            // Remove from stage
            piece.remove();
        }
    }
    
    /**
     * Updates piece positions after a move is made
     */
    public void updateAfterMove(Move move) {
        Square from = move.getFrom();
        Square to = move.getTo();
        
        // Get the piece that was at the source before the move
        Piece originalPiece = null;
        ChessPieceActor piece = piecesBySquare.remove(from);
        if (piece != null) {
            originalPiece = piece.getChessPiece();
        }
        
        // Get the current piece at the destination after the move
        Piece currentPiece = chessBoard.getPiece(to);
        
        // Check if this is a promotion by comparing the piece types
        boolean isPromotion = originalPiece != null && 
                             (originalPiece == Piece.WHITE_PAWN || originalPiece == Piece.BLACK_PAWN) && 
                             currentPiece != originalPiece;
        
        // First, remove any captured piece at the destination
        if (piecesBySquare.containsKey(to)) {
            removePiece(to);
        }
        
        // For promotions, we need to completely replace the pawn with the new piece
        if (isPromotion) {
            // Remove the pawn if it still exists
            if (piece != null) {
                piece.remove();
            }
            
            // Create the new promotion piece at the destination square
            createPiece(currentPiece, to);
        } else {
            // For regular moves, just update the piece's position
            if (piece != null) {
                piece.setCurrentSquare(to);
                piecesBySquare.put(to, piece);
            }
        }
        
        // Handle special moves (castling, en passant)
        // TODO: Implement special move handling
    }
    
    /**
     * Updates textures for all pieces when theme changes
     */
    public void updateAllPiecesTextures() {
        for (ChessPieceActor piece : piecesBySquare.values()) {
            piece.updateTexture();
        }
    }
    
    /**
     * Clears all pieces from the board
     */
    public void clearAllPieces() {
        for (ChessPieceActor piece : piecesBySquare.values()) {
            piece.remove();
        }
        
        piecesBySquare.clear();
        whitePieces.clear();
        blackPieces.clear();
    }
    
    /**
     * Updates the board size and repositions all pieces accordingly.
     * This is the main method that should be called whenever the board size changes.
     * 
     * @param boardSize the new size for the board
     */
    public void updateBoardSize(float boardSize) {
        // First resize the board actor
        boardActor.setSize(boardSize, boardSize);
        
        // Then update all pieces to match their new square positions
        updateAllPiecePositions();
    }
    
    /**
     * Updates the positions of all pieces to match their squares on the board.
     * This should be called after any change to the board's size or position.
     */
    public void updateAllPiecePositions() {
        for (Map.Entry<Square, ChessPieceActor> entry : piecesBySquare.entrySet()) {
            Square square = entry.getKey();
            ChessPieceActor piece = entry.getValue();
            
            // Snap the piece to its square, which will update its position and size
            piece.snapToSquare(square);
        }
    }
    
    /**
     * Gets a piece at a specific square
     */
    public ChessPieceActor getPiece(Square square) {
        return piecesBySquare.get(square);
    }
    
    /**
     * Gets all pieces for a player
     */
    public List<ChessPieceActor> getPlayerPieces(Side side) {
        return (side == Side.WHITE) ? whitePieces : blackPieces;
    }
    
    /**
     * Gets the chess board
     */
    public ChessBoard getChessBoard() {
        return chessBoard;
    }
    
    /**
     * Get the white player
     */
    public ChessPlayer getWhitePlayer() {
        return whitePlayer;
    }
    
    /**
     * Get the black player
     */
    public ChessPlayer getBlackPlayer() {
        return blackPlayer;
    }
    
    /**
     * Get the current game state as a string
     */
    public String getGameStateMessage() {
        return chessBoard.getGameState();
    }
}