package com.pwf.protohelper.platform.ui.console.views.network;

import com.google.protobuf.Message.Builder;
import com.pwf.core.EngineData;
import com.pwf.core.impl.EngineUtils;
import com.pwf.mvcme.MvcMeView;
import com.pwf.plugin.network.client.DefaultNetworkClientSettings;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.protohelper.controllers.EngineController;
import com.pwf.protohelper.controllers.NetworkDataController;
import com.pwf.protohelper.models.NetworkData;
import com.pwf.protohelper.platform.ui.console.ConsoleUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author mfullen
 */
public class NetworkCreateView extends MvcMeView<NetworkData>
{
    private NetworkData data;
    private static BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

    public NetworkCreateView()
    {
    }

    public void update(NetworkData model)
    {
        this.data = model;
        this.setVisible(true);
    }

    public void setVisible(boolean visible)
    {
        NetworkDataController networkDataController = this.getMvcFramework().getController(NetworkDataController.class);
        NetworkClientPlugin networkClientPlugin = this.selectNetworkClientPlugin(networkDataController);
        this.data.setNetworkClientPlugin(networkClientPlugin);
        this.createNetworkSettings();
        EngineData selectedEngineData = this.selectEngineData();
        this.selectProtobufMessageType(selectedEngineData);

        networkDataController.created(this.data);
        System.out.println("Network Data Created: " + this.data);
    }

    protected void createNetworkSettings()
    {
        try
        {
            System.out.println("===========================");
            System.out.println("Network Settings Creation");
            System.out.println("<required> (optional)");
            System.out.println("create <ip> <port> (ssl)");
            System.out.println("===========================");

            String s = ConsoleUtils.getInputFromUser();
            String[] split = s.split(" ");
            if (split.length > 0)
            {
                if (split[0].equalsIgnoreCase("create"))
                {
                    String ip = split[1];
                    int port = Integer.valueOf(split[2]);
                    boolean ssl = false;

                    if (split.length == 4)
                    {
                        if (split[3].startsWith("ssl"))
                        {
                            ssl = true;
                        }
                    }

                    DefaultNetworkClientSettings settings = new DefaultNetworkClientSettings();
                    settings.setIpAddress(ip);
                    settings.setPort(port);
                    settings.setSSL(ssl);

                    this.data.setSettings(settings);
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error Occurred! Please try again. " + ex.getMessage());
        }
    }

    protected EngineData selectEngineData()
    {
        EngineData selected = null;
        try
        {
            EngineController engineController = this.getMvcFramework().getController(EngineController.class);

            System.out.println("===========================");
            System.out.println("Please select from the following Message library to use");
            List<EngineData> engineDatas = engineController.getAllEngineData();
            int i = 1;
            for (EngineData engineData : engineDatas)
            {
                System.out.println(i + ": " + engineData);
                i++;
            }


            System.out.println("===========================");

            String messageChoice = ConsoleUtils.getInputFromUser();

            int messageIndex = Integer.parseInt(messageChoice);
            selected = engineDatas.get(messageIndex - 1);

        }
        catch (Exception ex)
        {
            System.out.println("Error Occurred! Please try again. " + ex.getMessage());
        }
        finally
        {
            return selected;
        }
    }

    protected void selectProtobufMessageType(EngineData engineData)
    {
        System.out.println("===========================");
        System.out.println("Please select the Transport Message to use");
        try
        {
            List<Builder> builders = new ArrayList<Builder>();

            Collection<Builder> protoBuilders = EngineUtils.getProtoBuilders(engineData);
            builders.addAll(protoBuilders);

            Collections.sort(builders, new Comparator<Builder>()
            {
                public int compare(Builder o1, Builder o2)
                {
                    return o1.getClass().getName().compareTo(o2.getClass().getName());
                }
            });

            int i = 1;
            for (Builder builder : builders)
            {
                System.out.println(i + ": " + builder.getClass().getName());
                i++;
            }
            System.out.println("===========================");

            String messageChoice = ConsoleUtils.getInputFromUser();

            int messageIndex = Integer.parseInt(messageChoice);
            Builder builder = builders.get(messageIndex - 1);
            engineData.setTransportMessage(builder);
            this.data.setEngineData(engineData);
        }
        catch (Exception ex)
        {
            System.out.println("Error Occurred! Please try again. " + ex.getMessage());
        }
    }

    public String getName()
    {
        return "create";
    }

    protected NetworkClientPlugin selectNetworkClientPlugin(NetworkDataController networkDataController)
    {

        List<NetworkClientPlugin> availableNetworkPlugins = new ArrayList<NetworkClientPlugin>(networkDataController.getAvailableNetworkPlugins());
        if (availableNetworkPlugins.isEmpty())
        {
            System.out.println("No network plugins available");
            return null;
        }
        if (availableNetworkPlugins.size() == 1)
        {
            return networkDataController.autoSelectNetworkClientPlugin();
        }
        System.out.println("==================================================");
        System.out.println("Please select the network client plugin to use:");
        int i = 1;
        for (NetworkClientPlugin networkClientPlugin : availableNetworkPlugins)
        {
            System.out.println(i++ + ":" + networkClientPlugin);
        }
        System.out.println("==================================================");

        String messageChoice = ConsoleUtils.getInputFromUser();

        int messageIndex = Integer.parseInt(messageChoice);
        return availableNetworkPlugins.get(messageIndex - 1);
    }
}