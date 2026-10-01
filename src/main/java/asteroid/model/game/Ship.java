package asteroid.model.game;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

/**
 * Represents the player's ship with position, rotation, and velocity for realistic space physics.
 */
public class Ship {
    // Position
    private double x;
    private double y;
    
    // Velocity and momentum
    private double velocityX = 0;
    private double velocityY = 0;
    private double acceleration = 0.2;
    private double friction = 0.98; // Slows the ship gradually
    
    // Rotation (in radians)
    private double rotation = -Math.PI / 2; // Start facing up (adjusted by -90 degrees)
    private double rotationSpeed = 0.2; // Increased from 0.1 for faster rotation
    
    // Ship properties
    private int width = 50;
    private int height = 60;
    private boolean thrusting = false;  // Flag to indicate if the ship is accelerating
    private boolean drawBounds = false; // Flag to indicate if the ship's boundary should be drawn
    
    /**
     * Creates a new ship at the specified location.
     * @param x The x-coordinate
     * @param y The y-coordinate
     */
    public Ship(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Updates the ship's position based on its current velocity.
     * @param screenWidth The width of the screen
     * @param screenHeight The height of the screen 
     */
    public void update(int screenWidth, int screenHeight) {
        // Apply thrust if the ship is accelerating
        if (thrusting) {
            // Add velocity in the direction the ship is facing
            velocityX += Math.cos(rotation) * acceleration; 
            velocityY += Math.sin(rotation) * acceleration;
        }
        
        // Apply friction to gradually slow down
        velocityX *= friction;
        velocityY *= friction;
        
        // Update position based on velocity
        x += velocityX;
        y += velocityY;
        
        // Screen wrapping (if ship goes off one edge, appear on the opposite edge)
        if (x < 0) x = screenWidth;
        if (x > screenWidth) x = 0;
        if (y < 0) y = screenHeight;
        if (y > screenHeight) y = 0;
    }
    
    /**
     * Rotates the ship counterclockwise.
     */
    public void rotateLeft() {
        rotation -= rotationSpeed;
    }
    
    /**
     * Rotates the ship clockwise.
     */
    public void rotateRight() {
        rotation += rotationSpeed;
    }
    
    /**
     * Applies thrust to accelerate the ship in its current direction.
     * @param thrust true to apply thrust, false to stop thrusting
     */
    public void setThrusting(boolean thrust) {
        this.thrusting = thrust;
    }
    
    /**
     * Gets whether the ship is currently thrusting.
     * @return true if thrusting, false otherwise
     */
    public boolean isThrusting() {
        return thrusting;
    }
    
    /**
     * Gets the coordinates of the ship's nose (front point).
     * Used for positioning bullets when firing.
     * @return Array with x,y coordinates of the ship's nose
     */
    public double[] getNoseCoordinates() {
        double noseX = x + Math.cos(rotation) * height/2;
        double noseY = y + Math.sin(rotation) * height/2;
        return new double[] {noseX, noseY};
    }
    
    /**
     * Gets the bounding rectangle of the ship, centered on its (x, y) position.
     * @return Rectangle representing the ship's bounds
     */
    public Rectangle getBounds() {
        return new Rectangle(
            (int)(x - width / 2.0),
            (int)(y - height / 2.0),
            width,
            height
        );
    }
    
    /**
     * Sets whether to draw the ship's boundary.
     * @param draw true to draw boundary, false otherwise
     */
    public void setDrawBounds(boolean draw) {
        this.drawBounds = draw;
    }

    /**
     * Gets whether the ship's boundary should be drawn.
     * @return true if boundary should be drawn, false otherwise
     */
    public boolean isDrawBounds() {
        return drawBounds;
    }
    
    /**
     * Draws the ship's bounding rectangle if drawBounds is true.
     * @param g The Graphics2D context
     */
    public void drawBoundary(Graphics g) {
        if (drawBounds) {
            Color oldColor = g.getColor();
            g.setColor(Color.RED);
            Rectangle bounds = getBounds();
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }
    
    // Getters and setters
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getRotation() { return rotation; }
    public void setRotation(double rotation) { this.rotation = rotation; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
