package asteroid.model.game;

import java.awt.Rectangle;

/**
 * Represents a power-up item in the game.
 * Currently implements a shield power-up that provides temporary invulnerability.
 */
public class PowerUp {
    // Power-up types
    public static final int SHIELD = 1;
    
    private int x;
    private int y;
    private int width = 30;
    private int height = 30;
    private int type;
    private double speed = 3;
    private boolean active = false;
    private long activationTime = 0;
    private long duration = 5000; // Duration in milliseconds (5 seconds for shield)
    
    /**
     * Creates a new power-up with the specified position and type.
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param type The power-up type
     */
    public PowerUp(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    /**
     * Updates the position of the power-up.
     */
    public void move() {
        this.y += speed;
    }
    
    /**
     * Activates the power-up effect.
     */
    public void activate() {
        this.active = true;
        this.activationTime = System.currentTimeMillis();
    }
    
    /**
     * Checks if the power-up is still active.
     * @return true if the power-up is active, false otherwise
     */
    public boolean isActive() {
        if (!active) {
            return false;
        } else {
            if ((System.currentTimeMillis() - activationTime) < duration) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    /**
     * Deactivates the power-up.
     */
    public void deactivate() {
        this.active = false;
    }
    
    /**
     * Checks if the power-up has moved off the bottom of the screen.
     * @param screenHeight The height of the screen
     * @return true if the power-up is off-screen, false otherwise
     */
    public boolean isOffScreen(int screenHeight) {
        return y > screenHeight;
    }
    
    /**
     * Gets a Rectangle representing the power-up's bounds for collision detection.
     * @return A Rectangle object representing the power-up's position and size
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getType() { return type; }
    
    /**
     * Gets the remaining duration of the power-up effect in milliseconds.
     * @return The remaining duration
     */
    public long getRemainingDuration() {
        if (!active) {
            return 0;
        } else {
            long elapsed = System.currentTimeMillis() - activationTime;
            return Math.max(0, duration - elapsed);
        }
    }
    
    /**
     * Gets the remaining duration as a percentage.
     * @return The remaining duration percentage (0.0 to 1.0)
     */
    public double getRemainingPercentage() {
        if (!active) {
            return 0.0;
        } else {
            return (double) getRemainingDuration() / duration;
        }
    }

}
