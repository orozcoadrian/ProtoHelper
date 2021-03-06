package com.pwf.protohelper;

import com.pwf.core.Engine;
import com.pwf.core.EngineConfiguration;
import com.pwf.core.NoLoadedMessagesException;
import com.pwf.core.ProtoFilter;
import com.pwf.plugin.ErrorEventListener;
import com.pwf.plugin.Plugin;
import com.pwf.plugin.PluginManager;
import com.pwf.plugin.PluginManagerFactory;
import com.pwf.protohelper.platform.CoreEnginePHP;
import com.pwf.protohelper.platform.UserInterfacePHP;
import com.pwf.protohelper.platform.ui.ConsoleUserInterface;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class ProtoHelper
{
    private static final Logger logger = LoggerFactory.getLogger(ProtoHelper.class);

    public static void main(String[] args) throws NoLoadedMessagesException
    {
        long start = System.currentTimeMillis();
        final PluginManager manager = PluginManagerFactory.createPluginManager();

        manager.addErrorHandler(new ErrorEventListener()
        {
            @Override
            public void onErrorOccurred(Plugin plugin, Throwable exception)
            {
                //error happened
                logger.debug("Error: " + exception.getMessage());
            }
        });

        manager.loadAllPlugins();
        for (Plugin plugin : manager.getPlugins())
        {
            logger.debug("Plugin: " + plugin);
        }

        CoreEnginePHP coreEnginePHP = manager.getPlugin(CoreEnginePHP.class);
        final Engine engine = coreEnginePHP.getEngine(new EngineConfigurationImpl());
        Runnable uiRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                UserInterfacePHP ui = manager.getPlugin(UserInterfacePHP.class);

                if (ui == null)
                {
                    ui = new ConsoleUserInterface();
                    manager.loadAndActivatePlugin(ui);
                }

                ui.startInterface(engine);
            }
        };

        new Thread(uiRunnable).start();

        long finish = System.currentTimeMillis();

        logger.debug("Took this many MS: " + (finish - start));
    }

    static class EngineConfigurationImpl implements EngineConfiguration
    {
        public EngineConfigurationImpl()
        {
        }

        @Override
        public String getLoadDirectory()
        {
            return "lib";
        }

        @Override
        public Collection<ProtoFilter> getProtoFilters()
        {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
