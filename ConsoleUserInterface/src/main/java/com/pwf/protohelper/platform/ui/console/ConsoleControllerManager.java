package com.pwf.protohelper.platform.ui.console;

import com.pwf.core.Engine;
import com.pwf.mvcme.MvcFramework;
import com.pwf.mvcme.MvcMe;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.protohelper.controllers.EngineController;
import com.pwf.protohelper.controllers.NetworkDataController;
import com.pwf.protohelper.models.EngineDataRepository;
import com.pwf.protohelper.models.InMemoryEngineData;
import com.pwf.protohelper.models.XmlNetworkDataRepository;
import com.pwf.protohelper.platform.ui.console.views.ErrorView;
import com.pwf.protohelper.platform.ui.console.views.network.NetworkConnectSelectionView;
import com.pwf.protohelper.platform.ui.console.views.network.NetworkCreateView;

/**
 *
 * @author mfullen
 */
public class ConsoleControllerManager
{
    private static MvcFramework mvcFramework = MvcMe.createMvcFramework();
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
        mvcFramework.register(createNetworkDataController(this.pluginManager));
        mvcFramework.register(createEngineDataController(this.engine));
    }

    protected static EngineController createEngineDataController(Engine engine)
    {
        EngineController engineDataController = new EngineController(engine);
        engineDataController.setEngineDataRepository(engineDataRepository);

        return engineDataController;
    }

    public static MvcFramework getMvcFramework()
    {
        return mvcFramework;
    }

    protected static NetworkDataController createNetworkDataController(final PluginManagerLite pluginManager)
    {
        NetworkDataController networkDataController = new NetworkDataController(pluginManager);
        networkDataController.setNetworkDataRepository(new XmlNetworkDataRepository());
        networkDataController.setEngineDataRepository(engineDataRepository);
        mvcFramework.register(errorView);
        mvcFramework.register(new NetworkCreateView());
        mvcFramework.register(new NetworkConnectSelectionView());

        return networkDataController;
    }
}
