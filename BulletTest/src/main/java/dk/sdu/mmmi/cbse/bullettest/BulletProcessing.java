/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.bullettest;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BULLET;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMYBULLET;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameData;
import static dk.sdu.mmmi.cbse.common.data.GameKeys.SPACE;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;
import static java.lang.Math.sqrt;

/**
 *
 * @author nasib
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class BulletProcessing implements IEntityProcessingService {

    //Declarity services
    boolean playerDelay = false;
    boolean enemyDelay = false;
    long lastPressPlayer = 0;
    long lastPressEnemy = 0;

    @Override
    public void process(GameData gameData, Map<String, Entity> world, Entity entity) {
        playerShoot(gameData, world, entity);
        enemyShoot(gameData, world, entity);
    }

    private void wrap(GameData gameData, Map<String, Entity> world, Entity entity) {
        float x = entity.getX();
        float y = entity.getY();
        float dx = entity.getDx();
        float dy = entity.getDy();
        float dt = gameData.getDelta();
        // Screen wrap
        x += dx * dt;
        if (x > gameData.getDisplayWidth()) {
            world.remove(entity.getID());
        } else if (x < 0) {
            world.remove(entity.getID());
        }

        y += dy * dt;
        if (y > gameData.getDisplayHeight()) {
            world.remove(entity.getID());
        } else if (y < 0) {
            world.remove(entity.getID());
        }
    }

    private void playerShoot(GameData gameData, Map<String, Entity> world, Entity entity) {
        float x = entity.getX();
        float y = entity.getY();
        float radians = entity.getRadians();
        float dx = entity.getDx();
        float dy = entity.getDy();
        float dt = gameData.getDelta();
        int maxSpeed = 80;

        for (Entity entity1 : world.values()) {

            if (entity1.getType().equals(PLAYER)) {
                //delay by 500 milliseconds between every shot
                playerDelay = (System.currentTimeMillis() - lastPressPlayer) > 500;

                if (gameData.getKeys().isDown(SPACE)) {
                    if (playerDelay) {
                        //new entity
                        Entity bullet = new Entity();
                        //type BULLET
                        bullet.setType(BULLET);
                        //player's radians (which way the player looks)
                        bullet.setRadians(entity1.getRadians());
                        bullet.setMaxSpeed(maxSpeed);
                        bullet.setX(entity1.getX());
                        bullet.setY(entity1.getY());
                        bullet.setDx(entity1.getDx());
                        bullet.setDy(entity1.getDy());
                        bullet.setSize(2);
                        world.put(bullet.getID(), bullet);
                        lastPressPlayer = System.currentTimeMillis();
                    }
                }
            }

            if (entity.getType().equals(BULLET)) {
                //bullet speed
                dx += Math.cos(radians) * maxSpeed * dt;
                dy += Math.sin(radians) * maxSpeed * dt;

                x += dx * dt;
                y += dy * dt;
                
                //max speed for bullet
                float vec = (float) sqrt(dx * dx + dy * dy);
                if (vec > maxSpeed) {
                    dx = (dx / vec) * maxSpeed;
                    dy = (dy / vec) * maxSpeed;
                }

                entity.setPosition(x, y);
                entity.setDx(dx);
                entity.setDy(dy);
                entity.setRadians(radians);

                wrap(gameData, world, entity);
            }
        }
    }

    private void enemyShoot(GameData gameData, Map<String, Entity> world, Entity entity) {
        float x = entity.getX();
        float y = entity.getY();
        float radians = entity.getRadians();
        float dx = entity.getDx();
        float dy = entity.getDy();
        float dt = gameData.getDelta();
        int maxSpeed = 5;

        for (Entity entity1 : world.values()) {
            if (entity1.getType().equals(ENEMY)) {
                enemyDelay = (System.currentTimeMillis() - lastPressEnemy) > 1000;

                if (enemyDelay) {
                    Entity bullet = new Entity();
                    bullet.setType(ENEMYBULLET);
                    bullet.setRadians(entity1.getRadians());
                    bullet.setMaxSpeed(maxSpeed);
                    bullet.setX(entity1.getX());
                    bullet.setY(entity1.getY());
                    bullet.setDx(entity1.getDx());
                    bullet.setDy(entity1.getDy());
                    bullet.setSize(2);
                    world.put(bullet.getID(), bullet);
                    lastPressEnemy = System.currentTimeMillis();
                }

            }
            

            if (entity.getType().equals(ENEMYBULLET)) {
                dx += Math.cos(radians) * maxSpeed * dt;
                dy += Math.sin(radians) * maxSpeed * dt;

                x += dx * dt;
                y += dy * dt;
                float vec = (float) sqrt(dx * dx + dy * dy);
                if (vec > maxSpeed) {
                    dx = (dx / vec) * maxSpeed;
                    dy = (dy / vec) * maxSpeed;
                }

                entity.setPosition(x, y);
                entity.setDx(dx);
                entity.setDy(dy);
                entity.setRadians(radians);

                wrap(gameData, world, entity);
            }
        }
    }

}
