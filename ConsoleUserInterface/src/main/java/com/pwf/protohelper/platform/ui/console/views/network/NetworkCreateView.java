package com.pwf.protohelper.platform.ui.console.views.network;

import com.google.protobuf.Message.Builder;
import com.pwf.mvc.ControllersManager;
import com.pwf.mvc.View;
import com.pwf.protohelper.controllers.EngineController;
import com.pwf.protohelper.controllers.NetworkDataController;
import com.pwf.protohelper.models.NetworkData;
import com.pwf.protohelper.platform.ui.console.command.impl.NetworkClientSettingsImpl;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author mfullen
 */
public class NetworkCreateView implements View<NetworkData>
{
    private ControllersManager controllersManager = null;
    private NetworkData data;

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
        this.createNetworkSettings();
        this.selectProtobufMessageType();
        NetworkDataController networkDataController = this.getControllerManager().getController(NetworkDataController.class);
        networkDataController.created(this.data);
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

            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String s = bufferRead.readLine();
            String[] split = s.split(" ");
            if (split.length > 0)
            {
                if (split[0].equalsIgnoreCase("create"))
                {
                    String ip = split[1];
                    int port = Integer.valueOf(split[2]);
                    boolean ssl = false;

                    // bufferRead.close();

                    if (split.length == 4)
                    {
                        if (split[3].startsWith("ssl"))
                        {
                            ssl = true;
                        }
                    }

                    NetworkClientSettingsImpl settings = new NetworkClientSettingsImpl();
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

    protected void selectProtobufMessageType()
    {
        System.out.println("===========================");
        System.out.println("Please select from the following Message types");
        try
        {
            EngineController engineController = this.getControllerManager().getController(EngineController.class);
            List<Builder> builders = engineController.getBuilders();
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

            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String messageChoice = bufferRead.readLine();
            //bufferRead.close();

            int messageIndex = Integer.parseInt(messageChoice);
            Builder builder = builders.get(messageIndex - 1);
            this.data.setMessage(builder);
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

    public void setControllerManager(ControllersManager controllerManager)
    {
        this.controllersManager = controllerManager;
    }

    public ControllersManager getControllerManager()
    {
        return this.controllersManager;
    }
}