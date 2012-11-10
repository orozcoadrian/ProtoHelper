package com.pwf.protohelper.platform.ui.console.command.impl;

import com.pwf.protohelper.models.NetworkData;
import com.pwf.protohelper.platform.ui.console.command.Command;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import com.google.protobuf.TextFormat;
import com.pwf.core.Engine;
import com.pwf.core.NoLoadedMessagesException;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.plugin.network.client.NetworkClientSettings;
import com.pwf.protohelper.platform.ui.console.ConsoleUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mfullen
 */
public class NetworkApp implements Command
{
    private List<NetworkData> networkDatas = new ArrayList<NetworkData>();
    private Map<String, Command> commands = new HashMap<String, Command>();
    private Map<Integer, NetworkData> networkMap = new HashMap<Integer, NetworkData>();
    private PluginManagerLite manager = null;
    private Engine engine = null;

    public NetworkApp(final PluginManagerLite manager, Engine engine)
    {
        this.manager = manager;
        this.engine = engine;

        this.commands.put("create", new CreateCommand(manager));
        this.commands.put("connect", new ConnectCommand(manager));
        this.commands.put("ls", new LsCommand());
        this.commands.put("send", new SendCommand());
    }

    public NetworkClientPlugin getNetworkClientPlugin()
    {
        NetworkClientPlugin networkClientPlugin = manager.getPlugin(NetworkClientPlugin.class);
        return networkClientPlugin;
    }

    public void execute(String[] args)
    {
        String commandString = args[0];
        Command command = this.commands.get(commandString);
        if (command != null)
        {
            command.execute(ConsoleUtils.getRemainingArguments(args));
        }
    }

    public String getName()
    {
        return "network";
    }

    private class ConnectCommand implements Command
    {
        private final PluginManagerLite manager;

        public ConnectCommand(PluginManagerLite manager)
        {
            this.manager = manager;
        }

        public String getName()
        {
            return "connect";
        }

        public void execute(String[] args)
        {
            if (getNetworkClientPlugin() == null)
            {
                System.out.println("No network plugin detected.");
                return;
            }
            if (args.length == 0)
            {
                System.out.println("Please select a network id to connect too.");
                return;
            }

            int index = Integer.valueOf(args[0]) - 1;

            try
            {
                NetworkData data = networkDatas.get(index);

                NetworkClientPlugin clonePlugin = manager.clonePlugin(getNetworkClientPlugin().getClass());
                data.setNetworkClientPlugin(clonePlugin);

                //TextFormat.merge("id: 2100 name: \"Mike\" job: \"Software Engineer 2\"", data.getMessage());

                clonePlugin.setMessageType(data.getMessage().build());
                clonePlugin.connect(data.getSettings());
            }
//            catch (ParseException ex)
//            {
//            }
            catch (Exception exception)
            {
                System.out.println("Invalid Network setting index");
            }

        }
    }

    public List<Builder> getMessages()
    {
        List<Builder> messages = null;
        try
        {
            System.out.println("Messages to send.");


            messages = new ArrayList<Builder>(engine.getProtoBuilders());
        }
        catch (NoLoadedMessagesException ex)
        {
            System.out.println("No loaded messages to send.");
        }
        finally
        {
            return messages;
        }
    }

    public void displayAvailableMessages()
    {
        int i = 1;
        for (Builder builder : getMessages())
        {
            System.out.println(i + ": " + builder.getClass().getName());
            i++;
        }
    }

    private class LsCommand implements Command
    {
        public LsCommand()
        {
        }

        public String getName()
        {
            return "ls";
        }

        public void execute(String[] args)
        {
            if (networkDatas.isEmpty())
            {
                System.out.println("No network settings specified");
                return;
            }
            int i = 1;

            for (NetworkData networkData : networkDatas)
            {
                System.out.println(i + ":" + networkData);
                i++;
            }
        }
    }

    private class SendCommand implements Command
    {
        public SendCommand()
        {
        }

        public String getName()
        {
            return "send";
        }

        public void execute(String[] args)
        {
            if (getNetworkClientPlugin() == null)
            {
                System.out.println("No network plugin detected.");
                return;
            }

            if (args.length == 0)
            {
                System.out.println("Please select a network id to connect too.");

                return;
            }

            int connectionIndex = Integer.valueOf(args[0]) - 1;

            try
            {
                System.out.println("Enter a protobuf message to send:");
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter Message >>");
                String s = bufferRead.readLine();

                NetworkData data = networkDatas.get(connectionIndex);
                NetworkClientPlugin plugin = data.getNetworkClientPlugin();
                TextFormat.merge(s, data.getMessage());
                Message msg = data.getMessage().build();
                System.out.println(TextFormat.shortDebugString(msg));
                plugin.sendMessage(msg);
            }
            catch (Exception exception)
            {
                System.out.println("Invalid Network setting index");
            }
        }
    }

    private class CreateCommand implements Command
    {
        private final PluginManagerLite manager;

        public CreateCommand(PluginManagerLite manager)
        {
            this.manager = manager;
        }

        public String getName()
        {
            return "create";
        }

        public void execute(String[] args)
        {
            try
            {
                if (args.length < 2)
                {
                    System.out.println("Error! Incorrect number of arguments. try 'network create <IP> <PORT> (ssl)");
                    return;
                }

                String ip = args[0];
                int port = Integer.valueOf(args[1]);
                boolean ssl = false;

                if (args.length == 3)
                {
                    if (args[2].startsWith("ssl"))
                    {
                        ssl = true;
                    }
                }

                displayAvailableMessages();

                System.out.println("Select a message type!!!!");
                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Select Message>>");
                String s = bufferRead.readLine();

                int messageIndex = Integer.parseInt(s);
                Builder builder = getMessages().get(messageIndex - 1);

                NetworkClientSettingsImpl networkClientSettingsImpl = new NetworkClientSettingsImpl();
                networkClientSettingsImpl.setIpAddress(ip);
                networkClientSettingsImpl.setPort(port);
                networkClientSettingsImpl.setSSL(ssl);

                NetworkData data = new NetworkData();
                data.setSettings(networkClientSettingsImpl);
                data.setMessage(builder);

                networkDatas.add(data);
            }
            catch (IOException ex)
            {
                Logger.getLogger(NetworkApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
