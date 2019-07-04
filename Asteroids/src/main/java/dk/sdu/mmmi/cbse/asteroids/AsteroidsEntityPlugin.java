/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.asteroids;

import dk.sdu.mmmi.cbse.data.Entity;
import dk.sdu.mmmi.cbse.data.EntityType;
import static dk.sdu.mmmi.cbse.data.EntityType.ASTEROIDS;
import dk.sdu.mmmi.cbse.data.GameData;
import dk.sdu.mmmi.cbse.services.IGamePluginService;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author nasib
 */
public class AsteroidsEntityPlugin implements IGamePluginService {

    private Map<String, Entity> world;
    private Entity asteroid;

    Random rand = new Random();

    @Override
    public void start(GameData gameData, Map<String, Entity> world) {
        this.world = world;
        //Add entities to the world
        for (int i = 0; i < 3; i++) {
            asteroid = createAsteroids(gameData);
            world.put(asteroid.getID(), asteroid);
        }

    }

    private Entity createAsteroids(GameData gameData) {
        Entity asteroids = new Entity();

        asteroids.setType(EntityType.ASTEROIDS);
        int randHeight = rand.nextInt(gameData.getDisplayHeight());
        int randWidth = rand.nextInt(gameData.getDisplayWidth());
        int randRadians = rand.nextInt((int) (2 * 3.1415f));
        int randSize = rand.nextInt(4 - 1) + 1;

        switch (randSize) {
            case 1:
                asteroids.setMaxSpeed(30);
                break;
            case 2:
                asteroids.setMaxSpeed(20);
                break;
            case 3:
                asteroids.setMaxSpeed(10);
                break;
            default:
                break;
        }
        asteroids.setPosition(randHeight, randWidth);
        asteroids.setRadians(randRadians);
        asteroids.setSize(randSize);

        asteroids.setShapeX(new float[6]);
        asteroids.setShapeY(new float[6]);

        return asteroids;

    }

    @Override
    public void stop(GameData gameData) {
        //Remove entities
        for (Entity e : world.values()) {
            if (e.getType().equals(ASTEROIDS)) {
                world.remove(asteroid.getID());
            }
        }
    }

}
