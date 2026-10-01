package asteroid.services;

/**
 * Service to manage scoring in the game.
 * Part of the Model layer's business logic in MVC architecture.
 */
public class ScoreService {
    // Point values for different asteroid sizes
    public static final int SMALL_ASTEROID_POINTS = 10;
    public static final int MEDIUM_ASTEROID_POINTS = 30;
    public static final int LARGE_ASTEROID_POINTS = 50;
    
    private int highScore = 0;
    
    /**
     * Get score points based on asteroid size.
     * @param asteroidSize The size of the asteroid (1=small, 2=medium, 3=large)
     * @return The points earned for destroying that asteroid
     */
    public int getAsteroidPoints(int asteroidSize) {
        switch (asteroidSize) {
            case 1: return SMALL_ASTEROID_POINTS;
            case 2: return MEDIUM_ASTEROID_POINTS;
            case 3: return LARGE_ASTEROID_POINTS;
            default: return 0;
        }
    }
    
    /**
     * Updates the high score if the current score is higher.
     * @param currentScore The current score to check against high score
     * @return true if a new high score was set
     */
    public boolean updateHighScore(int currentScore) {
        if (currentScore > highScore) {
            highScore = currentScore;
            return true;
        }
        return false;
    }
    
    /**
     * Gets the high score.
     * @return The high score
     */
    public int getHighScore() {
        return highScore;
    }
}
