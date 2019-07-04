/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.playerspring;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nasib
 */
@ServiceProvider (service = IEntityProcessingService.class)
public class PlayerWrapper implements IEntityProcessingService {
    
    public static PlayerControlSystem reference;

    @Override
    public void process(GameData gameData, Map<String, Entity> world, Entity entity) {
        if(reference != null){
            reference.process(gameData, world, entity);
        }
    }
    
}
