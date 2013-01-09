package com.pwf.protohelper.platform.ui.console;

import com.pwf.protohelper.platform.ui.console.views.network.NetworkConnectSelectionView;
import com.pwf.core.Engine;
import com.pwf.mvc.Controller;
import com.pwf.mvc.ControllersManager;
import com.pwf.mvc.MvcFramework;
import com.pwf.mvc.PostBackListener;
import com.pwf.mvc.View;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.protohelper.controllers.EngineController;

import com.pwf.protohelper.controllers.NetworkDataController;
import com.pwf.protohelper.models.EngineDataRepository;
import com.pwf.protohelper.models.InMemoryEngineData;
import com.pwf.protohelper.models.NetworkData;
import com.pwf.protohelper.models.XmlNetworkDataRepository;
import com.pwf.protohelper.platform.ui.console.views.ErrorView;
import com.pwf.protohelper.platform.ui.console.views.network.NetworkCreateView;

/**
 *
 * @author mfullen
 */
public class ConsoleControllerManager
{
    private ControllersManager controllersManager = MvcFramework.createControllersManager();
    private static final ErrorView errorView = new ErrorView();
    private Engine engine = null;
    private PluginManagerLite pluginManager = null;
    private static EngineDataRepository engineDataRepository = new InMemoryEngineData();

    ConsoleControllerManager(Engine engine, PluginManagerLite manager)
    {
        this.engine = engine;
        this.pluginManager = manager;
    }

    public void initialize()
    {
        this.controllersManager.addController(createNetworkDataController(this.pluginManager));
        this.controllersManager.addController(createEngineDataController(this.engine));

        this.setControllersManager();
    }

    public void setControllersManager()
    {
        for (Controller controller : this.controllersManager.getAllControllers())
        {
            for (View<Object> view : controller.getViews())
            {
                view.setControllerManager(controllersManager);
            }
        }
    }

    public ControllersManager getControllersManager()
    {
        return controllersManager;
    }

    protected static EngineController createEngineDataController(Engine engine)
    {
        EngineController engineDataController = new EngineController(engine);
        engineDataController.setEngineDataRepository(engineDataRepository);

        return engineDataController;
    }

    protected static NetworkDataController createNetworkDataController(final PluginManagerLite pluginManager)
    {
        NetworkDataController networkDataController = new NetworkDataController(pluginManager);
        networkDataController.setNetworkDataRepository(new XmlNetworkDataRepository());
        networkDataController.setEngineDataRepository(engineDataRepository);
        networkDataController.addViewListener(errorView);
        networkDataController.addViewListener(new NetworkCreateView());
        networkDataController.addViewListener(new NetworkConnectSelectionView());
        networkDataController.addPostbackListener(new PostBackListener<NetworkData>()
        {
            public void postData(NetworkData model)
            {
                NetworkClientPlugin networkClientPlugin = model.getNetworkClientPlugin();
                //networkClientPlugin.s
            }
        });
        return networkDataController;
    }
}
