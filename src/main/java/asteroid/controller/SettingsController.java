package asteroid.controller;

import javax.swing.JFrame;
import asteroid.services.AudioService;
import asteroid.view.HomeView;
import asteroid.view.SettingsView;

/**
 * Controller for the settings screen that manages user preferences.
 */
public class SettingsController {
    private SettingsView view;
    private JFrame frame;
    private AudioService audioService;
    private boolean audioEnabled;
    
    /**
     * Creates a new SettingsController.
     * @param view The settings view
     * @param frame The main application frame
     * @param audioService The audio service to configure
     */
    public SettingsController(SettingsView view, JFrame frame, AudioService audioService) {
        this.view = view;
        this.frame = frame;
        this.audioService = audioService;
        
        // Initialize view with current settings
        this.audioEnabled = audioService.isAudioEnabled();
        view.setAudioEnabled(audioEnabled);
        
        // Set up event handlers
        initController();
    }
    
    /**
     * Initializes the controller by connecting view event handlers.
     */
    private void initController() {
        // Save button applies settings and returns to home
        view.getBtnSave().addActionListener(e -> {
            saveSettings();
            showHome();
        });
        
        // Cancel button returns to home without saving
        view.getBtnCancel().addActionListener(e -> showHome());
    }
    
    /**
     * Saves the current settings.
     */
    private void saveSettings() {
        // Update audio setting
        audioEnabled = view.isAudioEnabled();
        audioService.setAudioEnabled(audioEnabled);
    }
    
    /**
     * Returns to the home screen.
     */
    private void showHome() {
        HomeView homeView = new HomeView();

        HomeController homeController = new HomeController(homeView, frame); // mancava questo 
        
        frame.setContentPane(homeView);
        frame.revalidate();
    }
}
