package asteroid.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import asteroid.controller.game.GameEngine;
import asteroid.model.game.Asteroid;
import asteroid.model.game.Bullet;
import asteroid.model.game.PowerUp;
import asteroid.model.game.Ship;

/**
 * The main game panel that handles rendering game elements.
 * Enhanced to display bullet firing, shield effects, and life icons.
 */
public class AsteroidGame extends JPanel implements KeyListener {
    // Game engine reference
    private GameEngine gameEngine;
    
    // UI components 
    private JLabel scoreLabel;
    
    // Heart icon for lives display
    private ImageIcon heartIcon;

    // Game entity images
    private Image shipImage;
    private Image asteroidImage;
    private Image shieldImage;
   // private Image ufoImage;
    
    // Constants
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 500;

    private static final Color PRIMARY_COLOR = new Color(30, 144, 255); // Dodger Blue

    /**
     * Creates a new AsteroidGame panel.
     */
    public AsteroidGame() {
        setupPanel();
        setupScoreLabel();
        loadResources();
        requestFocusInWindow();
    }

    /**
     * Sets up the game panel properties.
     */
    private void setupPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLUE);
        setLayout(new BorderLayout());
        setFocusable(true);
        addKeyListener(this);
    }
    
    /**
     * Sets up the score label.
     */
    private void setupScoreLabel() {
        scoreLabel = new JLabel("SCORE: 0", SwingConstants.RIGHT);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 10));
        add(scoreLabel, BorderLayout.NORTH);
    }

    /**
     * Loads game resources including images for game entities.
     */
    private void loadResources() {
        try {
            heartIcon = loadImageResource("/images/heart.png");
            shipImage = loadImageAsResource("/images/ship.png");
            asteroidImage = loadImageAsResource("/images/asteroid.png");
            shieldImage = loadImageAsResource("/images/shield.png");
           // ufoImage = loadImageAsResource("/images/ufo.png");
        } catch (Exception e) {
            System.err.println("Could not load images: " + e.getMessage());
        }
    }
    
    /**
     * Loads an image resource as ImageIcon.
     */
    private ImageIcon loadImageResource(String path) {
        java.net.URL url = getClass().getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            System.err.println("Could not find resource: " + path);
            return null;
        }
    }
    
    /**
     * Loads an image resource as Image.
     */
    private Image loadImageAsResource(String path) {
        ImageIcon icon = loadImageResource(path);
        if (icon != null && icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            return icon.getImage();
        }
        return null;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();  // Request focus for key events
    }

    /**
     * Paints the game components on the panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (gameEngine == null || gameEngine.isGameOver()) {
            return;
        }
        
        drawShip(g);
        drawBullets(g);
        drawAsteroids(g);
        drawPowerUps(g);
        drawLives(g);
    }
    
    /**
     * Draws the player ship.
     */
    private void drawShip(Graphics g) {
        Ship ship = gameEngine.getShip();
        
        // Draw shield if active
        if (gameEngine.isShieldActive()) {
            drawShield(g, ship);
        }
// Draw ship boundary for debugging
        
        // Create a copy of the graphics object for rotation
        Graphics2D g2d = (Graphics2D) g.create();

        ship.setDrawBounds(true); // Disable boundary drawing for ship
        ship.drawBoundary(g2d); 
        
        // Set up rotation transformation
        g2d.translate(ship.getX(), ship.getY());
        g2d.rotate(ship.getRotation());
        
        drawShipSprite(g2d, ship);
        
        // Draw thrust flame if thrusting
        if (ship.isThrusting()) {
            drawThrustFlame(g2d, ship);
        }
        
        g2d.dispose(); // Clean up the graphics object
    }
    
    /**
     * Draws the shield around the ship.
     */
    private void drawShield(Graphics g, Ship ship) {
        if (shieldImage != null) {
            // Draw shield bubble around ship
            int shieldSize = Math.max(ship.getWidth(), ship.getHeight()) * 2;
            g.drawImage(shieldImage, 
                       (int)ship.getX() - shieldSize/2, 
                       (int)ship.getY() - shieldSize/2,
                       shieldSize, shieldSize, null);
        } else {
            // Fallback to geometric shape if image not available
            g.setColor(new Color(100, 200, 255, 100)); // Semi-transparent light blue
            int shieldRadius = 40;
            g.fillOval((int)ship.getX() - shieldRadius, 
                      (int)ship.getY() - shieldRadius,
                      shieldRadius * 2, shieldRadius * 2);
        }
        
        // Draw shield energy bar
        drawShieldEnergyBar(g, ship);
    }
    
    /**
     * Draws the shield energy bar.
     */
    private void drawShieldEnergyBar(Graphics g, Ship ship) {
        double shieldPercentage = gameEngine.getShieldPercentage();
        g.setColor(new Color(0, 200, 255));
        g.fillRect((int)ship.getX() - ship.getWidth()/2, 
                  (int)ship.getY() - ship.getHeight()/2 - 10, 
                  (int)(ship.getWidth() * shieldPercentage), 5);
    }
    
    /**
     * Draws the ship sprite.
     */
    private void drawShipSprite(Graphics2D g2d, Ship ship) {
        if (shipImage != null) {
            // Apply an additional 90 degree rotation to correct the image orientation
            g2d.rotate(Math.PI / 2);
            g2d.drawImage(shipImage, 
                        -ship.getHeight()/2, // Swap width/height due to rotation
                        -ship.getWidth()/2, 
                        ship.getHeight(), 
                        ship.getWidth(), null);
        } else {
            // Draw a triangle for the ship if image not available
            g2d.setColor(PRIMARY_COLOR);
            int[] xPoints = {0, -ship.getWidth()/2, ship.getWidth()/2};
            int[] yPoints = {-ship.getHeight()/2, ship.getHeight()/2, ship.getHeight()/2};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }
    
    /**
     * Draws the thrust flame.
     */
    private void drawThrustFlame(Graphics2D g2d, Ship ship) {
        g2d.setColor(Color.ORANGE);
        int[] xFlame = {-ship.getWidth()/4, 0, ship.getWidth()/4};
        int[] yFlame = {ship.getHeight()/2, ship.getHeight()/2 + 10, ship.getHeight()/2};
        g2d.fillPolygon(xFlame, yFlame, 3);
    }
    
    /**
     * Draws all bullets.
     */
    private void drawBullets(Graphics g) {
        g.setColor(Color.YELLOW);
        List<Bullet> bullets = gameEngine.getBullets();
        for (Bullet bullet : bullets) {
            g.fillOval((int)bullet.getX() - bullet.getWidth()/2, 
                       (int)bullet.getY() - bullet.getHeight()/2, 
                       bullet.getWidth(), bullet.getHeight());
        }
    }
    
    /**
     * Draws all asteroids.
     */
    private void drawAsteroids(Graphics g) {
        List<Asteroid> asteroids = gameEngine.getAsteroids();
        for (Asteroid asteroid : asteroids) {
            drawAsteroid(g, asteroid);
        }
    }
    
    /**
     * Draws a single asteroid.
     */
    private void drawAsteroid(Graphics g, Asteroid asteroid) {
        if (asteroidImage != null) {
            // Use the asteroid image and scale based on size
            int size = asteroid.getSize();
            double scale = getAsteroidScale(size);
            
            g.drawImage(asteroidImage, 
                       (int)asteroid.getX(), 
                       (int)asteroid.getY(), 
                       (int)(asteroid.getWidth() * scale), 
                       (int)(asteroid.getHeight() * scale), null);
        } else {
            // Fallback to colored circles if image not available
            g.setColor(getAsteroidColor(asteroid.getSize()));
            g.fillOval((int)asteroid.getX(), (int)asteroid.getY(), 
                      asteroid.getWidth(), asteroid.getHeight());
        }
    }
    
    /**
     * Returns the scale for an asteroid based on its size.
     */
    private double getAsteroidScale(int size) {
        switch (size) {
            case 1: return 1.0; // Small
            case 2: return 1.3; // Medium
            case 3: return 1.6; // Large
            default: return 1.0;
        }
    }
    
    /**
     * Returns the color for an asteroid based on its size.
     */
    private Color getAsteroidColor(int size) {
        switch (size) {
            case 1: return new Color(255, 50, 50); // Small: bright red
            case 2: return new Color(220, 70, 70); // Medium: medium red
            case 3: return new Color(180, 90, 90); // Large: dark red
            default: return Color.RED;
        }
    }
    
    /**
     * Draws all power-ups.
     */
    private void drawPowerUps(Graphics g) {
        List<PowerUp> powerUps = gameEngine.getPowerUps();
        for (PowerUp powerUp : powerUps) {
            drawPowerUp(g, powerUp);
        }
    }
    
    /**
     * Draws a single power-up.
     */
    private void drawPowerUp(Graphics g, PowerUp powerUp) {
        if (powerUp.getType() == PowerUp.SHIELD) {
            if (shieldImage != null) {
                // Draw a smaller version of the shield image for power-ups
                g.drawImage(shieldImage, 
                           powerUp.getX(), powerUp.getY(), 
                           powerUp.getWidth(), powerUp.getHeight(), null);
            } else {
                // Fallback to blue diamond if image not available
                drawPowerUpDiamond(g, powerUp);
            }
        }
    }
    
    /**
     * Draws a diamond shape for power-ups.
     */
    private void drawPowerUpDiamond(Graphics g, PowerUp powerUp) {
        g.setColor(new Color(0, 191, 255)); // Deep Sky Blue
        int[] xPoints = {
            powerUp.getX() + powerUp.getWidth()/2,
            powerUp.getX() + powerUp.getWidth(),
            powerUp.getX() + powerUp.getWidth()/2,
            powerUp.getX()
        };
        int[] yPoints = {
            powerUp.getY(),
            powerUp.getY() + powerUp.getHeight()/2,
            powerUp.getY() + powerUp.getHeight(),
            powerUp.getY() + powerUp.getHeight()/2
        };
        g.fillPolygon(xPoints, yPoints, 4);
    }
    
    /**
     * Draws the player lives indicator.
     */
    private void drawLives(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        
        int lives = gameEngine.getGameState().getLives();
        if (heartIcon != null) {
            // Draw heart icons if available
            for (int i = 0; i < lives; i++) {
                g.drawImage(heartIcon.getImage(), 10 + i * 25, 10, 20, 20, null);
            }
        } else {
            // Fallback to text if icons not available
            g.drawString("Lives: " + lives, 10, 20);
        }
    }
    
    /**
     * Updates the score display.
     */
    public void updateScoreDisplay() {
        if (gameEngine != null) {
            scoreLabel.setText("SCORE: " + gameEngine.getScore());
        }
    }

    /**
     * Sets the game engine that will provide the game state for rendering.
     * @param engine The game engine
     */
    public void setGameEngine(GameEngine engine) {
        this.gameEngine = engine;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameEngine == null || gameEngine.isGameOver()) {
            return;
        }
        
        handleKeyPress(e.getKeyCode());
        repaint();
    }
    
    /**
     * Handles key press events.
     */
    private void handleKeyPress(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                gameEngine.rotateShipLeft();
                break;
            case KeyEvent.VK_RIGHT:
                gameEngine.rotateShipRight();
                break;
            case KeyEvent.VK_UP:
                gameEngine.thrustShip(true);
                break;
            case KeyEvent.VK_SPACE:
                gameEngine.fireBullet();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameEngine != null && !gameEngine.isGameOver()) {
            // Stop thrusting when UP key is released
        	if (e.getKeyCode() == KeyEvent.VK_UP) {
                gameEngine.thrustShip(false); // Ferma accelerazione
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
}