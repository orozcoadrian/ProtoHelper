package com.pwf.protohelper.controllers;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import com.pwf.core.EngineData;
import com.pwf.core.impl.EngineUtils;
import com.pwf.mvcme.MvcMeController;
import com.pwf.mvcme.ViewNotFoundException;

import com.pwf.plugin.PluginManagerLite;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.protohelper.models.EngineDataRepository;
import com.pwf.protohelper.models.FlatNetworkData;
import com.pwf.protohelper.models.InMemoryEngineData;
import com.pwf.protohelper.models.InMemoryNetworkDataRepository;
import com.pwf.protohelper.models.NetworkData;
import com.pwf.protohelper.models.NetworkDataRepository;
import java.lang.reflect.Method;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfullen
 */
public class NetworkDataController extends MvcMeController
{
    private static final Logger logger = LoggerFactory.getLogger(NetworkDataController.class);
    public static final String NETWORK_DATA_CREATE = "create";
    public static final String NETWORK_DATA_EDIT = "edit";
    public static final String NETWORK_DATA_DELETE = "delete";
    public static final String NETWORK_DATA_SELECTION = "networkSelection";
    private NetworkDataRepository networkDataRepository = null;
    private NetworkDataRepository activeConnectionsRepository = null;
    private EngineDataRepository engineDataRepository = null;
    private PluginManagerLite pluginManager;

    public NetworkDataController()
    {
        this(new InMemoryNetworkDataRepository());
    }

    public NetworkDataController(PluginManagerLite pluginManager)
    {
        this(new InMemoryNetworkDataRepository());
        this.pluginManager = pluginManager;
        this.activeConnectionsRepository = new InMemoryNetworkDataRepository();
        this.engineDataRepository = new InMemoryEngineData();
    }

    NetworkDataController(NetworkDataRepository networkDataRepository)
    {
        this.setNetworkDataRepository(networkDataRepository);
    }

    public boolean hasNetworkConnectionPlugins()
    {
        return !this.getAvailableNetworkPlugins().isEmpty();
    }

    public NetworkClientPlugin autoSelectNetworkClientPlugin()
    {
        if (hasNetworkConnectionPlugins())
        {
            return this.getAvailableNetworkPlugins().iterator().next();
        }

        logger.warn("There aren't any network client plugins to select. Returning null");
        return null;
    }

    public Collection<NetworkClientPlugin> getAvailableNetworkPlugins()
    {
        return this.pluginManager.getPlugins(NetworkClientPlugin.class);
    }

    public final void setNetworkDataRepository(NetworkDataRepository networkDataRepository)
    {
        this.networkDataRepository = networkDataRepository;
    }

    public void setEngineDataRepository(EngineDataRepository engineDataRepository)
    {
        this.engineDataRepository = engineDataRepository;
    }

    public void send(final NetworkData data, String message)
    {
        NetworkClientPlugin networkClientPlugin = data.getNetworkClientPlugin();
        if (networkClientPlugin == null)
        {
            logger.error("Network plugin null");
            return;
        }

        if (data.getEngineData() == null || data.getEngineData().getTransportMessage() == null)
        {
            logger.error("Transport message is null");
            return;
        }
        try
        {
            TextFormat.merge(message, data.getEngineData().getTransportMessage());
        }
        catch (ParseException ex)
        {
            logger.error("Error parsing the message", ex);
        }
        Message msg = data.getEngineData().getTransportMessage().build();
        logger.debug("Sending {}", TextFormat.shortDebugString(msg));
        networkClientPlugin.sendMessage(msg);
    }

    public void connect(final NetworkData data)
    {
        String errorMessage = "No network settings found.";
        if (data == null)
        {
            logger.error(errorMessage);
            this.updateErrorView(errorMessage);
            return;
        }

        if (data.getNetworkClientPlugin() == null)
        {
            errorMessage = "No Network Plugin found. Please configure a network plugin";
            logger.error(errorMessage);
            this.updateErrorView(errorMessage);
            return;
        }

        if (data.getEngineData().getTransportMessage() == null)
        {
            errorMessage = "Transport Message has not been configured. Please redo configuration";
            logger.error(errorMessage);
            this.updateErrorView(errorMessage);
            return;
        }
        NetworkClientPlugin networkClientPlugin = data.getNetworkClientPlugin();
        if (networkClientPlugin.isConnected())
        {
            logger.warn("Network plugin already connected. Creating another instance of the plugin");
            networkClientPlugin = this.pluginManager.clonePlugin(networkClientPlugin.getClass());
        }
        //update the networkclient plugin being used
        networkClientPlugin.addNetworkEventListener(new NetworkEventListenerImpl(data, this.activeConnectionsRepository));
        data.setNetworkClientPlugin(networkClientPlugin);
        networkClientPlugin.setMessageType(data.getEngineData().getTransportMessage().build());
        logger.info("Attempting to connect to {}", data.getSettings());
        networkClientPlugin.connect(data.getSettings());
    }

