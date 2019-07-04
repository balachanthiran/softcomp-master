/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nasib
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class EnemyProcessing implements IEntityProcessingService {

    int life = 1;

    @Override
    public void process(GameData gameData, Map<String, Entity> world, Entity entity) {

        if (entity.getType().equals(ENEMY)) {
            // update entity     
            setShape(entity);

            life = entity.getLife();

            if (entity.getLife() == 0) {
                world.remove(entity.getID());

            }

        }

        if (life == 0) {
            Entity e = createEnemyShip();
            world.put(e.getID(), e);
            life = 1;
        }

    }

    private Entity createEnemyShip() {
        Entity enemyShip = new Entity();
        enemyShip.setType(EntityType.ENEMY);

        enemyShip.setRadius(7);
        enemyShip.setLife(1);

        enemyShip.setShapeX(new float[6]);
        enemyShip.setShapeY(new float[6]);

        return enemyShip;

    }

    private void setShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        float x = entity.getX();
        float y = entity.getY();

        shapex[0] = x - 10;
        shapey[0] = y;

        shapex[1] = x - 3;
        shapey[1] = y - 5;

        shapex[2] = x + 3;
        shapey[2] = y - 5;

        shapex[3] = x + 10;
        shapey[3] = y;

        shapex[4] = x + 3;
        shapey[4] = y + 5;

        shapex[5] = x - 3;
        shapey[5] = y + 5;

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }

}
