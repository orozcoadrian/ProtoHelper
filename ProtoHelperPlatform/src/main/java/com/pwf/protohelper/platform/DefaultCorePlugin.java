package com.pwf.protohelper.platform;

import com.pwf.core.Engine;
import com.pwf.core.EngineConfiguration;
import com.pwf.core.impl.DefaultEngine;
import com.pwf.plugin.PluginInformation;
import com.pwf.plugin.PluginManagerLite;

/**
 *
 * @author mfullen
 */
public class DefaultCorePlugin implements CoreEnginePHP
{
    private PluginManagerLite manager = null;
    private DefaultEngine engine = new DefaultEngine(null);

    public void onLoaded(PluginManagerLite pluginManager)
    {
        this.manager = pluginManager;
    }

    public void onActivated()
    {
        //do nothing
    }

    public void onDeactivated()
    {
        //do nothing
    }

    public PluginInformation getPluginInformation()
    {
        return new PluginInformationImpl();
    }

    public Engine getEngine(EngineConfiguration config)
    {
        this.engine.setConfiguration(config);

        return this.engine;
    }

    @Override
    public String toString()
    {
        PluginInformation pluginInfo = this.getPluginInformation();
        return String.format("%s (%s) %s © %s", pluginInfo.getName(), pluginInfo.getIdentifier(), pluginInfo.getVersion(),
                pluginInfo.getProvider());
    }

    protected static class PluginInformationImpl implements PluginInformation
    {
        public PluginInformationImpl()
        {
        }

        public String getName()
        {
            return "Protobuf Helper Plugin Core";
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
            return DefaultCorePlugin.class.getName();
        }
    }
}
