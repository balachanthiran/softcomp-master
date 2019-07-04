package dk.sdu.mmmi.cbse.asteroids;

import dk.sdu.mmmi.cbse.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.services.IGamePluginService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
    //Generic...
    private ServiceRegistration r;
    private ServiceRegistration s;

    @Override
    public void start(BundleContext context) throws Exception {
        r = context.registerService(IGamePluginService.class, new AsteroidsEntityPlugin(), null);
        s = context.registerService(IEntityProcessingService.class, new AsteroidsControlSystem(), null);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        r.unregister();
        s.unregister();
    }

}
