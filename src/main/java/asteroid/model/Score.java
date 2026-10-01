package asteroid.model;

/**
 * Represents the player's score in the game.
 * Provides methods to increment, reset and retrieve the score.
 */
public class Score {
    private int value;
    private int highScore;
    
    /**
     * Creates a new Score instance with initial value of 0.
     */
    public Score() {
        this.value = 0;
        this.highScore = 0;
    }
    
    /**
     * Increases the score by the specified amount.
     * @param points The points to add to the current score.
     */
    public void increment(int points) {
        this.value += points;
        // Update high score if current score is higher
        if (this.value > this.highScore) {
            this.highScore = this.value;
        }
    }
    
    /**
     * Resets the score to 0.
     */
    public void reset() {
        this.value = 0;
    }
    
    /**
     * Gets the current score value.
     * @return The current score.
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Gets the high score.
     * @return The high score.
     */
    public int getHighScore() {
        return highScore;
    }
    
    /**
     * Returns a string representation of the current score.
     * @return String representation of the score.
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
