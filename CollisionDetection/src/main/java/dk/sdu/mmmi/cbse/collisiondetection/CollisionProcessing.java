/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.collisiondetection;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ASTEROIDS;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BULLET;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMYBULLET;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nasib
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class CollisionProcessing implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, Map<String, Entity> world, Entity entity) {
        //checks for entity1 and entity2 in the world
        for (Entity entity1 : world.values()) {
            for (Entity entity2 : world.values()) {
                if (entity1.getType().equals(ASTEROIDS) && entity2.getType().equals(PLAYER)) {
                    if (collisionOverlap(entity2, entity1)) {
                        world.remove(entity1.getID());
                        //respawn
                        entity2.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
                        entity2.setDx(0);
                        entity2.setDy(0);
                        entity2.setRadians(3.1415f / 2);
                        //the player has been hit
                        entity2.setIsHit(true);
                    }
                }
                if (entity1.getType().equals(BULLET) && entity2.getType().equals(ASTEROIDS)) {
                    if (collisionOverlap(entity1, entity2)) {
                        world.remove(entity1.getID());
                        world.remove(entity2.getID());
                    }
                }
                if (entity1.getType().equals(BULLET) && entity2.getType().equals(ENEMY)) {
                    if (collisionOverlap(entity1, entity2)) {
                        world.remove(entity1.getID());
                        entity2.setLife(0);
                    }
                }
                if (entity1.getType().equals(ENEMYBULLET) && entity2.getType().equals(PLAYER)) {
                    if (collisionOverlap(entity1, entity2)) {
                        world.remove(entity1.getID());
                        entity2.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
                        entity2.setDx(0);
                        entity2.setDy(0);
                        entity2.setRadians(3.1415f / 2);
                        entity2.setIsHit(true);
                    }

                }
                if (entity1.getType().equals(ENEMY) && entity2.getType().equals(PLAYER)) {
                    if (collisionOverlap(entity1, entity2)) {
                        entity2.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
                        entity2.setDx(0);
                        entity2.setDy(0);
                        entity2.setRadians(3.1415f / 2);
                        entity2.setIsHit(true);
                    }

                }

            }

        }

    }

    private boolean collisionOverlap(Entity entity1, Entity entity2) {
        float dx;
        float dy;
        float entity1Radius;
        float entity2Radius;

        if (entity2.getType().equals(ASTEROIDS)) {
            //checks the different sizes of the asteroids (3 sizes)
            switch ((int) entity2.getSize()) {
                case 1:
                    dx = (entity1.getX() + entity1.getRadius() / 2) - (entity2.getX() + 10);
                    dy = (entity1.getY() + entity1.getRadius() / 2) - (entity2.getY());
                    entity1Radius = entity1.getRadius();
                    entity2Radius = 20;
                    break;
                case 2:
                    dx = (entity1.getX() + entity1.getRadius() / 2) - (entity2.getX() + 20);
                    dy = (entity1.getY() + entity1.getRadius() / 2) - (entity2.getY());
                    entity1Radius = entity1.getRadius();
                    entity2Radius = 40;
                    break;
                default:
                    dx = (entity1.getX() + entity1.getRadius() / 2) - (entity2.getX() + 30);
                    dy = (entity1.getY() + entity1.getRadius() / 2) - (entity2.getY());
                    entity1Radius = entity1.getRadius();
                    entity2Radius = 60;
                    break;
            }

        } else {
            //checks for the rest of the entities radius
            dx = (entity1.getX() + entity1.getRadius() / 2) - (entity2.getX() + entity2.getRadius() / 2);
            dy = (entity1.getY() + entity1.getRadius() / 2) - (entity2.getY() + entity2.getRadius() / 2);
            entity1Radius = entity1.getRadius();
            entity2Radius = entity2.getRadius();
        }

        return Math.sqrt((dx * dx) + (dy * dy)) <= (entity1Radius + entity2Radius);

    }

}
