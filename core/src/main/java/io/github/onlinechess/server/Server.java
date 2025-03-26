package io.github.onlinechess.server;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;

public class Server {
    private final Board chessBoard;

    // The server manages the chessboard and ensures valid moves are made.
    public Server() {
        chessBoard = new Board();
    }

    public boolean makeMove(Player player, Move move) {
        if (checkIfMoveIsAllowed(move, player)) {
            chessBoard.doMove(move); // Apply the move to the board
            return true;
        }
        return false;
    }

    private boolean checkIfMoveIsAllowed(Move requestedMove, Player player) {
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
}
