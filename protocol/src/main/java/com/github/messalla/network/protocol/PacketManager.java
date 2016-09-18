package com.github.messalla.network.protocol;

import com.github.messalla.network.protocol.packet.HandlerList;
import com.github.messalla.network.protocol.packet.Handler;
import com.github.messalla.network.protocol.packet.Packet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;

public class PacketManager {
    @Getter private final HandlerList handlers = new HandlerList();

    public void registerHandler(Handler handler) {
        handlers.addHandler(handler);
    }

    public void unregisterHandler(Handler handler) {
        handlers.removeHandler(handler);
    }

    public void firePacket(ChannelHandlerContext ctx, Packet packet) {
        for(Handler handler : handlers.asList()) {
            Method method = handler.getHandlerMethods(packet);
            if(method != null)
                try {
                    method.invoke(handler, ctx, packet);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
        }
    }
}
