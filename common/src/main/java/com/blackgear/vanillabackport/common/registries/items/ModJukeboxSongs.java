package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.api.registrar.bootstrap.BootstrapRegistrar;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;

public class ModJukeboxSongs {
    public static final BootstrapRegistrar<JukeboxSong> REGISTRIES = BootstrapRegistrar.create(Registries.JUKEBOX_SONG, VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<JukeboxSong> TEARS = register("tears", ModSoundEvents.MUSIC_DISC_TEARS, 175, 10);
    public static final ResourceKey<JukeboxSong> LAVA_CHICKEN = register("lava_chicken", ModSoundEvents.MUSIC_DISC_LAVA_CHICKEN, 134, 9);
    public static final ResourceKey<JukeboxSong> BOUNCE = register("bounce", ModSoundEvents.MUSIC_DISC_BOUNCE, 234, 8);

    private static ResourceKey<JukeboxSong> register(String name, Holder<SoundEvent> soundEvent, int lengthInSeconds, int comparatorOutput) {
        return REGISTRIES.resource(
            name,
            (context, key) -> new JukeboxSong(
                soundEvent,
                Component.translatable(Util.makeDescriptionId("jukebox_song", key)),
                (float) lengthInSeconds,
                comparatorOutput
            ));
    }
}