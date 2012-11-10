package com.pwf.protohelper.platform.ui.console;

import com.pwf.protohelper.platform.ui.console.command.impl.InfoCommand;
import com.pwf.protohelper.platform.ui.console.command.impl.HelpCommand;
import com.pwf.protohelper.platform.ui.console.command.impl.NetworkApp;
import com.pwf.protohelper.platform.ui.console.command.Command;
import com.pwf.core.Engine;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.protohelper.controllers.NetworkDataController;
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
        this.consoleControllerManager = new ConsoleControllerManager(engine);
        this.consoleControllerManager.initialize();
        this.commands.put("network", new NetworkApp(manager, engine));
        this.commands.put("help", new HelpCommand());
        this.commands.put("info", new InfoCommand());
    }

    public void start()
    {
        System.out.println("Welcome to Protobuf Helper. type -help for commands. -exit closes the application");
        System.out.print(">>");
        NetworkDataController controller = this.consoleControllerManager.getControllersManager().getController(NetworkDataController.class);
        controller.create();

//        String s = null;
//        try
//        {
//            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
//            while (!"-exit".equals(s = bufferRead.readLine()))
//            {
//                String[] split = s.split(" ");
//                if (split.length > 0)
//                {
//                    String commandName = split[0];
//                    int remainingArgsSize = split.length - 1;
//                    String[] args = ConsoleUtils.getRemainingArguments(split);
//                    Command command = this.commands.get(commandName);
//                    if (command != null)
//                    {
//                        command.execute(args);
//                    }
//                    else
//                    {
//                        System.out.println("Command not recognized");
//                    }
//                }
//                System.out.print(">>");
//            }
//            System.exit(0);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }

    }
}
