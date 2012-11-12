package com.pwf.protohelper.controllers;

import com.pwf.mvc.AbstractController;
import com.pwf.mvc.ViewNotFoundException;
import com.pwf.protohelper.models.InMemoryNetworkDataRepository;
import com.pwf.protohelper.models.NetworkData;
import com.pwf.protohelper.models.NetworkDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfullen
 */
public class NetworkDataController extends AbstractController
{
    private static final Logger logger = LoggerFactory.getLogger(NetworkDataController.class);
    public static final String NETWORK_DATA_CREATE = "create";
    public static final String NETWORK_DATA_EDIT = "edit";
    public static final String NETWORK_DATA_DELETE = "delete";
    private NetworkDataRepository networkDataRepository = null;

    public NetworkDataController()
    {
        this(new InMemoryNetworkDataRepository());
    }

    public NetworkDataController(NetworkDataRepository networkDataRepository)
    {
        this.setNetworkDataRepository(networkDataRepository);
    }

    public final void setNetworkDataRepository(NetworkDataRepository networkDataRepository)
    {
        this.networkDataRepository = networkDataRepository;
    }

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

    public void created(NetworkData networkData)
    {
        this.networkDataRepository.add(networkData);
        this.networkDataRepository.save();
    }

    public void edit(int id)
    {
        NetworkData data = this.networkDataRepository.findById(id);
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

    public void edited(NetworkData networkData)
    {
        NetworkData nd = this.networkDataRepository.findById(networkData.getId());
        nd.setEngineData(networkData.getEngineData());
        nd.setNetworkClientPlugin(networkData.getNetworkClientPlugin());
        nd.setSettings(networkData.getSettings());

        this.networkDataRepository.save();
    }

    public void delete(int id)
    {
        NetworkData data = this.networkDataRepository.findById(id);
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

    public void deleted(NetworkData networkData)
    {
        NetworkData nd = this.networkDataRepository.findById(networkData.getId());
        this.networkDataRepository.remove(nd);
        this.networkDataRepository.save();
    }
}
