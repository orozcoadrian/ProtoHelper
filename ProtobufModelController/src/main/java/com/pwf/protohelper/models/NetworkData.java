package com.pwf.protohelper.models;

import com.pwf.core.EngineData;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.plugin.network.client.NetworkClientSettings;

/**
 *
 * @author mfullen
 */
public class NetworkData
{
    private int id;
    private NetworkClientPlugin networkClientPlugin;
    private EngineData engineData;
    private NetworkClientSettings settings;

    public EngineData getEngineData()
    {
        return this.engineData;
    }

    public NetworkClientPlugin getNetworkClientPlugin()
    {
        return networkClientPlugin;
    }

    public NetworkClientSettings getSettings()
    {
        return settings;
    }

    public void setEngineData(EngineData data)
    {
        this.engineData = data;
    }

    public void setNetworkClientPlugin(NetworkClientPlugin networkClientPlugin)
    {
        this.networkClientPlugin = networkClientPlugin;
    }

    public void setSettings(NetworkClientSettings settings)
    {
        this.settings = settings;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        String mstring = this.getEngineData() != null ? this.getEngineData().getTransportMessage().getClass().getName() : "None";
        boolean connected = this.getNetworkClientPlugin() != null ? this.getNetworkClientPlugin().isConnected() : false;
        return String.format("Id: %d Settings: %s MessageType: %s connected: %b", this.getId(), this.getSettings().toString(), mstring, connected);
    }
}
