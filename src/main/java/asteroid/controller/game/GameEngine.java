package asteroid.controller.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import asteroid.model.GameState;
import asteroid.model.game.Asteroid;
import asteroid.model.game.Bullet;
import asteroid.model.game.PowerUp;
import asteroid.model.game.Ship;
import asteroid.services.AudioService;
import asteroid.services.GameService;
import asteroid.services.ScoreService;

/**
 * Core game engine that handles game logic and processing.
 * This is part of the Controller layer in MVC architecture.
 */
public class GameEngine implements ActionListener {
    // Game components
    private Timer timer;
    private Ship ship;
    private List<Asteroid> asteroids = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    private Random random = new Random();
    private boolean gameOver = false;
    private Runnable onGameOver;
    
    // Game state and services
    private GameState gameState;
    private GameService gameService;
    private ScoreService scoreService = new ScoreService();
    // Use the singleton instance of AudioService
    private AudioService audioService = AudioService.getInstance();
    
    // Shield status
    private boolean shieldActive = false;
    private long shieldActivationTime = 0;
    private static final long SHIELD_DURATION = 5000; // 5 seconds
    
    // Power-up spawning
    private int asteroidsDestroyedSinceLastPowerUp = 0;
    private long lastPowerUpTime = 0;
    private static final long POWER_UP_COOLDOWN = 15000; // 15 seconds
    private static final int ASTEROIDS_FOR_POWER_UP = 5;
    
    // Game rendering callback
    private Runnable updateViewCallback;
    
    // Constants
    private static final int PANEL_WIDTH = 500;
    private static final int PANEL_HEIGHT = 500;
    private static final int SHIP_WIDTH = 50;
    private static final int SHIP_HEIGHT = 20;
    
    /**
     * Creates a new game engine.
     */
    public GameEngine(GameState gameState, GameService gameService, Runnable updateViewCallback) {
        this.gameState = gameState;
        this.gameService = gameService;
        this.updateViewCallback = updateViewCallback;
        
        // Initialize ship in the center of the screen
        ship = new Ship(PANEL_WIDTH / 2, PANEL_HEIGHT / 2);
        
        // Start the game loop timer (updates game 20 times per second)
        timer = new Timer(50, this); // a tick every 50ms

        // Set initial audio state from the singleton instance
        this.audioService.setAudioEnabled(AudioService.getInstance().isAudioEnabled());
    }
    
    /**
     * Starts the game engine.
     */
    public void start() {
        timer.start();
    }
    
    /**
     * Stops the game engine.
     */
    public void stop() {
        timer.stop();
    }
    
    /**
     * The main game loop - updates game state on each tick.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;
        
        // Update ship position with physics
        ship.update(PANEL_WIDTH, PANEL_HEIGHT);
        
        // Update game elements
        updateBullets();
        updateAsteroids();
        updatePowerUps();
        updateShieldStatus();
        
        // Spawn new game elements
        if (random.nextInt(20) == 0) {  // decide spawn rate  20 means 5% chance every tick
            spawnAsteroid();
        }
        checkPowerUpSpawn();
        
        // Update game speed based on score milestones
        if (gameState.getScore() > 0 && gameState.getScore() % 500 == 0) {
            gameState.setGameSpeed(gameState.getGameSpeed() + 0.1);
        }

        // Update the view
        if (updateViewCallback != null) {
            updateViewCallback.run();
        }
    }
    
    /**
     * Updates bullet positions and checks for collisions with asteroids.
     */
    private void updateBullets() {
        // Use a separate list for new asteroids to avoid concurrent modification
        List<Asteroid> newAsteroids = new ArrayList<>();
        
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            
            // Update bullet position and check if it should be removed
            if (!bullet.update(PANEL_WIDTH, PANEL_HEIGHT)) {
                bulletIterator.remove();
                continue;
            }
            
            // Check collisions with asteroids
            boolean hitAsteroid = false;
            Iterator<Asteroid> asteroidIterator = asteroids.iterator();
            while (asteroidIterator.hasNext() && !hitAsteroid) {
                Asteroid asteroid = asteroidIterator.next();
                
                if (bullet.getBounds().intersects(asteroid.getBounds())) {
                    // Award points based on asteroid size
                    int points = scoreService.getAsteroidPoints(asteroid.getSize());
                    gameState.setScore(gameState.getScore() + points);
                    
                    // Play sound effect
                    audioService.playExplosionSound();
                    
                    // Track destruction for power-up spawning
                    asteroidsDestroyedSinceLastPowerUp++;
                    
                    // Split asteroid into smaller pieces
                    createSmallerAsteroids(asteroid, newAsteroids);
                    
                    // Remove the original asteroid and bullet
                    asteroidIterator.remove();
                    bulletIterator.remove();
                    hitAsteroid = true;
                }
            }
        }
        
