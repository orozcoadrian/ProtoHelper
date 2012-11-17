package com.pwf.protohelper.platform.ui.console;

import com.pwf.core.Engine;
import com.pwf.core.EngineData;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.protohelper.controllers.EngineController;
import com.pwf.protohelper.controllers.NetworkDataController;
import com.pwf.protohelper.models.NetworkData;
import com.pwf.protohelper.platform.ui.console.command.Command;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mfullen
 */
public class ProtohelperConsole
{
    private Map<String, Command> commands = new HashMap<String, Command>();
    private ConsoleControllerManager consoleControllerManager = null;

    public final void initialize(PluginManagerLite manager, Engine engine)
    {
        this.consoleControllerManager = new ConsoleControllerManager(engine, manager);
        this.consoleControllerManager.initialize();

        final NetworkDataController networkController = this.consoleControllerManager.getControllersManager().getController(NetworkDataController.class);
        final EngineController engineController = this.consoleControllerManager.getControllersManager().getController(EngineController.class);
        Command networkSetupCommand = new Command()
        {
            public String getName()
            {
                return "networksetup";
            }

            public void execute(String[] args)
            {
                networkController.create();
            }
        };
        Command saveNetworkConfigCommand = new Command()
        {
            public String getName()
            {
                return "networksave";
            }

            public void execute(String[] args)
            {
                networkController.save();
            }
        };
        Command loadNetworkConfigCommand = new Command()
        {
            public String getName()
            {
                return "networkload";
            }

            public void execute(String[] args)
            {
                networkController.loadData();
            }
        };
        final Command listNetworkConfigCommand = new Command()
        {
            public String getName()
            {
                return "networkls";
            }

            public void execute(String[] args)
            {
                int i = 1;
                for (NetworkData networkData : networkController.getNetworkData())
                {
                    System.out.println(i++ + ":" + networkData);
                }
            }
        };
        Command networkConnectCommand = new Command()
        {
            public String getName()
            {
                return "networkconnect";
            }

            public void execute(String[] args)
            {
                listNetworkConfigCommand.execute(args);
                networkController.selectNetwork();
            }
        };
        Command listEngineData = new Command()
        {
            public String getName()
            {
                return "enginels";
            }

            public void execute(String[] args)
            {
                for (EngineData engineData : engineController.getAllEngineData())
                {
                    System.out.println(engineData.getLibaryId());
                }
            }
        };
        Command helpCommand = new Command()
        {
            public String getName()
            {
                return "-help";
            }

            public void execute(String[] args)
            {
                System.out.println("Command listing.");
                for (String string : commands.keySet())
                {
                    System.out.println("Command name: " + string);
                }
            }
        };
        this.commands.put(networkSetupCommand.getName(), networkSetupCommand);
        this.commands.put(saveNetworkConfigCommand.getName(), saveNetworkConfigCommand);
        this.commands.put(networkConnectCommand.getName(), networkConnectCommand);
        this.commands.put(loadNetworkConfigCommand.getName(), loadNetworkConfigCommand);
        this.commands.put(listNetworkConfigCommand.getName(), listNetworkConfigCommand);
        this.commands.put(listEngineData.getName(), listEngineData);
        this.commands.put(helpCommand.getName(), helpCommand);
    }

    public void start()
    {
        final EngineController controller = this.consoleControllerManager.getControllersManager().getController(EngineController.class);
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                controller.getAllEngineData();
                controller.loadExtensionRegistry();
            }
        };

        new Thread(runnable).start();


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
                System.out.print("Main Menu >>");
            }
            System.exit(0);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
