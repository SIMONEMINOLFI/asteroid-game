package asteroid.model.game;

import java.awt.Rectangle;

/**
 * Represents a bullet fired from the ship that travels in a straight line.
 */
public class Bullet {
    // Position and direction
    private double x;
    private double y;
    private double directionX;
    private double directionY;
    private double speed = 10;
    
    // Size for rendering and collision
    private int width = 3;
    private int height = 3;
    
    // Lifespan timer to remove bullets after a certain time
    private int lifespan = 30; // frames before bullet disappears
    
    /**
     * Creates a new bullet with the specified position and direction.
     * @param x The starting x-coordinate
     * @param y The starting y-coordinate
     * @param rotation The direction in radians that the bullet will travel
     */
    public Bullet(double x, double y, double rotation) {
        this.x = x;
        this.y = y;
        
        // Calculate movement direction based on rotation
        this.directionX = Math.cos(rotation) * speed;
        this.directionY = Math.sin(rotation) * speed;
    }
    
    /**
     * Updates the bullet's position and lifespan.
     * @return true if the bullet is still active, false if it should be removed
     */
    public boolean update(int screenWidth, int screenHeight) {
        // Move bullet
        x += directionX;
        y += directionY;
        
        // Screen wrapping
        if (x < 0) x = screenWidth;
        if (x > screenWidth) x = 0;
        if (y < 0) y = screenHeight;
        if (y > screenHeight) y = 0;
        
        // Decrement lifespan
        lifespan--;
        
        // Return true if bullet is still active
        return lifespan > 0;
    }
    
    /**
     * Gets a Rectangle representing the bullet's bounds for collision detection.
     * @return A Rectangle object representing the bullet's position and size
     */
    public Rectangle getBounds() {
        return new Rectangle((int)x - width/2, (int)y - height/2, width, height);
    }
    
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
