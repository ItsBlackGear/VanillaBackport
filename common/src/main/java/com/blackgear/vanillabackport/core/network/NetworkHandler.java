package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.NetworkChannel;
import com.blackgear.platform.core.network.base.NetworkDirection;
import com.blackgear.vanillabackport.core.VanillaBackport;

public class NetworkHandler {
    public static final NetworkChannel DEFAULT_CHANNEL = new NetworkChannel(VanillaBackport.MOD_ID, 1, "network");

    public static void bootstrap() {
        DEFAULT_CHANNEL.registerPacket(NetworkDirection.TO_SERVER, ServerboundSelectBundleItemPacket.ID, ServerboundSelectBundleItemPacket.HANDLER, ServerboundSelectBundleItemPacket.class);
    }
}