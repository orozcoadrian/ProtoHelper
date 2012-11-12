package com.pwf.ui;

import com.google.protobuf.Message;
import com.pwf.core.Engine;
import com.pwf.core.EngineData;
import com.pwf.core.NoLoadedMessagesException;
import com.pwf.plugin.PluginInformation;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.protohelper.platform.UserInterfacePHP;
import java.awt.BorderLayout;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author mfullen
 */
public class ProtoHelperSwingUI implements UserInterfacePHP
{
    private PluginManagerLite manager = null;
    private final PluginInformation pluginInformation = new PluginInformationImpl();
    private Engine engine = null;

    @Override
    public void startInterface(Engine coreEngine)
    {
        this.engine = coreEngine;

        boolean findMessagesOnClassth = engine.findMessagesOnClasspath();
        Collection<EngineData> allEngineData = engine.getAllEngineData();


        DefaultListModel listModel = new DefaultListModel();
        for (EngineData engineData : allEngineData)
        {
            listModel.addElement(engineData);
        }


        JList list = new JList(listModel);

        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        JFrame frame = new JFrame("Protos");
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void onLoaded(PluginManagerLite pluginManager)
    {
        this.manager = pluginManager;
    }

    @Override
    public void onActivated()
    {
    }

    @Override
    public void onDeactivated()
    {
    }

    @Override
    public PluginInformation getPluginInformation()
    {
        return this.pluginInformation;
    }

    protected static class PluginInformationImpl implements PluginInformation
    {
        public PluginInformationImpl()
        {
        }

        @Override
        public String getName()
        {
            return "ProtoHelper Console UI Plugin";
        }

        @Override
        public String getVersion()
        {
            return "1.0.0";
        }

        @Override
        public String getProvider()
        {
            return PluginInformation.DEFAULT_PROVIDER;
        }

        @Override
        public String getIdentifier()
        {
            return ProtoHelperSwingUI.class.getName();
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
