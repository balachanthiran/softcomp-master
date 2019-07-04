/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.health;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.HEALTH;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nasib
 */
@ServiceProvider (service = IEntityProcessingService.class)
public class HealthProcessing implements IEntityProcessingService {
    
    @Override
    public void process(GameData gameData, Map<String, Entity> world, Entity entity) {

        for (Entity entity1 : world.values()) {
            for (Entity entity2 : world.values()) {
                health(entity1, entity2, world);

            }
        }
        if (entity.getType().equals(HEALTH)) {
            setShape(entity);

        }

    }

    private void health(Entity entity1, Entity entity2, Map<String, Entity> world) {

        if (entity1.getType().equals(PLAYER) && entity2.getType().equals(HEALTH)) {
            // if the player's health is less or equal to 3 and player is hit, remove one health from the world
            if (entity1.getLife() <= 3 && entity1.getIsHit()) {
                entity1.setIsHit(false);
                entity1.setLife(entity1.getLife() - 1);
                world.remove(entity2.getID());
            }

        }
    }

    private void setShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        float x = entity.getX();
        float y = entity.getY();
        float radians = entity.getRadians();

        shapex[0] = (float) (x + Math.cos(radians) * 8);
        shapey[0] = (float) (y + Math.sin(radians) * 8);

        shapex[1] = (float) (x + Math.cos(radians - 4 * 3.1415f / 5) * 8);
        shapey[1] = (float) (y + Math.sin(radians - 4 * 3.1145f / 5) * 8);

        shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * 5);
        shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * 5);

        shapex[3] = (float) (x + Math.cos(radians + 4 * 3.1415f / 5) * 8);
        shapey[3] = (float) (y + Math.sin(radians + 4 * 3.1415f / 5) * 8);

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }
}
