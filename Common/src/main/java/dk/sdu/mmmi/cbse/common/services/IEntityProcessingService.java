package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import java.util.Map;

public interface IEntityProcessingService {
    /**
     * TODO: Describe the contract using pre- and post-conditions.
     * This interface class does all the calculation, like for example drawing
     * shape, moving the player etc.
     * @param gameData
     * @param world
     * @param entity
     * 
     * Pre-condition: The process method must have the parameters available
     * Post-condition: The process has executed the parameters
     */
    void process(GameData gameData, Map<String, Entity> world, Entity entity);
}
