package com.pwf.protohelper.platform.ui.console.command.impl;

import com.pwf.plugin.network.client.NetworkClientSettings;

/**
 *
 * @author mfullen
 */
public class NetworkClientSettingsImpl implements NetworkClientSettings
{
    private int port;
    private boolean SSL;
    private String ipAddress;

    public NetworkClientSettingsImpl()
    {
    }

    public int getPort()
    {
        return port;
    }

    public boolean isSSL()
    {
        return SSL;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setSSL(boolean SSL)
    {
        this.SSL = SSL;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString()
    {
        return String.format("Ip: %s Port: %d ssl:%b", this.getIpAddress(), this.getPort(), this.isSSL());
    }
}
