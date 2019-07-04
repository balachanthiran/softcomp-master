package dk.sdu.mmmi.cbse.playerspring;

import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@ServiceProvider (service = IGamePluginService.class)
public class PlayerEntityPlugin implements IGamePluginService {

    private Map<String, Entity> world;
    private Entity player;

    public PlayerEntityPlugin() {
    }

    @Override
    public void start(GameData gameData, Map<String, Entity> world) {
        this.world = world;
        // Add entities to the world
        ApplicationContext context = new ClassPathXmlApplicationContext("Beans.xml");
        
        PlayerWrapper.reference = (PlayerControlSystem)context.getBean("playerProcess");
        
        player = createPlayerShip(gameData);
        world.put(player.getID(), player);
    }

    private Entity createPlayerShip(GameData gameData) {
        Entity playerShip = new Entity();
        playerShip.setType(PLAYER);

        playerShip.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);

        playerShip.setMaxSpeed(300);
        playerShip.setAcceleration(200);
        playerShip.setDeacceleration(10);

        playerShip.setShapeX(new float[4]);
        playerShip.setShapeY(new float[4]);

        playerShip.setRadians(3.1415f / 2);
        playerShip.setRotationSpeed(5);
        
        playerShip.setLife(3);
        
        playerShip.setRadius(7);
        
        return playerShip;
    }

    @Override
    public void stop(GameData gameData) {
        // Remove entities
        world.remove(player.getID());
    }

}
