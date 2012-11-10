package com.pwf.protohelper.platform.ui.console;

import com.pwf.core.Engine;
import com.pwf.mvc.Controller;
import com.pwf.mvc.ControllersManager;
import com.pwf.mvc.MvcFramework;
import com.pwf.mvc.PostBackObserver;
import com.pwf.mvc.View;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.protohelper.controllers.EngineController;
import com.pwf.protohelper.controllers.NetworkDataController;
import com.pwf.protohelper.models.NetworkData;
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

    ConsoleControllerManager(Engine engine)
    {
        this.engine = engine;
    }

    public void initialize()
    {
        this.controllersManager.addController(createNetworkDataController());
        this.controllersManager.addController(createEngineDataController(this.engine));

        this.setControllersManager();
    }

    public void setControllersManager()
    {
        for (Controller controller : this.controllersManager.getAllControllers())
        {
            for (View<Object> view : controller.getViewObservers())
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

        return engineDataController;
    }

    protected static NetworkDataController createNetworkDataController()
    {
        NetworkDataController networkDataController = new NetworkDataController();
        networkDataController.addViewObserver(errorView);
        networkDataController.addViewObserver(new NetworkCreateView());
        networkDataController.addPostbackObserver(new PostBackObserver<NetworkData>()
        {
            public void dataToPost(NetworkData model)
            {
                NetworkClientPlugin networkClientPlugin = model.getNetworkClientPlugin();
                //networkClientPlugin.s
            }
        });
        return networkDataController;
    }
}
