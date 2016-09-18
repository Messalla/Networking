package com.github.messalla.network.protocol.packet;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class Handler {
    public final HashMap<Class<?>, Method> handlerMethods =
            new HashMap<>();

    public final void init() {
        for(Method method : this.getClass().getMethods()) {
            if(method.getAnnotationsByType(PacketHandler.class).length == 0) {
                continue;
            }
            handlerMethods.put(method.getParameterTypes()[1], method);
        }
    }

    @org.jetbrains.annotations.Nullable
    public final Method getHandlerMethods(Packet packet) {
        if(handlerMethods.containsKey(packet.getClass()))
            return handlerMethods.get(packet.getClass());
        return null;
    }
}
