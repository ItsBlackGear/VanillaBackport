package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;

public class EyeblossomFlowerPotBlock extends FlowerPotBlock {
    public EyeblossomFlowerPotBlock(Block content, Properties properties) {
        super(content, properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.isRandomlyTicking(state) && level.dimensionType().natural()) {
            boolean hasOpenEyeblossom = this.getContent() == ModBlocks.OPEN_EYEBLOSSOM.get();
            boolean isNaturalNight = CreakingHeartBlock.isNaturalNight(level);

            if (hasOpenEyeblossom != isNaturalNight) {
                level.setBlock(pos, this.opposite(state), 3);
                EyeblossomBlock.Type type = EyeblossomBlock.Type.fromBoolean(hasOpenEyeblossom).transform();
                type.spawnTransformParticle(level, pos, random);
                level.playSound(null, pos, type.longSwitchSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

        super.randomTick(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.is(ModBlocks.POTTED_OPEN_EYEBLOSSOM.get()) || state.is(ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get());
    }

    private BlockState opposite(BlockState state) {
        if (state.is(ModBlocks.POTTED_OPEN_EYEBLOSSOM.get())) {
            return ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get().defaultBlockState();
        } else {
            return state.is(ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get())
                ? ModBlocks.POTTED_OPEN_EYEBLOSSOM.get().defaultBlockState()
                : state;
        }
    }
}