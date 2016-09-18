package com.github.messalla.network.protocol.pipeline;

import com.github.messalla.network.protocol.Protocol;
import com.github.messalla.network.protocol.packet.Packet;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf,
                          List<Object> list) throws Exception {
        if(byteBuf instanceof EmptyByteBuf)
            return;
        int packetId = byteBuf.readInt();
        Class<? extends Packet> packetClass = Protocol.getPacketViaId(packetId);
        if(packetClass != null) {
            Packet packet = packetClass.newInstance();
            packet.read(new ByteBufInputStream(byteBuf));
            list.add(packet);
        }
    }
}
