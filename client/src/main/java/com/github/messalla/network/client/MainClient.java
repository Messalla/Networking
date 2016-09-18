package com.github.messalla.network.client;

import com.github.messalla.network.protocol.ChannelHandler;
import com.github.messalla.network.protocol.Client;
import com.github.messalla.network.protocol.packets.StringPacket;
import com.github.messalla.network.protocol.pipeline.PacketDecoder;
import com.github.messalla.network.protocol.pipeline.PacketEncoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.AllArgsConstructor;

/**
 * Created by Leon on 18.09.2016.
 */
@AllArgsConstructor
public class MainClient extends Client {
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private String host;
    private int port;

    public void onEnable() {
        getChannelHandler().send(new StringPacket("Hallo Youtube, das ist ein Tutorial!"));
    }

    public void start() {
        service.execute(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            try {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            protected void initChannel(SocketChannel socketChannel)
                                    throws Exception {
                                MainClient.super.channelHandler = preparePipeline(socketChannel);
                                onEnable();
                            }
                        });
                ChannelFuture future = bootstrap.connect(host, port).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
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
    public static void main(String[] args) {
        MainClient mainClient = new MainClient("localhost", 13337);
        mainClient.start();
    }
}