package com.pwf.protohelper.platform.ui;

import com.pwf.core.Engine;
import com.pwf.plugin.PluginInformation;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.protohelper.platform.UserInterfacePHP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author mfullen
 */
public class ConsoleUserInterface implements UserInterfacePHP
{
    private PluginManagerLite manager = null;
    private final PluginInformation pluginInformation = new PluginInformationImpl();
    private Engine engine = null;

    public void startInterface(Engine coreEngine)
    {
        this.engine = coreEngine;
        this.consoleAppStarted();
    }

    public void onLoaded(PluginManagerLite pluginManager)
    {
        this.manager = pluginManager;
    }

    public void onActivated()
    {
    }

    public void onDeactivated()
    {
    }

    public PluginInformation getPluginInformation()
    {
        return this.pluginInformation;
    }

    public void consoleAppStarted()
    {
        System.out.println("Welcome to Protobuf Helper. type -help for commands. -exit closes the application");
        String s = null;
        try
        {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            while (!"-exit".equals(s = bufferRead.readLine()))
            {
                if (s.equalsIgnoreCase("-help"))
                {
                    helpMethod();
                }
                else if (s.equalsIgnoreCase("-info"))
                {
                    infoMethod();
                }
                else if (s.equalsIgnoreCase("-protobufs"))
                {
                    validProtobufs();
                }
                else
                {
                    System.out.println("Command not recognized");
                }

            }
            System.exit(0);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void infoMethod()
    {
        System.out.println("PluginInformation: " + this);
    }

    public void helpMethod()
    {
        System.out.println("-info prints plugin information");
    }

    public void validProtobufs()
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected static class PluginInformationImpl implements PluginInformation
    {
        public PluginInformationImpl()
        {
        }

        public String getName()
        {
            return "ProtoHelper Console UI Plugin";
        }

        public String getVersion()
        {
            return "1.0.0";
        }

        public String getProvider()
        {
            return PluginInformation.DEFAULT_PROVIDER;
        }

        public String getIdentifier()
        {
            return ConsoleUserInterface.class.getName();
        }
    }

    @Override
    public String toString()
    {
        PluginInformation pluginInfo = this.getPluginInformation();
        return String.format("%s (%s) %s © %s", pluginInfo.getName(), pluginInfo.getIdentifier(), pluginInfo.getVersion(),
                pluginInfo.getProvider());
    }
}
