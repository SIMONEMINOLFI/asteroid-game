package asteroid.services;

import asteroid.model.GameState;
import asteroid.model.Score;

/**
 * Service class for managing game state updates including score, lives and game speed.
 */
public class GameService {
    
    /**
     * Updates the score in the game state.
     * @param state The current game state
     * @param delta The amount to change the score by
     */
    public void updateScore(GameState state, int delta) {
        state.setScore(state.getScore() + delta);
    }
    
    /**
     * Increments the score in a Score object.
     * @param score The Score object to update
     * @param points The points to add
     */
    public void incrementScore(Score score, int points) {
        score.increment(points);
    }
    
    /**
     * Decreases the number of lives in the game state.
     * @param state The current game state
     * @return true if player still has lives left, false otherwise
     */
    public boolean decrementLives(GameState state) {
        int currentLives = state.getLives() - 1;
        state.setLives(currentLives);
        return currentLives > 0;
    }
    
    /**
     * Increases the game speed based on score milestones.
     * @param state The current game state
     * @param threshold The score threshold at which to increase speed
     * @param increment The amount to increase speed by
     */
    public void updateGameSpeed(GameState state, int threshold, double increment) {
        if (state.getScore() > 0 && state.getScore() % threshold == 0) {
            state.setGameSpeed(state.getGameSpeed() + increment);
        }
    }
    
    /**
     * Initializes a new game state with default values.
     * @return A new GameState object
     */
    public GameState createNewGameState() {
        GameState state = new GameState();
        state.setScore(0);
        state.setLives(3);
        state.setGameSpeed(1.0);
        return state;
    }
}
