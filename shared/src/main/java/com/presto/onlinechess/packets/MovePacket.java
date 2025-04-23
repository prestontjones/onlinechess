package com.presto.onlinechess.packets;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

public class MovePacket {
    private String fromSquare;
    private String toSquare;
    private String promotionPiece;
    
    // Required no-arg constructor for Kryo serialization
    public MovePacket() {}
    
    // Constructor from a Move
    public MovePacket(Move move) {
        this.fromSquare = move.getFrom().toString();
        this.toSquare = move.getTo().toString();
        
        if (move.getPromotion() != null && move.getPromotion() != Piece.NONE) {
            this.promotionPiece = move.getPromotion().toString();
        } else {
            this.promotionPiece = null;
        }
    }
    
    // Convert back to a Move
    public Move toMove() {
        Square from = Square.valueOf(fromSquare);
        Square to = Square.valueOf(toSquare);
        
        if (promotionPiece != null && !promotionPiece.isEmpty()) {
            return new Move(from, to, Piece.valueOf(promotionPiece));
        } else {
            return new Move(from, to);
        }
    }
    
    // Getters and setters
    public String getFromSquare() {
        return fromSquare;
    }
    
    public void setFromSquare(String fromSquare) {
        this.fromSquare = fromSquare;
    }
    
    public String getToSquare() {
        return toSquare;
    }
    
    public void setToSquare(String toSquare) {
        this.toSquare = toSquare;
    }
    
    public String getPromotionPiece() {
        return promotionPiece;
    }
    
    public void setPromotionPiece(String promotionPiece) {
        this.promotionPiece = promotionPiece;
    }
}
