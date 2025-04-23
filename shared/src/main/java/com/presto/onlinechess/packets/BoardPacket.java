package com.presto.onlinechess.packets;

import com.github.bhlangonijr.chesslib.Board;

public class BoardPacket {
    public Board board;

    // Required no-arg constructor for Kryo serialization
    public BoardPacket() {}

    public BoardPacket(Board board) {
        this.board = board;
    }

}
