package asteroid.model.game;

import java.awt.Rectangle;

/**
 * Represents an asteroid in the game with its position, size, and movement properties.
 */
public class Asteroid {
    // Position and dimensions
    private double x;
    private double y;
    private int width;
    private int height;
    
    // Movement properties
    private double directionX;
    private double directionY;
    private double baseSpeed = 5;
    private double speed = 5;
    
    // Type and characteristics
    private int size; // Size category: 1=small, 2=medium, 3=large
    
    /**
     * Creates a new asteroid with the specified position, dimensions, and size category.
     */
    public Asteroid(int x, int y, int width, int height, int size) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.size = size;
        
        // Adjust speed based on size - smaller asteroids move faster
        switch (size) {
            case 1: // Small
                this.baseSpeed = 7.0; // Small asteroids are fastest
                break;
            case 2: // Medium
                this.baseSpeed = 5.0; // Medium speed
                break;
            case 3: // Large
                this.baseSpeed = 3.0; // Slowest
                break;
            default:
                this.baseSpeed = 5.0; // Default fallback
        }
        
        this.speed = this.baseSpeed;
    }
    
    /**
     * Updates the position of the asteroid based on its direction.
     */
    public void move() {
        this.x += directionX;
        this.y += directionY;
    }
    
    /**
     * Checks if the asteroid has moved completely off screen.
     * @param screenWidth The width of the screen
     * @param screenHeight The height of the screen
     * @return true if the asteroid is off-screen, false otherwise
     */
    public boolean isOffScreen(int screenWidth, int screenHeight) {
        return x < -width*2 || x > screenWidth + width*2 || 
               y < -height*2 || y > screenHeight + height*2;
    }
    
    /**
     * Gets a Rectangle representing the asteroid's bounds for collision detection.
     */
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    
    // Getters and setters
    public double getBaseSpeed() { return baseSpeed; }
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getSize() { return size; }
    
    public void setDirectionX(double directionX) { this.directionX = directionX; }
    public void setDirectionY(double directionY) { this.directionY = directionY; }
    public double getDirectionX() { return directionX; }
    public double getDirectionY() { return directionY; }
}
