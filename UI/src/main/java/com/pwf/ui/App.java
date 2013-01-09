//package com.pwf.ui;
//
//import com.google.protobuf.Message.Builder;
//import com.google.protobuf.TextFormat;
//import com.pwf.core.Engine;
//import com.pwf.core.EngineConfiguration;
//import com.pwf.core.NoLoadedMessagesException;
//import com.pwf.core.ProtoFilter;
//import com.pwf.ls.messaging.ExampleMessageProto;
//import com.pwf.plugin.ErrorEventListener;
//import com.pwf.plugin.Plugin;
//import com.pwf.plugin.PluginManager;
//import com.pwf.plugin.PluginManagerFactory;
//import com.pwf.plugin.network.client.NetworkClientPlugin;
//import com.pwf.plugin.network.client.NetworkClientSettings;
//import com.pwf.protohelper.platform.CoreEnginePHP;
//import com.pwf.protohelper.platform.UserInterfacePHP;
//import com.pwf.protohelper.platform.ui.ConsoleUserInterface;
//import java.awt.BorderLayout;
//import java.lang.reflect.InvocationTargetException;
//import java.net.MalformedURLException;
//import java.util.*;
//import javax.swing.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Hello world!
// *
// */
//public class App
//{
//    private static final Logger logger = LoggerFactory.getLogger(App.class);
//
//    public static void main(String[] args) throws ClassNotFoundException,
//                                                  InstantiationException,
//                                                  IllegalAccessException,
//                                                  NoSuchMethodException,
//                                                  IllegalArgumentException,
//                                                  InvocationTargetException,
//                                                  MalformedURLException,
//                                                  NoLoadedMessagesException
//    {
//        String classpath = System.getProperty("java.class.path");
//        logger.trace("Classpath=" + classpath);
//        logger.trace("userdir=" + System.getProperty("user.dir"));
//        ExampleMessageProto.Example message = ExampleMessageProto.Example.newBuilder().setId(2100).setJob("Software Engineer 2").setName("Mike").build();
//        String shortDebugString = TextFormat.shortDebugString(message);
//        System.out.println(shortDebugString);
//        PluginManager manager = PluginManagerFactory.createPluginManager();
//
//        manager.addErrorHandler(new ErrorEventListener()
//        {
//            @Override
//            public void onErrorOccurred(Plugin plugin, Throwable exception)
//            {
//                //error happened
//                logger.trace("Error: " + exception.getMessage());
//            }
//        });
//
//        manager.loadAllPlugins();
//        for (Plugin plugin : manager.getPlugins())
//        {
//            logger.debug("Plugin: " + plugin);
//        }
//        NetworkClientPlugin client = manager.getPlugin(NetworkClientPlugin.class);
//        if (client != null)
//        {
//            client.setMessageType(ExampleMessageProto.Example.getDefaultInstance());
//            manager.activate(client);
//
//
//            client.connect(new NetworkClientSettingsImpl());
//            client.sendMessage(message);
//        }
//        else
//        {
//            logger.trace("NETWORK PLUGIN IS NULL");
//        }
//        NetworkClientPlugin clonePlugin = manager.clonePlugin(client.getClass());
//        clonePlugin.setMessageType(ExampleMessageProto.Example.getDefaultInstance());
//
//        clonePlugin.onActivated();
//        clonePlugin.connect(new NetworkClientSettingsImpl());
//
//        clonePlugin.sendMessage(message);
//
//
//
//        long start = System.currentTimeMillis();
//        CoreEnginePHP coreEnginePHP = manager.getPlugin(CoreEnginePHP.class);
//        Engine engine = coreEnginePHP.getEngine(new EngineConfigurationImpl());
//        boolean findMessagesOnClassth = engine.findMessagesOnClasspath();
//
//          DefaultListModel listModel = new DefaultListModel();
////        Collection<Builder> protoBuilders = engine.getProtoBuilders();
////
////
////
////        for (Builder builder : protoBuilders)
////        {
////            listModel.addElement(builder);
////        }
//
//        long finish = System.currentTimeMillis();
//
//        logger.debug("Took this many MS: " + (finish - start));
//
//
//        UserInterfacePHP ui = manager.getPlugin(UserInterfacePHP.class);
//
//        if (ui == null)
//        {
//            ui = new ConsoleUserInterface();
//            manager.addPlugin(ui);
//            manager.activate(ui);
//        }
//
//        ui.startInterface(engine);
//
//
//        JList list = new JList(listModel);
//
//        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.add(scrollPane, BorderLayout.CENTER);
//
//        JFrame frame = new JFrame("Protos");
//        frame.add(panel);
//        frame.pack();
//        frame.setVisible(true);
//
//    }
//
//    private static class NetworkClientSettingsImpl implements
//            NetworkClientSettings
//    {
//        public NetworkClientSettingsImpl()
//        {
//        }
//
//        @Override
//        public int getPort()
//        {
//            return 5011;
//        }
//
//        @Override
//        public boolean isSSL()
//        {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//
//        @Override
//        public String getIpAddress()
//        {
//            return "localhost";
//        }
//    }
//
//    static class EngineConfigurationImpl implements EngineConfiguration
//    {
//        public EngineConfigurationImpl()
//        {
//        }
//
//        @Override
//        public String getLoadDirectory()
//        {
//            return "lib";
//        }
//
//        @Override
//        public Collection<ProtoFilter> getProtoFilters()
//        {
//            throw new UnsupportedOperationException("Not supported yet.");
//        }
//    }
//}
