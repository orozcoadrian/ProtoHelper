package com.pwf.protohelper.platform.ui.console.views.network;

import com.pwf.mvcme.MvcMeView;
import com.pwf.protohelper.controllers.NetworkDataController;
import com.pwf.protohelper.models.NetworkData;
import com.pwf.protohelper.platform.ui.console.ConsoleUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mfullen
 */
public class NetworkConnectSelectionView extends MvcMeView<NetworkData>
{
    private NetworkData data;

    public NetworkConnectSelectionView()
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
        List<NetworkData> arrayList = new ArrayList<NetworkData>(networkDataController.getNetworkData());
        String messageChoice = ConsoleUtils.getInputFromUser();

        int messageIndex = Integer.parseInt(messageChoice);
        this.data = arrayList.get(messageIndex - 1);

        networkDataController.connect(data);

    }

    public String getName()
    {
        return NetworkDataController.NETWORK_DATA_SELECTION;
    }
}
