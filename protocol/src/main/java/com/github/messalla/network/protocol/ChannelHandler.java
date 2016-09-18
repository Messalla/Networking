package com.github.messalla.network.protocol;

import com.github.messalla.network.protocol.packet.Packet;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChannelHandler extends SimpleChannelInboundHandler<Packet> {
    private BlockingQueue<Packet> sendBeforConnected = new LinkedBlockingDeque<>();
    private Channel channel;
    private final PacketManager packetManager;

    public ChannelHandler(PacketManager packetManager) {
        this.packetManager = packetManager;
    }



    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
        for(Packet packet : sendBeforConnected) {
            this.channel.writeAndFlush(packet);
        }
    }

    public void send(Packet packet) {
        if(channel == null)
            sendBeforConnected.add(packet);
        else
            channel.write(packet);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext,
                                   Packet packet)
            throws Exception {
        packetManager.firePacket(channelHandlerContext, packet);
    }
}
