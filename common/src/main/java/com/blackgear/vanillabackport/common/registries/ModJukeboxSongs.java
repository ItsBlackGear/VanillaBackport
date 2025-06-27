package com.blackgear.vanillabackport.common.registries;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.JukeboxSong;

public class ModJukeboxSongs {
    public static final ResourceKey<JukeboxSong> TEARS = create("tears");
    public static final ResourceKey<JukeboxSong> LAVA_CHICKEN = create("lava_chicken");

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        register(context, TEARS, ModSoundEvents.MUSIC_DISC_TEARS.get(), 175, 10);
        register(context, LAVA_CHICKEN, ModSoundEvents.MUSIC_DISC_LAVA_CHICKEN.get(), 134, 9);
    }

    private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, SoundEvent soundEvent, int lengthInSeconds, int comparatorOutput) {
        context.register(key, new JukeboxSong(Holder.direct(soundEvent), Component.translatable(Util.makeDescriptionId("jukebox_song", key.location())), (float) lengthInSeconds, comparatorOutput));
    }

    private static ResourceKey<JukeboxSong> create(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, VanillaBackport.vanilla(name));
    }
}