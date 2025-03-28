package io.github.onlinechess.controllers.java;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.game.Player;
import com.github.bhlangonijr.chesslib.move.Move;

import io.github.onlinechess.utils.BoardManager;

public class ChessPieceController {
    private final BoardManager boardManager;
    private final boolean isOnline;

    public ChessPieceController(boolean isOnline) { 
        this.isOnline = isOnline;
    }

    public Boolean attempMove() {




        if (isOnline) {

        } else {

        }
    }
}
