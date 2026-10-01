package asteroid.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Settings screen that allows the player to configure game options.
 * Currently supports enabling/disabling audio.
 */
public class SettingsView extends JPanel {
    // UI Components
    private JLabel lblTitle = new JLabel("Settings", JLabel.CENTER);
    private JCheckBox chkAudio = new JCheckBox("Enable Audio");
    private JButton btnSave = new JButton("Save");
    private JButton btnCancel = new JButton("Cancel");
    
    /**
     * Creates a new SettingsView with options for game configuration.
     */
    public SettingsView() {
        // Set panel properties
        setLayout(new BorderLayout(0, 20));
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Configure title
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(new Color(30, 144, 255)); // Dodger Blue
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Add title to the top of the panel
        add(lblTitle, BorderLayout.NORTH);
        
        // Configure settings controls
        chkAudio.setForeground(Color.WHITE);
        chkAudio.setBackground(Color.BLACK);
        chkAudio.setFont(new Font("Arial", Font.PLAIN, 16));
        chkAudio.setSelected(true); // Default: audio enabled
        
        // Create settings panel
        JPanel settingsPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        settingsPanel.setBackground(Color.BLACK);
        settingsPanel.add(chkAudio);
        settingsPanel.add(new JLabel()); // Spacer
        
        // Add settings panel to the center
        add(settingsPanel, BorderLayout.CENTER);
        
        // Configure buttons
        configureButton(btnSave);
        configureButton(btnCancel);
        
        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        // Add button panel to the bottom
        add(buttonPanel, BorderLayout.SOUTH);
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
     * Gets the audio checkbox.
     * @return The audio checkbox
     */
    public JCheckBox getChkAudio() {
        return chkAudio;
    }
    
    /**
     * Gets the save button.
     * @return The save button
     */
    public JButton getBtnSave() {
        return btnSave;
    }
    
    /**
     * Gets the cancel button.
     * @return The cancel button
     */
    public JButton getBtnCancel() {
        return btnCancel;
    }
    
    /**
     * Sets the audio checkbox state.
     * @param enabled true to check the box, false to uncheck
     */
    public void setAudioEnabled(boolean enabled) {
        chkAudio.setSelected(enabled);
    }
    
    /**
     * Gets whether audio is enabled from the checkbox state.
     * @return true if audio is enabled, false otherwise
     */
    public boolean isAudioEnabled() {
        return chkAudio.isSelected();
    }
}
