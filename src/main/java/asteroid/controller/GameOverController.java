package asteroid.controller;

import javax.swing.JFrame;

import asteroid.controller.game.GameController;
import asteroid.model.GameState;
import asteroid.services.GameService;
import asteroid.view.GameOverView;
import asteroid.view.HomeView;
import asteroid.view.game.GamePanel;

/**
 * Controller for the game over screen that handles restart functionality.
 * Enhanced to show final score and return to main menu.
 */
public class GameOverController {
    private GameService gameService;
    private int finalScore;
    
    /**
     * Creates a new GameOverController.
     */
    public GameOverController() {
        this.gameService = new GameService();
    }
    
    /**
     * Shows the game over screen with the final score.
     * @param frame The main application frame
     */
    public void showGameOver(JFrame frame) {
        showGameOver(frame, 0);
    }
    
    /**
     * Shows the game over screen with a specific final score.
     * @param frame The main application frame
     * @param finalScore The final score to display
     */
    public void showGameOver(JFrame frame, int finalScore) {
        this.finalScore = finalScore;
        
        GameOverView gameOverView = new GameOverView();
        gameOverView.setFinalScore(finalScore);
        
        // Configure button actions
        gameOverView.getBtnRestart().addActionListener(e -> restartGame(frame));
        gameOverView.getBtnMainMenu().addActionListener(e -> returnToMainMenu(frame));
        
        frame.setContentPane(gameOverView);
        frame.revalidate();
    }
    
    /**
     * Restarts the game by creating a new GamePanel and GameController.
     * @param frame The main application frame
     */
    public void restartGame(JFrame frame) {
        // Create a new game state
        GameState state = gameService.createNewGameState();
        
        // Create game panel with game over callback that passes the score
        GamePanel gamePanel = new GamePanel(() -> {
            // Pass the final score from gameState to the game over screen
            showGameOver(frame, state.getScore());
        });
        
        // Create game controller
        GameController controller = new GameController(state, gameService, gamePanel);
        
        // Switch to game panel
        frame.setContentPane(gamePanel);
        frame.revalidate();
    }
    
    /**
     * Returns to the main menu.
     * @param frame The main application frame
     */
    public void returnToMainMenu(JFrame frame) {
        HomeView homeView = new HomeView();
        HomeController homeController = new HomeController(homeView, frame);
        
        frame.setContentPane(homeView);
        frame.revalidate();
    }
    
    /**
     * Gets the final score.
     * @return The final score
     */
    public int getFinalScore() {
        return finalScore;
    }
}
