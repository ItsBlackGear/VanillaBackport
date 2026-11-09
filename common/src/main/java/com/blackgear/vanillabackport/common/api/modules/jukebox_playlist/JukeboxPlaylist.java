package com.blackgear.vanillabackport.common.api.modules.jukebox_playlist;

import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.OptionalInt;

public class JukeboxPlaylist {
    public static final BooleanProperty PLAYLIST_MODE = BooleanProperty.create("playlist_mode");
    
    public static boolean isInPlaylistMode(LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState blockAbove = level.getBlockState(above);
        BlockEntity entityAbove = level.getBlockEntity(above);
        return blockAbove.is(ModBlockTags.JUKEBOX_CONTAINERS)
            && entityAbove instanceof Container
            && (!(blockAbove.getBlock() instanceof ChestBlock) || ChestBlock.getBlockType(blockAbove) == DoubleBlockCombiner.BlockType.SINGLE);
    }
    
    public static boolean isMusicDisc(ItemStack stack) {
        return stack.has(DataComponents.JUKEBOX_PLAYABLE);
    }
    
    public static OptionalInt findNextPlaylistPosition(Container container, int startPosition) {
        int startIndex = Math.clamp(startPosition, -1, container.getContainerSize());
        for (int offset = 1; offset <= container.getContainerSize(); offset++) {
            int slot = (startIndex + offset) % container.getContainerSize();
            if (isMusicDisc(container.getItem(slot))) {
                return OptionalInt.of(slot);
            }
        }
        return OptionalInt.empty();
    }
    
    public static void tickWithoutRecord(Level level, BlockPos pos, BlockState state, JukeboxBlockEntity jukebox) {
        ((PlaylistHolder) jukebox).vb$tryToAdvancePlaylist(false);
    }
}