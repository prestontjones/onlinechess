package io.github.onlinechess.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import io.github.onlinechess.Main;
import io.github.onlinechess.ui.dialogs.HostGameDialog;
import io.github.onlinechess.ui.dialogs.JoinGameDialog;
import io.github.onlinechess.ui.dialogs.SettingsDialog;

public class MainMenuScreen extends BaseScreen {
    private Table mainTable;
    private Image logoImage;
    private Label titleLabel;
    private Window settingsWindow;
    private Window hostGameWindow;
    private Window joinGameWindow;
    
    public MainMenuScreen(final Main game) {
        super(game);
        createUI();
    }
    
    private void createUI() {
        // Set the background of the stage
        Table background = new Table();
        background.setFillParent(true);
        background.setBackground(skin.getTiledDrawable("bg"));
        stage.addActor(background);
        
        // Main table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(20); // Overall padding
        stage.addActor(mainTable);

        

        // Header Section with background
        Table header = new Table();
        header.setBackground(skin.getDrawable("window"));
        header.pad(10);
        
        try {
            Texture logoTexture = new Texture(Gdx.files.internal("raw os8ui/splash.png"));
            logoImage = new Image(logoTexture);
            header.add(logoImage).size(80).padRight(20); // Fixed size for simplicity
        } catch (Exception e) {
            Gdx.app.error("MainMenu", "Logo load failed", e);
        }

        titleLabel = new Label("[#55bce0]Chess [BLACK]Online", skin, "title");
        header.add(titleLabel).left();
        mainTable.add(header).top().fillX().padBottom(40).row();

        // Buttons with nice background
        Table buttonContainer = new Table();
        buttonContainer.setBackground(skin.getDrawable("white-rect"));
        buttonContainer.pad(20);
        
        Table buttons = new Table();
        buttons.defaults().pad(15).minWidth(280).height(60).fillX();

        TextButton offlineButton = new TextButton("Offline Game", skin);
        TextButton hostButton = new TextButton("Host Game", skin);
        TextButton joinButton = new TextButton("Join Game", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        buttons.add(offlineButton).row();
        buttons.add(hostButton).row();
        buttons.add(joinButton).row();
        buttons.add(settingsButton).row();
        buttons.add(exitButton).row();

        buttonContainer.add(buttons).expand().fill();
        mainTable.add(buttonContainer).expand().center().minWidth(400).row();

        // Footer Section with background
        Table footer = new Table();
        footer.setBackground(skin.getDrawable("window"));
        footer.pad(10);
        
        Label versionLabel = new Label("v0.1.0", skin);
        versionLabel.setColor(Color.GRAY);
        footer.add(versionLabel).expandX().left();

        Label creditLabel = new Label("Made with libGDX", skin);
        creditLabel.setColor(Color.GRAY);
        footer.add(creditLabel).expandX().right();

        mainTable.add(footer).bottom().fillX();

        // Event Listeners
        offlineButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("Chess", "Offline Game button clicked");
                game.setScreen(new ChessBoardScreen(game));
            }
        });
        
        hostButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                showHostGameDialog();
            }
        });
        
        joinButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                showJoinGameDialog();
            }
        });
        
        settingsButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                showSettingsDialog();
            }
        });
        
        exitButton.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    // Dialog methods
    private void showHostGameDialog() {
        if (hostGameWindow != null) hostGameWindow.remove();
        hostGameWindow = HostGameDialog.create(game, stage, skin, this);
    }

    private void showJoinGameDialog() {
        if (joinGameWindow != null) joinGameWindow.remove();
        joinGameWindow = JoinGameDialog.create(game, stage, skin, this);
    }

    public void showSettingsDialog() {
        if (settingsWindow != null) settingsWindow.remove();
        settingsWindow = SettingsDialog.create(game, stage, skin, this);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (settingsWindow != null) settingsWindow.remove();
        if (hostGameWindow != null) hostGameWindow.remove();
        if (joinGameWindow != null) joinGameWindow.remove();
    }
}