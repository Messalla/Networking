package com.github.messalla.network.protocol.packet;

import java.util.ArrayList;
import java.util.List;

public class HandlerList {
    private volatile List<Handler> handlers = new ArrayList<>();

    public List<Handler> asList() {
        return handlers;
    }

    public void addHandler(Handler handler) {
        if(handlers.contains(handler))
            return;
        handlers.add(handler);
        handler.init();
    }

    public void removeHandler(Handler handler) {
        if(!handlers.contains(handler))
            return;
        handlers.remove(handler);
    }
}
