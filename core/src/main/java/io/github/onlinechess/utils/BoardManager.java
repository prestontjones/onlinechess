package io.github.onlinechess.utils;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;


public class BoardManager {
    private final Board chessBoard;

    // The server manages the chessboard and ensures valid moves are made.
    public BoardManager() {
        chessBoard = new Board();
    }

    public boolean makeMove(ChessPlayer chessPlayer, Move move) {
        if (checkIfMoveIsAllowed(move, chessPlayer)) {
            chessBoard.doMove(move); // Apply the move to the board
            // returnGameState();
            return true;
        }
        return false;
    }

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

    private boolean checkIfMoveIsLegal(Move requestedMove) {
        return chessBoard.legalMoves().contains(requestedMove);
    }

    private String returnGameState() {
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
    
}
