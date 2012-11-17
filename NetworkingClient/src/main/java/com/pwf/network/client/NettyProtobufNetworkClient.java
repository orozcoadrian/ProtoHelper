package com.pwf.network.client;

import com.google.protobuf.MessageLite;
import com.pwf.plugin.PluginInformation;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.plugin.network.client.NetworkClientSettings;
import com.pwf.plugin.network.client.NetworkEventListener;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 *
 * @author mfullen
 */
public class NettyProtobufNetworkClient<M extends MessageLite> implements
        NetworkClientPlugin<M>,
        ErrorHandler
{
    private PluginManagerLite pluginManager = null;
    private ClientBootstrap bootstrap = null;
    private ChannelFuture future = null;
    private Channel channel = null;
    private M messageLite = null;
    private Collection<NetworkEventListener<M>> listeners = new ArrayList<NetworkEventListener<M>>();
    private PluginInformation pluginInformation = new PluginInformation()
    {
        public String getName()
        {
            return "Protobuf Netty Plugin";
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
            return NettyProtobufNetworkClient.class.getName();
        }
    };

    public void setMessageType(M messageLite)
    {
        this.messageLite = messageLite;
    }

    public void connect(NetworkClientSettings networkClientSettings)
    {
        this.bootstrap.setPipelineFactory(new ChannelPipelineFactoryImpl(messageLite, this));
        future = bootstrap.connect(new InetSocketAddress(networkClientSettings.getIpAddress(), networkClientSettings.getPort()));
        channel = future.awaitUninterruptibly().getChannel();

        if (!future.isSuccess())
        {
            this.pluginManager.reportError(this, future.getCause());
            //future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
        }
        for (NetworkEventListener<M> networkEventListener : listeners)
        {
            networkEventListener.onClientConnected();
        }
    }

    public void disconnect()
    {
        // Close the connection.  Make sure the close operation ends because
        // all I/O operations are asynchronous in Netty.
        channel.close().awaitUninterruptibly();

        // Shut down all thread pools to exit.
        bootstrap.releaseExternalResources();
        for (NetworkEventListener<M> networkEventListener : listeners)
        {
            networkEventListener.onClientDisconnected();
        }
    }

    public boolean isConnected()
    {
        return this.channel != null && this.channel.isConnected();
    }

    public void onLoaded(PluginManagerLite pluginManager)
    {
        this.pluginManager = pluginManager;
        this.bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    }

    public void onActivated()
    {
    }

    public void onDeactivated()
    {
        this.disconnect();
    }

    public PluginInformation getPluginInformation()
    {
        return pluginInformation;
    }

    public void onHandleMessageReceived(M message)
    {
        for (NetworkEventListener<M> networkEventListener : listeners)
        {
            networkEventListener.onMessageReceived(message);
        }
    }

    public void sendMessage(M message)
    {
        ChannelFuture lastWriteFuture = channel.write(message);

        // Wait until all messages are flushed before closing the channel.
        if (lastWriteFuture != null)
        {
            lastWriteFuture.awaitUninterruptibly();
        }
        for (NetworkEventListener<M> networkEventListener : listeners)
        {
            networkEventListener.onMessageSent(message);
        }
    }

    @Override
    public String toString()
    {
        PluginInformation pluginInfo = this.getPluginInformation();
        return String.format("%s (%s) %s © %s", pluginInfo.getName(), pluginInfo.getIdentifier(), pluginInfo.getVersion(),
                pluginInfo.getProvider());
    }

    public void onErrorOccured(Throwable e)
    {
        this.pluginManager.reportError(this, e);
    }

    public void addNetworkEventListener(NetworkEventListener<M> listener)
    {
        this.listeners.add(listener);
    }

    public void removeNetworkEventListener(NetworkEventListener<M> listener)
    {
        this.listeners.remove(listener);
    }

    public Collection<NetworkEventListener<M>> getListeners()
    {
        return Collections.unmodifiableCollection(this.listeners);
    }
}
