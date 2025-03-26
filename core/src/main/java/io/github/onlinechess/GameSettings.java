package io.github.onlinechess;

/**
 * Stores game configuration settings
 */
public class GameSettings {
    // Player settings
    private String username = "Player";
    
    // Audio settings
    private float musicVolume = 0.7f;
    private float sfxVolume = 1.0f;
    private boolean soundEnabled = true;
    
    // Network settings
    private int serverPort = 8080;
    private String serverAddress = "localhost";
    
    // Game settings
    private int aiDifficulty = 2; // 1-3 (Easy, Medium, Hard)
    private boolean aiCommentaryEnabled = true;
    
    // Constructors
    public GameSettings() {
        // Default constructor
    }
    
    // Getters and setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public float getMusicVolume() {
        return musicVolume;
    }
    
    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }
    
    public float getSfxVolume() {
        return sfxVolume;
    }
    
    public void setSfxVolume(float sfxVolume) {
        this.sfxVolume = sfxVolume;
    }
    
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled = soundEnabled;
    }
    
    public int getServerPort() {
        return serverPort;
    }
    
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    
    public String getServerAddress() {
        return serverAddress;
    }
    
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
    
    public int getAiDifficulty() {
        return aiDifficulty;
    }
    
    public void setAiDifficulty(int aiDifficulty) {
        if (aiDifficulty >= 1 && aiDifficulty <= 3) {
            this.aiDifficulty = aiDifficulty;
        }
    }
    
    public boolean isAiCommentaryEnabled() {
        return aiCommentaryEnabled;
    }
    
    public void setAiCommentaryEnabled(boolean aiCommentaryEnabled) {
        this.aiCommentaryEnabled = aiCommentaryEnabled;
    }
}