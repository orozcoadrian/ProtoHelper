package com.pwf.protohelper.platform.ui;

import com.pwf.core.Engine;
import com.pwf.plugin.PluginInformation;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.protohelper.platform.UserInterfacePHP;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mfullen
 */
public class ConsoleUserInterface implements UserInterfacePHP
{
    private PluginManagerLite manager = null;
    private final PluginInformation pluginInformation = new PluginInformationImpl();
    private Engine engine = null;
    private Map<String, Command> commands = new HashMap<String, Command>();

    public ConsoleUserInterface()
    {
    }

    protected final void initialize()
    {
        this.commands.put("network", new NetworkApp(this.manager, engine));
        this.commands.put("help", new Command()
        {
            public String getName()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void execute(String[] args)
            {
                helpMethod();
            }
        });
        this.commands.put("info", new Command()
        {
            public String getName()
            {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void execute(String[] args)
            {
                infoMethod();
            }
        });
    }

    public void startInterface(Engine coreEngine)
    {
        this.engine = coreEngine;
        this.initialize();
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
        System.out.print(">>");
        String s = null;
        try
        {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            while (!"-exit".equals(s = bufferRead.readLine()))
            {
                String[] split = s.split(" ");
                if (split.length > 0)
                {
                    String commandName = split[0];
                    int remainingArgsSize = split.length - 1;
                    String[] args = ConsoleUtils.getRemainingArguments(split);
                    Command command = this.commands.get(commandName);
                    if (command != null)
                    {
                        command.execute(args);
                    }
                    else
                    {
                        System.out.println("Command not recognized");
                    }
                }
                System.out.print(">>");
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
        System.out.println("Commands:");
        for (String string : this.commands.keySet())
        {
            System.out.println(string);
        }
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
