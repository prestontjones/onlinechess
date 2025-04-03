package io.github.onlinechess.controllers;

import com.github.bhlangonijr.chesslib.move.Move;

import io.github.onlinechess.utils.BoardManager;
import io.github.onlinechess.utils.ChessPlayer;

public class ChessPieceController {
    private final BoardManager boardManager;
    private final boolean isOnline;

    public ChessPieceController(boolean isOnline) { 
        this.isOnline = isOnline;
        boardManager = new BoardManager();
    }

    public boolean attemptMove(ChessPlayer player, Move move) {
        if (isOnline) {
            // In online mode, validate locally first to give immediate feedback
            if (!boardManager.makeMove(player, move)) {
                return false;
            }
            
            // TODO: Send move to server (DONT DO THIS NOW ONLY DO OFFLINE MODE)
            // Example: networkManager.sendMove(player, move);
            
            return true;
        } else {
            // In offline mode, just use the board manager directly
            return boardManager.makeMove(player, move);
        }
    }

    
}
