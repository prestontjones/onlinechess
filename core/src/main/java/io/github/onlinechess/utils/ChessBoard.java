package io.github.onlinechess.utils;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

/**
 * Core chess game logic that wraps the chesslib Board.
 * Handles validation and execution of moves according to chess rules.
 */
public class ChessBoard {
    private final Board chessBoard;
    private final boolean isOnline;
    
    /**
     * Creates a new chess board with standard initial position
     * 
     * @param isOnline Whether this is an online game
     */
    public ChessBoard(boolean isOnline) {
        this.chessBoard = new Board();
        this.isOnline = isOnline;
    }
    
    /**
     * Make a move on the chess board
     * 
     * @param player The player making the move
     * @param move The move to execute
     * @return Whether the move was successful
     */
    public boolean makeMove(ChessPlayer player, Move move) {
        if (checkIfMoveIsAllowed(move, player)) {
            // If online, we would send the move to the server here
            if (isOnline) {
                // TODO: Send move to server
            }
            
            // Apply the move to the local board
            chessBoard.doMove(move);
            return true;
        }
        return false;
    }
    
    /**
     * Check if a move is allowed for the player
     */
    private boolean checkIfMoveIsAllowed(Move requestedMove, ChessPlayer player) {
        // Check if it's the player's turn
        if (!player.getSide().equals(chessBoard.getSideToMove())) {
            return false;
        }
        
        // Check if the game is already over
        if (chessBoard.isMated() || chessBoard.isStaleMate() || chessBoard.isDraw()) {
            return false;
        }
        
        // Check if the move is legal
        return checkIfMoveIsLegal(requestedMove);
    }
    
    /**
     * Check if a move is legal according to chess rules
     */
    private boolean checkIfMoveIsLegal(Move requestedMove) {
        return chessBoard.legalMoves().contains(requestedMove);
    }
    
    /**
     * Get the current state of the game as a string
     */
    public String getGameState() {
        if (chessBoard.isMated()) {
            return "Checkmate! " + (chessBoard.getSideToMove().flip()) + " wins";
        }
        
        if (chessBoard.isStaleMate()) {
            return "Stalemate! The game is a draw";
        }
        
        if (chessBoard.isInsufficientMaterial()) {
            return "Draw due to insufficient material";
        }
        
        if (chessBoard.isDraw()) {
            return "Draw";
        }
        
        if (chessBoard.isKingAttacked()) {
            return "Check! " + chessBoard.getSideToMove() + " is in danger";
        }
        
        return "Game in progress: " + chessBoard.getSideToMove() + " to move";
    }
    
    /**
     * Get the side to move
     * 
     * @return The side that should move next
     */
    public Side getSideToMove() {
        return chessBoard.getSideToMove();
    }
    
    /**
     * Get the underlying chess board
     * 
     * @return The chess board
     */
    public Board getChessLibBoard() {
        return chessBoard;
    }
    
    /**
     * Get the piece at a specific square
     */
    public Piece getPiece(Square square) {
        return chessBoard.getPiece(square);
    }
    
    /**
     * Check if this is an online game
     */
    public boolean isOnline() {
        return isOnline;
    }
}