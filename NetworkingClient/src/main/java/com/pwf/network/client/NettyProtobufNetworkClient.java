package com.pwf.network.client;

import com.google.protobuf.MessageLite;
import com.pwf.plugin.PluginInformation;
import com.pwf.plugin.PluginManagerLite;
import com.pwf.plugin.network.client.NetworkClientPlugin;
import com.pwf.plugin.network.client.NetworkClientSettings;
import java.net.InetSocketAddress;
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
        NetworkClientPlugin<M>
{
    private PluginManagerLite pluginManager = null;
    private ClientBootstrap bootstrap = null;
    private ChannelFuture future = null;
    private Channel channel = null;
    private M messageLite = null;
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
            return "com.pwf.netty.protobuf.client";
        }
    };

    public void setMessageType(M messageLite)
    {
        this.messageLite = messageLite;
    }

    public void connect(NetworkClientSettings networkClientSettings)
    {
        future = bootstrap.connect(new InetSocketAddress(networkClientSettings.getIpAddress(), networkClientSettings.getPort()));
        channel = future.awaitUninterruptibly().getChannel();

        if (!future.isSuccess())
        {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }
    }

    public void disconnect()
    {
        // Close the connection.  Make sure the close operation ends because
        // all I/O operations are asynchronous in Netty.
        channel.close().awaitUninterruptibly();

        // Shut down all thread pools to exit.
        bootstrap.releaseExternalResources();
    }

    public void onErrorDetected(Exception exeception)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isConnected()
    {
        return this.channel.isConnected();
    }

    public void initialize(PluginManagerLite pluginManager)
    {
        this.pluginManager = pluginManager;
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ChannelPipelineFactoryImpl(messageLite));
    }

    public void start()
    {
    }

    public void stop()
    {
        this.disconnect();
    }

    public PluginInformation getPluginInformation()
    {
        return pluginInformation;
    }

    public void onHandleMessageReceived(M message)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendMessage(M message)
    {
        ChannelFuture lastWriteFuture = channel.write(message);

        // Wait until all messages are flushed before closing the channel.
        if (lastWriteFuture != null)
        {
            lastWriteFuture.awaitUninterruptibly();
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
