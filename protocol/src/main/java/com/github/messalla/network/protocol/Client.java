package com.github.messalla.network.protocol;

import lombok.Getter;

public abstract class Client extends PacketManager {
    @Getter public ChannelHandler channelHandler;

    public void onEnable() {
    }

    public void onDisable() {
    }
}
