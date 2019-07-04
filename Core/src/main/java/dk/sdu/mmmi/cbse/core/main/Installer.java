package dk.sdu.mmmi.cbse.core.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dk.sdu.mmmi.cbse.services.IGamePluginService;
import dk.sdu.mmmi.cbse.data.Entity;
import dk.sdu.mmmi.cbse.data.GameData;
import dk.sdu.mmmi.cbse.services.IEntityProcessingService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

public class Installer extends ModuleInstall {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private final dk.sdu.mmmi.cbse.data.GameData gameData = new dk.sdu.mmmi.cbse.data.GameData();
    private List<dk.sdu.mmmi.cbse.services.IEntityProcessingService> entityProcessors = new ArrayList<>();
    private Map<String, dk.sdu.mmmi.cbse.data.Entity> world = new ConcurrentHashMap<>();
    private List<IGamePluginService> gamePlugins;

    @Override
    public void restored() {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Asteroids";
        cfg.width = 800;
        cfg.height = 600;
        cfg.useGL30 = false;
        cfg.resizable = false;

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("Looking for service");
                IEntityProcessingService service = Lookup.getDefault().lookup(IEntityProcessingService.class);
                if (service != null) {
                    for (Entity e : world.values()) {
                        service.process(gameData, world, e);
                    }
                } else {
                    System.out.println("No service found");
                }
            }
        }, 5000, 5000, TimeUnit.MILLISECONDS);

        new LwjglApplication(new Game(), cfg);
    }

    /*@Override
    public void restored()
    {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("Looking for service");
                IEntityProcessingService service = Lookup.getDefault().lookup(IEntityProcessingService.class);
                if(service != null)
                {
                    
                    service.process(gameData, world, entity);
                }
                else
                {
                    System.out.println("No service found");
                }
            }
        }, 5000, 5000, TimeUnit.MILLISECONDS);
    }*/
}
