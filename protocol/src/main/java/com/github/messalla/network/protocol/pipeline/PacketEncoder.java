package com.github.messalla.network.protocol.pipeline;

import com.github.messalla.network.protocol.Protocol;
import com.github.messalla.network.protocol.packet.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          Packet packet,
                          ByteBuf byteBuf) throws Exception {
        int packetID = Protocol.getIdViaPacket(packet.getClass());
        if(packetID > -1) {
            byteBuf.writeInt(packetID);
            packet.write(new ByteBufOutputStream(byteBuf));
        }
    }
}
