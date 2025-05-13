package com.blackgear.vanillabackport.client;

import com.blackgear.platform.core.util.config.ConfigBuilder;

public class ClientConfig {
    public final ConfigBuilder.ConfigValue<Boolean> fadeMusicOnPaleGarden;

    public ClientConfig(ConfigBuilder builder) {
        builder.push("The Garden Awakens");
        this.fadeMusicOnPaleGarden = builder.comment("Determine if the music should fade out when entering a pale garden").define("fadeMusicOnPaleGarden", true);
        builder.pop();
    }
}