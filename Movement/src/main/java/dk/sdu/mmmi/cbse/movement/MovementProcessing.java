/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.movement;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ASTEROIDS;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import static dk.sdu.mmmi.cbse.common.data.EntityType.MAP;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.LEFT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.RIGHT;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.SPACE;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.UP;
import dk.sdu.mmmi.cbse.common.events.Event;
import dk.sdu.mmmi.cbse.common.events.EventType;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import java.util.Map;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nasib
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class MovementProcessing implements IEntityProcessingService {

    Random rand = new Random();

    @Override
    public void process(GameData gameData, Map<String, Entity> world, Entity entity) {
        if (entity.getType().equals(PLAYER)) {
            // Update entity
            playerMovement(gameData, entity);
            wrap(gameData, entity);
        }

        if (entity.getType().equals(ENEMY)) {
            // Update entity
            enemyMovement(gameData, entity);
            wrap(gameData, entity);
        }

        if (entity.getType().equals(ASTEROIDS) || entity.getType().equals(MAP)) {
            // Update entity
            asteroidsMovement(gameData, entity);
            wrap(gameData, entity);
        }

    }

    public void playerMovement(GameData gameData, Entity entity) {
        float dt = gameData.getDelta();
        float dx = entity.getDx();
        float dy = entity.getDy();
        float acceleration = entity.getAcceleration();
        float deceleration = entity.getDeacceleration();
        float maxSpeed = entity.getMaxSpeed();
        float radians = entity.getRadians();
        float rotationSpeed = entity.getRotationSpeed();
        // turning
        if (gameData.getKeys().isDown(LEFT)) {
            radians += rotationSpeed * dt;
        }

        if (gameData.getKeys().isDown(RIGHT)) {
            radians -= rotationSpeed * dt;
        }
        if (gameData.getKeys().isDown(SPACE)) {
            gameData.addEvent(new Event(EventType.PLAYER_SHOOT));
        }
        // accelerating            
        if (gameData.getKeys().isDown(UP)) {
            dx += cos(radians) * acceleration * dt;
            dy += sin(radians) * acceleration * dt;
        }

        // deceleration
        float vec = (float) sqrt(dx * dx + dy * dy);
        if (vec > 0) {
            dx -= (dx / vec) * deceleration * dt;
            dy -= (dy / vec) * deceleration * dt;
        }
        if (vec > maxSpeed) {
            dx = (dx / vec) * maxSpeed;
            dy = (dy / vec) * maxSpeed;
        }

        entity.setDx(dx);
        entity.setDy(dy);
        entity.setRadians(radians);

    }

    private void enemyMovement(GameData gameData, Entity entity) {
        float x = entity.getX();
        float y = entity.getY();
        float dx = entity.getDx();
        float dy = entity.getDy();
        float radians = entity.getRadians();
        float maxSpeed = entity.getMaxSpeed();
        float rotationSpeed = entity.getRotationSpeed();
        int r = rand.nextInt(2000);

        // accelerating            
        x += cos(radians) * maxSpeed;
        y += sin(radians) * maxSpeed;

        //right
        if (r >= 0 && r < 50) {
            radians -= rotationSpeed;
        }
        //left
        if (r >= 100 && r < 100) {
            radians += rotationSpeed;
        }

        //update entity
        entity.setDx(dx);
        entity.setDy(dy);
        entity.setRadians(radians);
        entity.setMaxSpeed(3);
        entity.setRotationSpeed(5);
        entity.setPosition(x, y);
    }

    private void asteroidsMovement(GameData gameData, Entity entity) {
        float x = entity.getX();
        float y = entity.getY();
        float dx = entity.getDx();
        float dy = entity.getDy();
        float dt = gameData.getDelta();
        float radians = entity.getRadians();
        float speed = entity.getMaxSpeed();

        x += dx * dt;
        y += dy * dt;

        dx = (float) (Math.cos(radians / 4) * speed);
        dy = (float) (Math.sin(radians / 4) * speed);

        entity.setPosition(x, y);
        entity.setRadians(radians);
        entity.setDx(dx);
        entity.setDy(dy);
    }

    private void wrap(GameData gameData, Entity entity) {
        float x = entity.getX();
        float y = entity.getY();
        float dt = gameData.getDelta();
        float dx = entity.getDx();
        float dy = entity.getDy();

        // Screen wrap
        x += dx * dt;
        if (x > gameData.getDisplayWidth()) {
            x = 0;
        } else if (x < 0) {
            x = gameData.getDisplayWidth();
        }

        y += dy * dt;
        if (y > gameData.getDisplayHeight()) {
            y = 0;
        } else if (y < 0) {
            y = gameData.getDisplayHeight();
        }
        entity.setDx(dx);
        entity.setDy(dy);
        entity.setPosition(x, y);

    }

}
