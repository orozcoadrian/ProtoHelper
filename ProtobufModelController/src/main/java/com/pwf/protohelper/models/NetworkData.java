package com.pwf.protohelper.models;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
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
    private Message.Builder message;
    private NetworkClientSettings settings;

    public Builder getMessage()
    {
        return message;
    }

    public NetworkClientPlugin getNetworkClientPlugin()
    {
        return networkClientPlugin;
    }

    public NetworkClientSettings getSettings()
    {
        return settings;
    }

    public void setMessage(Builder message)
    {
        this.message = message;
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
        String mstring = this.getMessage() != null ? this.getMessage().build().getClass().getName() : "None";
        boolean connected = this.getNetworkClientPlugin() != null ? this.getNetworkClientPlugin().isConnected() : false;
        return String.format("Id: %d Settings: %s MessageType: %s connected: %b", this.getId(), this.getSettings().toString(), mstring, connected);
    }
}
