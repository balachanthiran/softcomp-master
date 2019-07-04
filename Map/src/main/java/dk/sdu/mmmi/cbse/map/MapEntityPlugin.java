/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.map;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.EntityType;
import static dk.sdu.mmmi.cbse.common.data.EntityType.MAP;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Map;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nasib
 */
@ServiceProvider(service = IGamePluginService.class)
public class MapEntityPlugin implements IGamePluginService {

    Random rand = new Random();

    private Map<String, Entity> world;
    private Entity map;

    @Override
    public void start(GameData gameData, Map<String, Entity> world) {
        this.world = world;
        for (int i = 0; i < 50; i++) {
            map = createMap(gameData);
            world.put(map.getID(), map);
        }

    }

    private Entity createMap(GameData gameData) {
        Entity mapEnt = new Entity();

        int randWidth = rand.nextInt(gameData.getDisplayWidth());
        int randHeight = rand.nextInt(gameData.getDisplayHeight());

        mapEnt.setType(EntityType.MAP);
        mapEnt.setPosition(randWidth, randHeight);
        mapEnt.setMaxSpeed(5);
        mapEnt.setRadians(3.1415f);

        return mapEnt;
    }

    @Override
    public void stop(GameData gameData) {
        for (Entity e : world.values()) {

            if (e.getType().equals(MAP)) {
                world.remove(e.getID());

            }
        }

    }
}
