package com.github.messalla.network.server.handler;

import com.github.messalla.network.protocol.packet.Handler;
import com.github.messalla.network.protocol.packet.PacketHandler;
import com.github.messalla.network.protocol.packets.StringPacket;

import io.netty.channel.ChannelHandlerContext;

public class StringPacketHandler extends Handler {
    @PacketHandler
    public void onPacketInput(ChannelHandlerContext ctx, StringPacket packet) {
        System.out.println(packet.getString());
    }
}
