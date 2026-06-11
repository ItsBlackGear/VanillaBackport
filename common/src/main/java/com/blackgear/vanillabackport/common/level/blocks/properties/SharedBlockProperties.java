package com.blackgear.vanillabackport.common.level.blocks.properties;

import com.blackgear.vanillabackport.client.registries.ModSoundTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class SharedBlockProperties {
    public static final Properties PALE_OAK = Properties.of()
        .mapColor(MapColor.QUARTZ)
        .instrument(NoteBlockInstrument.BASS)
        .strength(2.0F, 3.0F)
        .sound(SoundType.WOOD)
        .ignitedByLava();
    
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
    
    public static Properties logProperties(MapColor topColor, MapColor sideColor, SoundType sound) {
        return Properties.of()
            .mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : sideColor)
            .instrument(NoteBlockInstrument.BASS)
            .strength(2.0F)
            .sound(sound)
            .ignitedByLava();
    }
    
    public static Properties buttonProperties() {
        return Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY);
    }
    
    public static Properties flowerPotProperties() {
        return Properties.of()
            .instabreak()
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY);
    }
}