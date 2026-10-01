package asteroid.view.game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import asteroid.view.AsteroidGame;

/**
 * Panel that contains the main game view and score display.
 * Acts as a container for the AsteroidGame component.
 */
public class GamePanel extends JPanel {
    private AsteroidGame game;
    private JLabel scoreLabel;
    private Consumer<Integer> scoreUpdater;
    private DoubleConsumer gameSpeedUpdater;
    private Runnable onGameOver;
    
    /**
     * Creates a new GamePanel with a game over callback.
     * @param onGameOver Callback to execute when the game ends
     */
    public GamePanel(Runnable onGameOver) {
        // Set the game over callback
        this.onGameOver = onGameOver;
        
        // Set layout for the game panel
        setLayout(new BorderLayout());
        
        // Create the score label
        scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        add(scoreLabel, BorderLayout.NORTH);
        
        // Create the game instance
        game = new AsteroidGame();
        
        // Add the game instance to the center of the panel
        add(game, BorderLayout.CENTER);
    }
    
    /**
     * Gets the game view component.
     * @return The AsteroidGame view
     */
    public AsteroidGame getGameView() {
        return game;
    }
    
    /**
     * Gets the game over callback.
     * @return The game over callback
     */
    public Runnable getOnGameOver() {
        return onGameOver;
    }
    
    /**
     * Sets the score updater function.
     * @param updater Consumer that updates the score
     */
    public void setScoreUpdater(Consumer<Integer> updater) {
        this.scoreUpdater = updater;
    }
    
    /**
     * Sets the game speed updater function.
     * @param updater Consumer that updates the game speed
     */
    public void setGameSpeedUpdater(DoubleConsumer updater) {
        this.gameSpeedUpdater = updater;
    }
    
    /**
     * Updates the score display.
     * @param score The new score to display
     */
    public void updateScoreDisplay(int score) {
        scoreLabel.setText("Score: " + score);
    }
    
    /**
     * Gets the current score from the game.
     * @return The current score
     */
    public int getScore() {
        return 0; // Will be provided by game engine
    }
    
    /**
     * Gets the high score from the game.
     * @return The high score
     */
    public int getHighScore() {
        return 0; // Will be provided by game engine
    }
}
