package asteroid.model;

public class GameState {
    private int score;
    private int lives;
    private double gameSpeed;
    
    // Getter and setter for score
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    // Getter and setter for lives
    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }
    // Getter and setter for gameSpeed
    public double getGameSpeed() { return gameSpeed; }
    public void setGameSpeed(double gameSpeed) { this.gameSpeed = gameSpeed; }
}
