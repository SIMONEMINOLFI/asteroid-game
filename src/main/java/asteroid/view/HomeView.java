package asteroid.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The home screen view that displays game title and main menu options.
 * Enhanced with blue color scheme and updated button text.
 */
public class HomeView extends JPanel {
    // UI Components
    private JLabel lblTitle = new JLabel("ASTEROID GAME", JLabel.CENTER);
    private JLabel lblInstructions = new JLabel(
        "<html>Use arrow keys to move.<br/>Press SPACE to shoot.<br/>Avoid asteroids and collect power-ups!</html>", 
        JLabel.CENTER
    );
    private JButton btnStart = new JButton("Start Game");
    private JButton btnSettings = new JButton("Options");
    private JButton btnExit = new JButton("Exit");

    /**
     * Creates a new HomeView with updated styling and layout.
     */
    public HomeView() {
        // Set panel properties
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        //  title
        lblTitle.setFont(new Font("Arial", Font.BOLD, 36));
        lblTitle.setForeground(new Color(30, 144, 255)); // Dodger Blue
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        //  instructions
        lblInstructions.setForeground(Color.WHITE);
        lblInstructions.setFont(new Font("Arial", Font.PLAIN, 14));
        lblInstructions.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Create a panel for the title and instructions
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(lblInstructions, BorderLayout.CENTER);
        
        // Add the top panel to the main panel
        add(topPanel, BorderLayout.NORTH);
        
        // Configure buttons with blue theme
        configureButton(btnStart);
        configureButton(btnSettings);
        configureButton(btnExit);
        
        // Create a panel with GridLayout for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(btnStart);
        buttonPanel.add(btnSettings);
        buttonPanel.add(btnExit);
        
        // Add the button panel to the center of the layout
        add(buttonPanel, BorderLayout.CENTER);
    }
    
    /**
     * Configures a button with the game's visual style.
     * @param button The button to configure
     */
    private void configureButton(JButton button) {
        button.setBackground(new Color(30, 60, 90));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }
    
    // Getter methods for the buttons (used by controller)
    public JButton getBtnStart() { return btnStart; }
    public JButton getBtnSettings() { return btnSettings; }
    public JButton getBtnExit() { return btnExit; }
}