        // Add the new asteroids after iteration is complete
        asteroids.addAll(newAsteroids);
    }
    
    /**
     * Creates smaller asteroids when a larger one is destroyed.
     */
    private void createSmallerAsteroids(Asteroid asteroid, List<Asteroid> newAsteroids) {
        // Large asteroids split into medium, medium into small
        if (asteroid.getSize() == 3) { // Large
            for (int i = 0; i < 2; i++) {
                int offsetX = (i == 0) ? -20 : 20; 
                Asteroid mediumAsteroid = new Asteroid(
                    (int)asteroid.getX() + offsetX,
                    (int)asteroid.getY(),
                    35, 35, 2); // Medium size
                
                // Set direction with some randomness
                double angle = Math.random() * Math.PI * 2; // Random direction
                double speed = mediumAsteroid.getBaseSpeed() * gameState.getGameSpeed();
                mediumAsteroid.setDirectionX(Math.cos(angle) * speed);
                mediumAsteroid.setDirectionY(Math.sin(angle) * speed);
                
                newAsteroids.add(mediumAsteroid);
            }
        } else if (asteroid.getSize() == 2) { // Medium
            for (int i = 0; i < 2; i++) {
                int offsetX = (i == 0) ? -15 : 15;
                Asteroid smallAsteroid = new Asteroid(
                    (int)asteroid.getX() + offsetX,
                    (int)asteroid.getY(),
                    20, 20, 1); // Small size
                
                // Set direction with some randomness
                double angle = Math.random() * Math.PI * 2; // Random direction
                double speed = smallAsteroid.getBaseSpeed() * gameState.getGameSpeed();
                smallAsteroid.setDirectionX(Math.cos(angle) * speed);
                smallAsteroid.setDirectionY(Math.sin(angle) * speed);
                
                newAsteroids.add(smallAsteroid);
            }
        }
    }
    
    /**
     * Updates asteroid positions and checks for collisions with the ship.
     */
    private void updateAsteroids() {
        Iterator<Asteroid> iterator = asteroids.iterator();
        while (iterator.hasNext()) {
            Asteroid asteroid = iterator.next();
            asteroid.move();
            
            // Check collision with ship (only if shield is not active)
            if (!shieldActive && asteroid.getBounds().intersects(ship.getBounds())) {
                // Play sound effect
                audioService.playLoseLifeSound();
                
                // Decrement lives
                gameState.setLives(gameState.getLives() - 1);
                iterator.remove();
                
                // Check if game over
                if (gameState.getLives() <= 0) {
                    endGame();
                }
                continue;
            }
            
            // Remove asteroids that have left the screen
            if (asteroid.isOffScreen(PANEL_WIDTH, PANEL_HEIGHT)) {
                iterator.remove();
            }
        }
    }
    
    /**
     * Updates power-up positions and checks for collisions with the ship.
     */
    private void updatePowerUps() {
        Iterator<PowerUp> iterator = powerUps.iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.move();
            
            // Check collision with ship
            if (powerUp.getBounds().intersects(ship.getBounds())) {
                // Activate power-up effect
                if (powerUp.getType() == PowerUp.SHIELD) {
                    activateShield();
                    audioService.playPowerupSound();
                }
                
                iterator.remove();
                continue;
            }
            
            // Remove power-ups that have left the screen
            if (powerUp.isOffScreen(PANEL_HEIGHT)) {
                iterator.remove();
            }
        }
    }
    
    /**
     * Updates the shield status and deactivates it when the duration expires.
     */
    private void updateShieldStatus() {
        if (shieldActive) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - shieldActivationTime > SHIELD_DURATION) {
                shieldActive = false;
            }
        }
    }
    
    /**
     * Activates the shield power-up.
     */
    private void activateShield() {
        shieldActive = true;
        shieldActivationTime = System.currentTimeMillis();
    }
    
    /**
     * Checks if conditions are met to spawn a power-up.
     */
    private void checkPowerUpSpawn() {
        long currentTime = System.currentTimeMillis();
        
        // Check if enough asteroids destroyed and cooldown passed
        if (asteroidsDestroyedSinceLastPowerUp >= ASTEROIDS_FOR_POWER_UP && 
            currentTime - lastPowerUpTime > POWER_UP_COOLDOWN) {
            
            // Spawn power-up
            int x = random.nextInt(PANEL_WIDTH - 30);
            PowerUp powerUp = new PowerUp(x, 0, PowerUp.SHIELD);
            powerUps.add(powerUp);
            
            // Reset counter and cooldown
            asteroidsDestroyedSinceLastPowerUp = 0;
            lastPowerUpTime = currentTime;
        }
    }
    
    /**
     * Spawns a new asteroid with random properties from any edge of the screen.
     */
    private void spawnAsteroid() {
        // Determine which edge the asteroid will spawn from
        int edge = random.nextInt(4); // 0: top, 1: right, 2: bottom, 3: left
        int x = 0, y = 0;
        
        // Generate position based on the chosen edge
        switch (edge) {
            case 0: // Top edge
                x = random.nextInt(PANEL_WIDTH);
                y = -50;
                break;
            case 1: // Right edge
                x = PANEL_WIDTH + 50;
                y = random.nextInt(PANEL_HEIGHT);
                break;
            case 2: // Bottom edge
                x = random.nextInt(PANEL_WIDTH);
                y = PANEL_HEIGHT + 50;
                break;
            case 3: // Left edge
                x = -50;
                y = random.nextInt(PANEL_HEIGHT);
                break;
        }
        
        // Randomly determine asteroid size
        int size = random.nextInt(3) + 1; // 1=small, 2=medium, 3=large
        
        // Size affects visual dimensions
        int width, height;
        switch (size) {
            case 1: width = height = 20; break; // Small
            case 2: width = height = 35; break; // Medium
            case 3: width = height = 50; break; // Large
            default: width = height = 30;
        }
        
        // Create the asteroid
        Asteroid newAsteroid = new Asteroid(x, y, width, height, size);
        
        // Set movement direction toward center of screen
        double centerX = PANEL_WIDTH / 2;
        double centerY = PANEL_HEIGHT / 2;
        double angle = Math.atan2(centerY - y, centerX - x);
        
        // Add some randomness to the direction
        angle += (random.nextDouble() - 0.5) * Math.PI/2;
        
        // Set the speed and direction
        double baseSpeed = newAsteroid.getBaseSpeed() * gameState.getGameSpeed();
        newAsteroid.setDirectionX(Math.cos(angle) * baseSpeed);
        newAsteroid.setDirectionY(Math.sin(angle) * baseSpeed);
        
        asteroids.add(newAsteroid);
    }
    
    /**
     * Fires a bullet from the ship's position in the direction it's facing.
     */
    public void fireBullet() {
        // Get the position at the nose of the ship
        double[] nosePos = ship.getNoseCoordinates();
        
        // Create bullet from nose of ship with ship's rotation
        Bullet bullet = new Bullet(nosePos[0], nosePos[1], ship.getRotation());
        bullets.add(bullet);
        
        // Play shoot sound
        audioService.playShootSound();
    }
    
    /**
     * Rotates the ship left (counterclockwise).
     */
    public void rotateShipLeft() {
        ship.rotateLeft();
    }
    
    /**
     * Rotates the ship right (clockwise).
     */
    public void rotateShipRight() {
        ship.rotateRight();
    }
    
    /**
     * Applies thrust to accelerate the ship.
     */
    public void thrustShip(boolean thrusting) {
        ship.setThrusting(thrusting);
    }
    
    /**
     * Moves the ship left if possible.
     */
    public void moveShipLeft() {
        if (ship.getX() > 0) {
            ship.setX(ship.getX() - 20);
        }
    }
    
    /**
     * Moves the ship right if possible.
     */
    public void moveShipRight() {
        if (ship.getX() < PANEL_WIDTH - SHIP_WIDTH) {
            ship.setX(ship.getX() + 20);
        }
    }
    
    /**
     * Ends the game and triggers the game over callback.
     */
    private void endGame() {
        gameOver = true;
        timer.stop();
        
        // Play game over sound
        audioService.playGameOverSound();
        
        // Trigger game over callback
        if (onGameOver != null) {
            onGameOver.run();
        }
    }
    
    /**
     * Gets the current game score.
     * @return The current score
     */
    public int getScore() {
        return gameState.getScore();
    }
    
    /**
     * Gets the high score.
     * @return The high score
     */
    public int getHighScore() {
        return scoreService.getHighScore();
    }
    
    /**
     * Gets the ship model.
     * @return The ship
     */
    public Ship getShip() {
        return ship;
    }
    
    /**
     * Gets the list of asteroids.
     * @return The asteroids
     */
    public List<Asteroid> getAsteroids() {
        return asteroids;
    }
    
    /**
     * Gets the list of bullets.
     * @return The bullets
     */
    public List<Bullet> getBullets() {
        return bullets;
    }
    
    /**
     * Gets the list of power-ups.
     * @return The power-ups
     */
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
    
    /**
     * Checks if shield is active.
     * @return true if shield is active, false otherwise
     */
    public boolean isShieldActive() {
        return shieldActive;
    }
    
    /**
     * Gets the remaining shield duration as a percentage.
     * @return The remaining shield percentage (0.0 to 1.0)
     */
    public double getShieldPercentage() {
        if (!shieldActive) return 0.0;
        long elapsed = System.currentTimeMillis() - shieldActivationTime;
        return Math.max(0.0, 1.0 - (double)elapsed / SHIELD_DURATION);
    }
    
    /**
     * Gets the game state.
     * @return The game state
     */
    public GameState getGameState() {
        return gameState;
    }
    
    /**
     * Gets the audio service.
     * @return The audio service
     */
    public AudioService getAudioService() {
        // Return the singleton instance
        return audioService;
    }
    
    /**
     * Sets whether audio is enabled.
     * @param enabled true to enable audio, false to disable
     */
    public void setAudioEnabled(boolean enabled) {
        // Set audio state on the singleton instance
        audioService.setAudioEnabled(enabled);
    }
    
    /**
     * Checks if the game is over.
     * @return true if game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Sets the callback to be executed when the game ends.
     */
    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }
}
