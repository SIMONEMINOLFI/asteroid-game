package asteroid.controller.game;

import java.util.List;
import asteroid.model.game.Asteroid;
import asteroid.services.AsteroidService;

public class AsteroidController {
    private AsteroidService asteroidService;
    
    public AsteroidController(AsteroidService asteroidService) {
        this.asteroidService = asteroidService;
    }
    
    // Update asteroids speed based on a multiplier
    public void updateAsteroidsSpeed(List<Asteroid> asteroids, double multiplier) {
        asteroidService.updateAsteroidsSpeed(asteroids, multiplier);
    }
}
