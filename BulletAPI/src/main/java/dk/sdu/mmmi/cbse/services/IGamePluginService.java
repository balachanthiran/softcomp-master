package dk.sdu.mmmi.cbse.services;

import dk.sdu.mmmi.cbse.data.Entity;
import dk.sdu.mmmi.cbse.data.GameData;
import java.util.Map;

public interface IGamePluginService {
    /**
     * TODO: Describe the contract using pre- and post-conditions.
     * This interface class creates the game by getting the parameters, gameData and world
     * @param gameData
     * @param world
     * 
     * Pre-condition: The parameters in Start must be available
     * Post-condition: The game starts and Player, Enemy, Asteroids, and the world is created.
     */
    void start(GameData gameData, Map<String, Entity> world);

    /**
     * TODO: Describe the contract using pre- and post-conditions.
     *
     * @param gameData
     * 
     * Pre-condition: 
     * Post-condition:
     */
    void stop(GameData gameData);
}
