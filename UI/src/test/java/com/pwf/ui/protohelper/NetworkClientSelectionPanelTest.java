package com.pwf.ui.protohelper;

import com.pwf.mvcme.MvcFramework;
import com.pwf.mvcme.MvcMe;
import com.pwf.plugin.Plugin;
import com.pwf.plugin.PluginManager;
import com.pwf.plugin.PluginManagerFactory;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.protohelper.controllers.NetworkDataController;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author mfullen
 */
public class NetworkClientSelectionPanelTest
{
    public NetworkClientSelectionPanelTest()
    {
    }

    public static void main(String[] args)
    {
        loadNetworkClientSection(new MockNetworkClientPlugin("n1"), new MockNetworkClientPlugin("n2"));
    }

    protected static void loadNetworkClientSection(NetworkClientPlugin... plugins)
    {
        MvcFramework cm = MvcMe.createMvcFramework();
        final PluginManager pluginManager = PluginManagerFactory.createPluginManager();
        for (NetworkClientPlugin networkClientPlugin : plugins)
        {
            pluginManager.addPlugin(networkClientPlugin);
        }
        final NetworkDataController networkDataController = new NetworkDataController(pluginManager);
        cm.register(networkDataController);

        final NetworkClientSelectionPanel panel = new NetworkClientSelectionPanel();
        cm.register(panel);
        panel.update(networkDataController.getAvailableNetworkPlugins());

        JFrame frame = new JFrame("NetworkClientSelection Test");
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        JFrame actionFrame = new JFrame("ActionFrame");
        JPanel actionPanel = new JPanel();
        final JLabel selectedLabel = new JLabel("Test");
        actionPanel.add(new JButton(new AbstractAction("Add NetworkClient")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Random r = new Random();
                int nextInt = r.nextInt();
                pluginManager.addPlugin(new MockNetworkClientPlugin("R" + nextInt));
                panel.update(networkDataController.getAvailableNetworkPlugins());
                selectedLabel.setText(panel.getSelectedNetworkClientPlugin() + "");
            }
        }));
        actionPanel.add(new JButton(new AbstractAction("Remove All NetworkClients")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                CopyOnWriteArrayList<Plugin> copyOnWriteArrayList = new CopyOnWriteArrayList<Plugin>(pluginManager.getPlugins());
                for (Plugin plugin : copyOnWriteArrayList)
                {
                    pluginManager.removePlugin(plugin);
                }
                panel.update(networkDataController.getAvailableNetworkPlugins());
                selectedLabel.setText(panel.getSelectedNetworkClientPlugin() + "");
            }
        }));
        actionPanel.add(selectedLabel);
        actionFrame.add(actionPanel);
        actionFrame.setLocationRelativeTo(null);
        actionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        actionFrame.pack();
        actionFrame.setVisible(true);
    }
}