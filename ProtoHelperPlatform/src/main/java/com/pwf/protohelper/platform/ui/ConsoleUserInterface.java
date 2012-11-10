package com.pwf.protohelper.platform.ui;

import com.pwf.core.Engine;
import com.pwf.plugin.PluginInformation;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.protohelper.platform.UserInterfacePHP;
import com.pwf.protohelper.platform.ui.console.ProtohelperConsole;

/**
 *
 * @author mfullen
 */
public class ConsoleUserInterface implements UserInterfacePHP
{
    private PluginManagerLite manager = null;
    private final PluginInformation pluginInformation = new PluginInformationImpl();
    private final ProtohelperConsole console = new ProtohelperConsole();

    public ConsoleUserInterface()
    {
    }

    public void startInterface(Engine coreEngine)
    {
        console.initialize(manager, coreEngine);
        console.start();
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
