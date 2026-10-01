package asteroid.services;

import java.util.List;
import asteroid.model.game.Asteroid;

public class AsteroidService {
    public void updateAsteroidsSpeed(List<Asteroid> asteroids, double speedMultiplier) {
        for (Asteroid asteroid : asteroids) {
            double newSpeed = asteroid.getBaseSpeed() * speedMultiplier;
            asteroid.setSpeed(newSpeed);
        }
    }
}
