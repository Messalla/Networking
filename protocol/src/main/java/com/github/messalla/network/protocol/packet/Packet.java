package com.github.messalla.network.protocol.packet;

import java.io.IOException;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

public abstract class Packet {
    public abstract void read(ByteBufInputStream byteBuf) throws IOException;
    public abstract void write(ByteBufOutputStream byteBuf) throws IOException;
}
