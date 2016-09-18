package com.github.messalla.network.protocol;

import com.github.messalla.network.protocol.packet.Packet;
import com.github.messalla.network.protocol.packets.StringPacket;

import lombok.Getter;

public enum Protocol {
    STRING_PACKET(1, StringPacket.class);

    @Getter
    private int packetId;
    @Getter private Class<? extends Packet> packetClass;

    Protocol(int packetId, Class<? extends Packet> packetClass) {
        this.packetId = packetId;
        this.packetClass = packetClass;
    }

    public static Class<? extends Packet> getPacketViaId(int packetId) {
        for(Protocol protocol : values())
            if(protocol.getPacketId() ==  packetId)
                return protocol.getPacketClass();
        return null;
    }

    public static int getIdViaPacket(Class<? extends Packet> packetClass) {
        for(Protocol protocol : values())
            if(protocol.getPacketClass().equals(packetClass))
                return protocol.getPacketId();
        return -1;
    }
}
