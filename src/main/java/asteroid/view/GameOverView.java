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
 * Game over screen that shows the final score and allows restarting.
 */
public class GameOverView extends JPanel {
    // UI Components
    private JLabel lblGameOver = new JLabel("GAME OVER", JLabel.CENTER);
    private JLabel lblFinalScore = new JLabel("Final Score: 0", JLabel.CENTER);
    private JButton btnRestart = new JButton("Play Again");
    private JButton btnMainMenu = new JButton("Main Menu");
    
    /**
     * Creates a new GameOverView with enhanced styling.
     */
    public GameOverView() {
        // Set panel properties
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.BLUE);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Configure game over message with red color for emphasis
        lblGameOver.setFont(new Font("Arial", Font.BOLD, 48));
        lblGameOver.setForeground(Color.RED);
        lblGameOver.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Configure final score display
        lblFinalScore.setFont(new Font("Arial", Font.BOLD, 24));
        lblFinalScore.setForeground(Color.WHITE);
        lblFinalScore.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        // Create a panel for the game over message and score
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.BLACK);
        messagePanel.add(lblGameOver, BorderLayout.NORTH);
        messagePanel.add(lblFinalScore, BorderLayout.CENTER);
        
        // Add the message panel to the main panel
        add(messagePanel, BorderLayout.NORTH);
        
        // Configure buttons
        configureButton(btnRestart);
        configureButton(btnMainMenu);
        
        // Create a panel with GridLayout for the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(btnRestart);
        buttonPanel.add(btnMainMenu);
        
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
    
    /**
     * Sets the final score display.
     * @param score The final score to display
     */
    public void setFinalScore(int score) {
        lblFinalScore.setText("Final Score: " + score);
    }
    
    /**
     * Gets the restart button.
     * @return The restart button
     */
    public JButton getBtnRestart() {
        return btnRestart;
    }
    
    /**
     * Gets the main menu button.
     * @return The main menu button
     */
    public JButton getBtnMainMenu() {
        return btnMainMenu;
    }
}
