/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.asteroids;

import dk.sdu.mmmi.cbse.data.Entity;
import static dk.sdu.mmmi.cbse.data.EntityType.ASTEROIDS;
import dk.sdu.mmmi.cbse.data.GameData;
import dk.sdu.mmmi.cbse.services.IEntityProcessingService;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author nasib
 */
public class AsteroidsControlSystem implements IEntityProcessingService {

    Random rand = new Random();

    @Override
    public void process(GameData gameData, Map<String, Entity> world, Entity entity) {
        if (entity.getType().equals(ASTEROIDS)) {
            setShape(entity);
        }
    }

    private void setShape(Entity entity) {
        float[] shapex = entity.getShapeX();
        float[] shapey = entity.getShapeY();
        float x = entity.getX();
        float y = entity.getY();
        float size = entity.getSize();

            shapex[0] = x;
            shapey[0] = y;

            shapex[1] = x + (20 * size);
            shapey[1] = y + (15 * size);

            shapex[2] = x + (40 * size);
            shapey[2] = y;

            shapex[3] = x + (30 * size);
            shapey[3] = y - (20 * size);

            shapex[4] = x + (10 * size);
            shapey[4] = y - (20 * size);

            shapex[5] = x;
            shapey[5] = y;

        entity.setShapeX(shapex);
        entity.setShapeY(shapey);
    }

}
