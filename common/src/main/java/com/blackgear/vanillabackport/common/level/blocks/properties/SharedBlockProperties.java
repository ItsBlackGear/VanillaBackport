package com.blackgear.vanillabackport.common.level.blocks.properties;

import com.blackgear.vanillabackport.client.registries.ModSoundTypes;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class SharedBlockProperties {
    public static final Properties SULFUR = Properties.of()
        .sound(ModSoundTypes.SULFUR)
        .mapColor(MapColor.COLOR_YELLOW)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .strength(1.5F, 6.0F);

    public static final Properties CINNABAR = Properties.of()
        .sound(ModSoundTypes.CINNABAR)
        .mapColor(MapColor.COLOR_RED)
        .instrument(NoteBlockInstrument.BASEDRUM)
        .requiresCorrectToolForDrops()
        .strength(1.5F, 6.0F);
}