    public void loadData()
    {
        Collection<FlatNetworkData> loadedNetworkDatas = this.networkDataRepository.load();

        for (FlatNetworkData flatNetworkData : loadedNetworkDatas)
        {
            NetworkData data = new NetworkData();
            data.setSettings(flatNetworkData.getNetworkClientSettings());
            EngineData findBy = this.engineDataRepository.findBy(flatNetworkData.getUrl());

            if (findBy != null)
            {
                try
                {
                    Class<? extends Message> transportClass = null;
                    for (Class<? extends Message> class1 : findBy.getLoadedClasses())
                    {
                        String replaceAll = flatNetworkData.getTransportMessageClass().replaceAll("\\$Builder", "");
                        if (class1.getName().equals(replaceAll))
                        {
                            transportClass = class1;
                        }
                    }

                    Method declaredMethod = transportClass.getMethod("newBuilder", (Class<?>[]) null);
                    Message.Builder transportBuilder = (Message.Builder) declaredMethod.invoke(transportClass, new Object[]
                            {
                            });
                    Builder constructBuilder = EngineUtils.constructBuilder(transportBuilder);
                    findBy.setTransportMessage(constructBuilder);
                }
                catch (Exception ex)
                {
                    logger.error("Failure to load data from file", ex);
                }
            }
            data.setEngineData(findBy);
            data.setId(0);

            NetworkClientPlugin ncp = null;
            if (hasNetworkConnectionPlugins())
            {
                for (NetworkClientPlugin networkClientPlugin : this.getAvailableNetworkPlugins())
                {
                    if (networkClientPlugin.getClass().getName().equals(flatNetworkData.getNetworkPluginClass()))
                    {
                        ncp = networkClientPlugin;
                    }
                }
            }
            data.setNetworkClientPlugin(ncp);
            this.networkDataRepository.create(data);
        }
    }

    public void save()
    {
        this.networkDataRepository.save();
    }

    public Collection<NetworkData> getNetworkData()
    {
        return this.networkDataRepository.getAll();
    }

    public Collection<NetworkData> getActiveConnections()
    {
        return this.activeConnectionsRepository.getAll();
    }

    public void selectNetwork()
    {
        try
        {
            this.getView(NETWORK_DATA_SELECTION).update(new NetworkData());
        }
        catch (ViewNotFoundException ex)
        {
            this.updateErrorView(createViewNotFoundErrorMesage(NETWORK_DATA_SELECTION));
            logger.error("View Not found", ex);
        }
    }

    /**
     * GET
     */
    public void create()
    {
        try
        {
            this.getView(NETWORK_DATA_CREATE).update(new NetworkData());
        }
        catch (ViewNotFoundException ex)
        {
            this.updateErrorView(createViewNotFoundErrorMesage(NETWORK_DATA_CREATE));
            logger.error("View Not found", ex);
        }
    }

    /**
     * POST DATA
     *
     * @param networkData
     */
    public void created(NetworkData networkData)
    {
        this.networkDataRepository.create(networkData);
        this.networkDataRepository.save();
    }

    /**
     * GET
     *
     * @param id
     */
    public void edit(int id)
    {
        NetworkData data = this.networkDataRepository.get(id);
        try
        {
            this.getView(NETWORK_DATA_EDIT).update(data);
        }
        catch (ViewNotFoundException ex)
        {
            this.updateErrorView(createViewNotFoundErrorMesage(NETWORK_DATA_EDIT));
            logger.error("View Not found", ex);
        }
    }

    /**
     * POST
     *
     * @param networkData
     */
    public void edited(NetworkData networkData)
    {
        NetworkData nd = this.networkDataRepository.get(networkData.getId());
        nd.setEngineData(networkData.getEngineData());
        nd.setNetworkClientPlugin(networkData.getNetworkClientPlugin());
        nd.setSettings(networkData.getSettings());

        this.networkDataRepository.save();
    }

    /**
     * GET
     *
     * @param id
     */
    public void delete(int id)
    {
        NetworkData data = this.networkDataRepository.get(id);
        try
        {
            this.getView(NETWORK_DATA_DELETE).update(data);
        }
        catch (ViewNotFoundException ex)
        {
            this.updateErrorView(createViewNotFoundErrorMesage(NETWORK_DATA_DELETE));
            logger.error("View Not found", ex);
        }
    }

    /**
     * POST
     *
     * @param networkData
     */
    public void deleted(NetworkData networkData)
    {
        NetworkData nd = this.networkDataRepository.get(networkData.getId());
        this.networkDataRepository.delete(nd);
        this.networkDataRepository.save();
    }
}
