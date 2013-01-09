package com.pwf.ui.protohelper;

import com.pwf.plugin.PluginInformation;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.plugin.network.client.NetworkClientSettings;
import com.pwf.plugin.network.client.NetworkEventListener;
import java.util.Collection;

/**
 *
 * @author mfullen
 */
public class MockNetworkClientPlugin implements NetworkClientPlugin
{
    private String name;

    public MockNetworkClientPlugin(String name)
    {
        this.name = name;
    }

    @Override
    public void connect(NetworkClientSettings networkClientSettings)
    {
        //do nothing
    }

    @Override
    public void disconnect()
    {
        //do nothing
    }

    @Override
    public void onHandleMessageReceived(Object message)
    {
        //do nothing
    }

    @Override
    public void sendMessage(Object message)
    {
        //do nothing
    }

    @Override
    public boolean isConnected()
    {
        return false;
    }

    @Override
    public void setMessageType(Object message)
    {
        //do nothing
    }

    @Override
    public void onLoaded(PluginManagerLite pluginManager)
    {
        //do nothing
    }

    @Override
    public void onActivated()
    {
        //do nothing
    }

    @Override
    public void onDeactivated()
    {
        //do nothing
    }

    @Override
    public PluginInformation getPluginInformation()
    {
        //do nothing
        return new PluginInformation()
        {
            @Override
            public String getName()
            {
                return "Mock Network Client: " + name;
            }

            @Override
            public String getVersion()
            {
                return "Version 0";
            }

            @Override
            public String getProvider()
            {
                return PluginInformation.DEFAULT_PROVIDER;
            }

            @Override
            public String getIdentifier()
            {
                return "Mock Test Plugin";
            }
        };
    }

    @Override
    public void addNetworkEventListener(NetworkEventListener listener)
    {
        //do nothing
    }

    @Override
    public void removeNetworkEventListener(NetworkEventListener listener)
    {
        //do nothing
    }

    @Override
    public Collection getListeners()
    {
        //do nothing
        return null;
    }
}
