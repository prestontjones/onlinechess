package io.github.onlinechess.screens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

public class BoardSquare extends Actor {
    private final Square square;
    private final Piece piece;
    private final TextureRegion texture;
    private boolean selected = false;
    
    public BoardSquare(Square square, Piece piece, TextureRegion texture) {
        this.square = square;
        this.piece = piece;
        this.texture = texture;
        
        setBounds(0, 0, 64, 64);
        
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                selected = !selected;
                return true;
            }
        });
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        
        // Add visual indication when selected (for future implementation)
        if (selected) {
            // Draw selection indicator (this would be implemented later)
        }
    }
    
    public Square getSquare() {
        return square;
    }
    
    public Piece getPiece() {
        return piece;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}