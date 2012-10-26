package com.pwf.network.client;



import com.google.protobuf.MessageLite;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.WriteCompletionEvent;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import org.jboss.netty.handler.codec.protobuf.ProtobufDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufEncoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import org.jboss.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mfullen
 */
public class ChannelPipelineFactoryImpl implements ChannelPipelineFactory
{
    private static final Logger logger = LoggerFactory.getLogger(ChannelPipelineFactoryImpl.class);
    private final MessageLite messageLite;
    public static final String SSL = "ssl";
    public static final String COMPRESSOR = "deflater";
    public static final String DECOMPRESSOR = "inflater";
    public static final String FRAME_DECODER = "frameDecoder";
    public static final String FRAME_ENCODER = "frameEncoder";
    public static final String PROTOBUF_DECODER = "protobufDecoder";
    public static final String PROTOBUF_ENCODER = "protobufEncoder";

    public ChannelPipelineFactoryImpl(MessageLite messageLite)
    {
        this.messageLite = messageLite;
    }

    public ChannelPipeline getPipeline() throws Exception
    {
        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast(DECOMPRESSOR, new ZlibEncoder(ZlibWrapper.GZIP));
        pipeline.addLast(COMPRESSOR, new ZlibDecoder(ZlibWrapper.GZIP));

        pipeline.addLast(FRAME_DECODER, new ProtobufVarint32FrameDecoder());
        pipeline.addLast(PROTOBUF_DECODER, new ProtobufDecoder(messageLite));

        pipeline.addLast(FRAME_ENCODER, new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(PROTOBUF_ENCODER, new ProtobufEncoder());
        pipeline.addLast("handler", new SimpleChannelUpstreamHandler()
        {
            @Override
            public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                    throws Exception
            {
                logger.debug("Channel connected");
                super.channelConnected(ctx, e);
            }

            @Override
            public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
                    throws Exception
            {
                logger.debug("Channel closed");
                super.channelClosed(ctx, e);
            }

            @Override
            public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
                    throws Exception
            {
                logger.debug("Channel disconnected");
                super.channelDisconnected(ctx, e);
            }

            @Override
            public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
                    throws Exception
            {
                logMessageInfo((MessageLite) e.getMessage());
                super.messageReceived(ctx, e);
            }

            @Override
            public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
                    throws Exception
            {
                logger.debug("Written amount {}", e.getWrittenAmount());
                super.writeComplete(ctx, e);
            }

            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
                    throws Exception
            {
                logger.error("error", e);
                super.exceptionCaught(ctx, e);
            }
        });
        return pipeline;
    }

    public static void logMessageInfo(MessageLite message)
    {
        logger.debug("message Size {}", message.getSerializedSize());
        logger.debug("message received {}", message);
    }
}
