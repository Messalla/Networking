package com.github.messalla.network.server;

import com.github.messalla.network.protocol.ChannelHandler;
import com.github.messalla.network.protocol.Server;
import com.github.messalla.network.protocol.pipeline.PacketDecoder;
import com.github.messalla.network.protocol.pipeline.PacketEncoder;
import com.github.messalla.network.server.handler.StringPacketHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.AllArgsConstructor;

/**
 * Created by Leon on 18.09.2016.
 */
@AllArgsConstructor
public class MainServer extends Server {
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private String host;
    private int port;

    @Override
    public void onEnable() {
        registerHandler(new StringPacketHandler());
    }

    public void start() {
        service.execute(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                preparePipeline(socketChannel);
                            }
                        })
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                ChannelFuture future = bootstrap.bind(host, port).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        });
    }

    private ChannelHandler preparePipeline(Channel channel) {
        ChannelHandler channelHandler = new ChannelHandler(this);

        channel.pipeline().addLast(new LengthFieldPrepender(4, true));
        channel.pipeline().addLast(new PacketDecoder());
        channel.pipeline().addLast(new PacketEncoder());
        channel.pipeline().addLast(channelHandler);
        return channelHandler;
    }

    public static void main( String[] args ) {
        MainServer mainServer = new MainServer("localhost", 13337);
        mainServer.start();
        mainServer.onEnable();
    }
}
