package io.github.onlinechess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

import io.github.onlinechess.ChessGame;

public class ChessBoardScreen implements Screen {
    private final ChessGame game;
    private final Stage stage;
    private final Skin skin;
    private final Board chessBoard;
    
    // Board dimensions
    private final float squareSize = 64f;
    private final float boardSize = squareSize * 8;
    
    // Chess piece textures
    private final Texture whitePawn, whiteRook, whiteKnight, whiteBishop, whiteQueen, whiteKing;
    private final Texture blackPawn, blackRook, blackKnight, blackBishop, blackQueen, blackKing;
    
    // Error message
    private final Label errorLabel;
    
    public ChessBoardScreen(ChessGame game) {
        this.game = game;
        this.chessBoard = new Board(); // This creates a board in standard starting position
        
        // Load the skin
        skin = new Skin(Gdx.files.internal("skins/os-eight/os-eight.json"));
        
        // Initialize UI
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        // Load chess piece textures
        whitePawn = new Texture("textures/white_pawn.png");
        whiteRook = new Texture("textures/white_rook.png");
        whiteKnight = new Texture("textures/white_knight.png");
        whiteBishop = new Texture("textures/white_bishop.png");
        whiteQueen = new Texture("textures/white_queen.png");
        whiteKing = new Texture("textures/white_king.png");
        
        blackPawn = new Texture("textures/black_pawn.png");
        blackRook = new Texture("textures/black_rook.png");
        blackKnight = new Texture("textures/black_knight.png");
        blackBishop = new Texture("textures/black_bishop.png");
        blackQueen = new Texture("textures/black_queen.png");
        blackKing = new Texture("textures/black_king.png");
        
        // Create main layout
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        
        // Error message label
        errorLabel = new Label("", skin);
        errorLabel.setAlignment(Align.center);
        errorLabel.setWrap(true);
        
        // Add error label at top
        mainTable.add(errorLabel).colspan(2).expandX().fillX().pad(10).row();
        
        // Create empty board table (actual board is drawn in render)
        Table boardTable = new Table();
        boardTable.setBackground(skin.getDrawable("window"));
        
        // Create chat panel
        Table chatTable = new Table();
        chatTable.setBackground(skin.getDrawable("window"));
        Label chatLabel = new Label("Chat area (to be implemented)", skin);
        chatLabel.setWrap(true);
        chatTable.add(chatLabel).expand().fill().pad(10);
        
        // Add board and chat to the main table
        mainTable.add(boardTable).width(boardSize).height(boardSize).pad(10);
        mainTable.add(chatTable).width(300).height(boardSize).pad(10).fillY();
        
        stage.addActor(mainTable);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Update the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1/30f));
        stage.draw();
        
        // Draw the chess board and pieces
        SpriteBatch batch = game.getBatch();
        batch.begin();
        
        // Calculate the center of the screen for board positioning
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float boardX = (screenWidth - boardSize) / 2;
        float boardY = (screenHeight - boardSize) / 2;
        
        // Draw the board squares
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // Determine square color (alternating pattern)
                boolean isLightSquare = (row + col) % 2 == 0;
                
                // Draw the square
                if (isLightSquare) {
                    batch.setColor(0.9f, 0.9f, 0.8f, 1); // Light square color
                } else {
                    batch.setColor(0.5f, 0.5f, 0.4f, 1); // Dark square color
                }
                
                float squareX = boardX + col * squareSize;
                float squareY = boardY + (7 - row) * squareSize; // Flip rows to match chess notation
                
                // Draw square as a filled rectangle
                Texture whiteTexture = game.getAssetManager().get("textures/white.png", Texture.class);
                batch.draw(whiteTexture, squareX, squareY, squareSize, squareSize);
                
                // Reset color for piece drawing
                batch.setColor(1, 1, 1, 1);
                
                // Get the chess piece at this square and draw it if there is one
                Square square = Square.values()[row * 8 + col];
                Piece piece = chessBoard.getPiece(square);
                
                if (piece != Piece.NONE) {
                    Texture pieceTexture = getPieceTexture(piece);
                    if (pieceTexture != null) {
                        batch.draw(pieceTexture, squareX, squareY, squareSize, squareSize);
                    }
                }
            }
        }
        
        batch.end();
    }
    
    private Texture getPieceTexture(Piece piece) {
        switch (piece) {
            case WHITE_PAWN: return whitePawn;
            case WHITE_ROOK: return whiteRook;
            case WHITE_KNIGHT: return whiteKnight;
            case WHITE_BISHOP: return whiteBishop;
            case WHITE_QUEEN: return whiteQueen;
            case WHITE_KING: return whiteKing;
            case BLACK_PAWN: return blackPawn;
            case BLACK_ROOK: return blackRook;
            case BLACK_KNIGHT: return blackKnight;
            case BLACK_BISHOP: return blackBishop;
            case BLACK_QUEEN: return blackQueen;
            case BLACK_KING: return blackKing;
            default: return null;
        }
    }
    
    public void displayError(String message) {
        errorLabel.setText(message);
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    
    @Override
    public void pause() {
        // Not needed for now
    }
    
    @Override
    public void resume() {
        // Not needed for now
    }
    
    @Override
    public void hide() {
        // Not needed for now
    }
    
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        
        // Dispose textures
        whitePawn.dispose();
        whiteRook.dispose();
        whiteKnight.dispose();
        whiteBishop.dispose();
        whiteQueen.dispose();
        whiteKing.dispose();
        blackPawn.dispose();
        blackRook.dispose();
        blackKnight.dispose();
        blackBishop.dispose();
        blackQueen.dispose();
        blackKing.dispose();
    }
}