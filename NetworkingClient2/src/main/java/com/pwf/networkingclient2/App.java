package com.pwf.networkingclient2;

import com.pwf.plugin.PluginInformation;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.plugin.network.client.NetworkClientSettings;

/**
 * Hello world!
 *
 */
public class App implements NetworkClientPlugin
{
    public static void main(String[] args)
    {
        System.out.println("Hello World!");
    }

    public void connect(NetworkClientSettings networkClientSettings)
    {
    }

    public void disconnect()
    {
    }

    public void onHandleMessageReceived(Object message)
    {
    }

    public void sendMessage(Object message)
    {
    }

    public boolean isConnected()
    {
        return true;
    }

    public void setMessageType(Object message)
    {
    }

    public void onLoaded(PluginManagerLite pluginManager)
    {
    }

    public void onActivated()
    {
    }

    public void onDeactivated()
    {
    }

    public PluginInformation getPluginInformation()
    {
        return new PluginInformation()
        {
            public String getName()
            {
                return "test network plugin";
            }

            public String getVersion()
            {
                return "0.0.0.0";
            }

            public String getProvider()
            {
                return PluginInformation.DEFAULT_PROVIDER;
            }

            public String getIdentifier()
            {
                return "test.network.plugin";
            }
        };
    }
}
