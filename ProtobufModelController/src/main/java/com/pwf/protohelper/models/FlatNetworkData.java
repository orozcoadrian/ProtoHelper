package com.pwf.protohelper.models;

import com.pwf.plugin.network.client.NetworkClientSettings;

/**
 *
 * @author mfullen
 */
public class FlatNetworkData
{
    private NetworkClientSettings networkClientSettings;
    private String url;
    private String networkPluginClass;
    private String transportMessageClass;

    public void setNetworkClientSettings(NetworkClientSettings networkClientSettings)
    {
        this.networkClientSettings = networkClientSettings;
    }

    public void setNetworkPluginClass(String networkPluginClass)
    {
        this.networkPluginClass = networkPluginClass;
    }

    public void setTransportMessageClass(String transportMessageClass)
    {
        this.transportMessageClass = transportMessageClass;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public NetworkClientSettings getNetworkClientSettings()
    {
        return networkClientSettings;
    }

    public String getNetworkPluginClass()
    {
        return networkPluginClass;
    }

    public String getTransportMessageClass()
    {
        return transportMessageClass;
    }

    public String getUrl()
    {
        return url;
    }

    public static FlatNetworkData create(NetworkData networkData)
    {
        FlatNetworkData flatNetworkData = new FlatNetworkData();
        flatNetworkData.setTransportMessageClass(networkData.getEngineData().getTransportMessage().getClass().getName());
        flatNetworkData.setUrl(networkData.getEngineData().getLibaryId().toString());
        flatNetworkData.setNetworkClientSettings(networkData.getSettings());
        flatNetworkData.setNetworkPluginClass(networkData.getNetworkClientPlugin().getClass().getName());
        return flatNetworkData;
    }
}
