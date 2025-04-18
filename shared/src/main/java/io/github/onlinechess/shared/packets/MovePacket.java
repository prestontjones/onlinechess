package io.github.onlinechess.shared.packets;

import java.util.UUID;

import com.github.bhlangonijr.chesslib.move.Move;

public class MovePacket {
    private Move move;
    
     // Required no-arg constructor for Kryo serialization
     public MovePacket() {}

     public MovePacket(Move move, UUID gameID) {
        


     }
}