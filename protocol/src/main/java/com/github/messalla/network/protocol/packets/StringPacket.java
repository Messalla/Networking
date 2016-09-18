package com.github.messalla.network.protocol.packets;

import com.github.messalla.network.protocol.packet.Packet;

import java.io.IOException;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StringPacket extends Packet {
    private String string;

    public StringPacket() {
    }

    @Override
    public void read(ByteBufInputStream byteBuf) throws IOException {
        string = byteBuf.readUTF();
    }

    @Override
    public void write(ByteBufOutputStream byteBuf) throws IOException {
        byteBuf.writeUTF(string);
    }
}
