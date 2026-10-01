package asteroid;

import javax.swing.JFrame;
import asteroid.controller.HomeController;
import asteroid.view.HomeView;

/**
 * Main application entry point that initializes the game frame and launches the home screen.
 */
public class Main {
    public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> {
        // Create the main application frame
            JFrame frame = new JFrame("Asteroid");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setResizable(false);
            
            // Initialize home view
            HomeView homeView = new HomeView();
            frame.setContentPane(homeView);
            
            // Connect view with controller and pass the frame for navigation
            HomeController homeController = new HomeController(homeView, frame);
            
            // Display the frame
            frame.setVisible(true);
       // });
    }
}