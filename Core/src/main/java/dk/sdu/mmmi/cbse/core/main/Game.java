package dk.sdu.mmmi.cbse.core.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import dk.sdu.mmmi.cbse.common.data.Entity;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ASTEROIDS;
import static dk.sdu.mmmi.cbse.common.data.EntityType.BULLET;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMY;
import static dk.sdu.mmmi.cbse.common.data.EntityType.ENEMYBULLET;
import static dk.sdu.mmmi.cbse.common.data.EntityType.MAP;
import static dk.sdu.mmmi.cbse.common.data.EntityType.PLAYER;
import static dk.sdu.mmmi.cbse.common.data.EntityType.HEALTH;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.core.managers.GameInputProcessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JOptionPane;

public class Game implements ApplicationListener {

    private static OrthographicCamera cam;
    private ShapeRenderer sr;
    private final Lookup lookup = Lookup.getDefault();
    private final GameData gameData = new GameData();
    private List<IEntityProcessingService> entityProcessors = new ArrayList<>();
    private Map<String, Entity> world = new ConcurrentHashMap<>();
    private List<IGamePluginService> gamePlugins;
    private int playerLife = 3;

    @Override
    public void create() {
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        sr = new ShapeRenderer();

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));

        Lookup.Result<IGamePluginService> result = lookup.lookupResult(IGamePluginService.class);
        result.addLookupListener(lookupListener);
        gamePlugins = new ArrayList<>(result.allInstances());
        result.allItems();

        for (IGamePluginService plugin : gamePlugins) {
            plugin.start(gameData, world);
        }
    }

    @Override
    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());
        gameData.getKeys().update();

        update();
        draw();
    }

    private void update() {
        // Update
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            for (Entity e : world.values()) {
                entityProcessorService.process(gameData, world, e);
            }
        }
        
        if(playerLife == 0){
            JOptionPane.showMessageDialog(null, "GAME OVER");
            System.exit(0);
        }
    }

    private void draw() {
        for (Entity entity : world.values()) {
            if (entity.getType().equals(PLAYER)) {
                playerLife = entity.getLife();
                sr.setColor(1, 1, 1, 1);

                sr.begin(ShapeRenderer.ShapeType.Line);

                float[] shapex = entity.getShapeX();
                float[] shapey = entity.getShapeY();

                for (int i = 0, j = shapex.length - 1;
                        i < shapex.length;
                        j = i++) {

                    sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
                }

                sr.end();
            }
            if (entity.getType().equals(HEALTH)) {
                sr.setColor(0, 1, 0, 1);

                sr.begin(ShapeRenderer.ShapeType.Line);
                for (int x = 0; x < playerLife; x++) {
                    float[] shapex = entity.getShapeX();
                    float[] shapey = entity.getShapeY();

                    for (int i = 0, j = shapex.length - 1;
                            i < shapex.length;
                            j = i++) {

                        sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
                    }
                }

                sr.end();
            }

            if (entity.getType().equals(BULLET) || entity.getType().equals(ENEMYBULLET)) {
                sr.setColor(0, 1, 1, 1);
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.circle(entity.getX() - entity.getRadius() / 2, entity.getY() + entity.getRadius() / 2, 2);
                sr.end();

            }
            if (entity.getType().equals(ENEMY)) {
                sr.setColor(1, 0, 0, 1);

                sr.begin(ShapeRenderer.ShapeType.Line);
                float[] shapex = entity.getShapeX();
                float[] shapey = entity.getShapeY();

                for (int i = 0, j = shapex.length - 1;
                        i < shapex.length;
                        j = i++) {

                    sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
                }

                sr.end();
            }
            if (entity.getType().equals(ASTEROIDS)) {
                sr.setColor(1, 1, 1, 1);

                sr.begin(ShapeRenderer.ShapeType.Line);

                float[] shapex = entity.getShapeX();
                float[] shapey = entity.getShapeY();

                for (int i = 0, j = shapex.length - 1;
                        i < shapex.length;
                        j = i++) {

                    sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
                }

                sr.end();
            }
            if (entity.getType().equals(MAP)) {
                sr.setColor(1, 1, 0, 1);
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.circle(entity.getX() - entity.getRadius() / 2, entity.getY() + entity.getRadius() / 2, 1);
                sr.end();

            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    private final LookupListener lookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {
            //newly installed modules
            for (IGamePluginService updatedGamePlugin : lookup.lookupAll(IGamePluginService.class)) {
                if (!gamePlugins.contains(updatedGamePlugin)) {
                    updatedGamePlugin.start(gameData, world);
                    gamePlugins.add(updatedGamePlugin);
                }
            }

            //stop and remove module
            //Uninstall klasse i silent update med en uninstall xml fil til moduler, som skal afinstalleres
            for (IGamePluginService gs : gamePlugins) {
                if (!lookup.lookupAll(IGamePluginService.class).contains(gs)) {
                    gs.stop(gameData);
                    gamePlugins.remove(gs);
                }
                
            }
        }
    };
}
