package asteroid.controller;

import javax.swing.JFrame;

import asteroid.controller.game.GameController;
import asteroid.model.GameState;
import asteroid.services.AudioService;
import asteroid.services.GameService;
import asteroid.view.HomeView;
import asteroid.view.SettingsView;
import asteroid.view.game.GamePanel;

/**
 * Controller for the home screen.
 * Handles navigation and button actions.
 */
public class HomeController {
    private HomeView view;
    private JFrame frame;
    private GameService gameService = new GameService();
    // Use the singleton instance of AudioService
    private AudioService audioService = AudioService.getInstance();
    
    /**
     * Creates a new HomeController.
     */
    public HomeController(HomeView view, JFrame frame) {
        this.view = view;
        this.frame = frame;
        initController();
    }
    
    /**
     * Initializes controller by connecting view event handlers.
     */
    private void initController() {
        // Connect buttons to action methods
        view.getBtnStart().addActionListener(e -> startGame());
        view.getBtnSettings().addActionListener(e -> showSettings());
        view.getBtnExit().addActionListener(e -> System.exit(0));
    }
    
    /**
     * Starts a new game.
     */
    private void startGame() {
        // Create a new game state
        GameState state = gameService.createNewGameState();
        
        // Create game panel with game over callback 
        GamePanel gamePanel = new GamePanel(() -> {
            showGameOver(state.getScore());
        });
        
        // Create game controller to connect model and view
        GameController controller = new GameController(state, gameService, gamePanel);
        
        // Configure audio settings using the singleton instance
        controller.setAudioEnabled(audioService.isAudioEnabled());
        
        // Switch to game panel
        frame.setContentPane(gamePanel);
        frame.revalidate();
    }
    
    /**
     * Shows the settings screen.
     */
    private void showSettings() {
        SettingsView settingsView = new SettingsView();
        // Pass the singleton instance to SettingsController
        SettingsController settingsController = new SettingsController(
            settingsView, frame, audioService);
        
        frame.setContentPane(settingsView);
        frame.revalidate();
    }
    
    /**
     * Shows the game over screen with the final score.
     */
    private void showGameOver(int finalScore) {
        GameOverController controller = new GameOverController();
        controller.showGameOver(frame, finalScore);
    }
}