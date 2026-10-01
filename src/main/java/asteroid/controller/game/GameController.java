package asteroid.controller.game;

import asteroid.model.GameState;
import asteroid.services.GameService;
import asteroid.view.AsteroidGame;
import asteroid.view.game.GamePanel;

/**
 * Controller that connects the game model with the view.
 * Part of the Controller layer in MVC architecture.
 */
public class GameController {
    private GameState state;
    private GameService service;
    private GamePanel view;
    private GameEngine gameEngine;
    private AsteroidGame gameView;
    
    /**
     * Creates a new GameController that connects models and views.
     */
    public GameController(GameState state, GameService service, GamePanel view) {
        this.state = state;
        this.service = service;
        this.view = view;
        this.gameView = view.getGameView();
        
        initializeGameEngine();
        initController();
    }
    
    /**
     * Initializes the game engine with the current models and view.
     */
    private void initializeGameEngine() {
        // Create game engine with update callback to refresh the view
        gameEngine = new GameEngine(state, service, this::updateView);
        
        // Set game over callback to show game over screen
        gameEngine.setOnGameOver(() -> {
            if (view.getOnGameOver() != null) {
                view.getOnGameOver().run();
            }
        });
        
        // Connect game engine to view
        gameView.setGameEngine(gameEngine);
        
        // Start the game
        gameEngine.start();
    }
    
    /**
     * Initializes controller by setting up event handling.
     */
    private void initController() {
        // Connect view events to controller methods
        view.setGameSpeedUpdater(this::updateGameSpeed);
        view.setScoreUpdater(this::updateScore);
    }
    
    /**
     * Updates the view based on the current game state.
     */
    private void updateView() {
        // Update score display in both panels
        view.updateScoreDisplay(state.getScore());
        gameView.updateScoreDisplay();
        
        // Repaint the game view
        gameView.repaint();
    }
    
    /**
     * Updates the score in the game state.
     */
    public void updateScore(int delta) {
        service.updateScore(state, delta);
        view.updateScoreDisplay(state.getScore());
    }
    
    /**
     * Updates the game speed.
     */
    public void updateGameSpeed(double newSpeed) {
        state.setGameSpeed(newSpeed);
    }
    
    /**
     * Gets the current game state.
     */
    public GameState getGameState() {
        return state;
    }
    
    /**
     * Sets whether audio is enabled in the game engine.
     */
    public void setAudioEnabled(boolean enabled) {
        if (gameEngine != null) {
            gameEngine.setAudioEnabled(enabled);
        }
    }
    
    /**
     * Gets whether audio is enabled in the game engine.
     */
    public boolean isAudioEnabled() {
        return gameEngine != null && gameEngine.getAudioService().isAudioEnabled();
    }
}
