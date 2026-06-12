package com.blackgear.vanillabackport.common.level.worldgen.tree_decorators;

import com.blackgear.vanillabackport.common.level.block.CreakingHeartBlock;
import com.blackgear.vanillabackport.common.level.block.states.CreakingHeartState;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.worldgen.ModTreeDecorators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreakingHeartDecorator extends TreeDecorator {
    public static final MapCodec<CreakingHeartDecorator> CODEC = Codec.floatRange(0.0F, 1.0F)
        .fieldOf("probability")
        .xmap(CreakingHeartDecorator::new, decorator -> decorator.probability);

    private final float probability;

    public CreakingHeartDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.CREAKING_HEART.get();
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        List<BlockPos> logs = context.logs();
        if (!logs.isEmpty()) {
            if (random.nextFloat() < this.probability) {
                List<BlockPos> heartPlacements = new ArrayList<>(context.logs());
                Util.shuffle(heartPlacements, random);
                Optional<BlockPos> targetPos = heartPlacements.stream().filter(pos -> {
                    for (Direction dir : Direction.values()) {
                        if (!context.level().isStateAtPosition(pos.relative(dir), state -> state.is(BlockTags.LOGS))) {
                            return false;
                        }
                    }

                    return true;
                }).findFirst();
                targetPos.ifPresent(pos ->
                    context.setBlock(
                        pos,
                        ModBlocks.CREAKING_HEART.get().defaultBlockState()
                            .setValue(CreakingHeartBlock.STATE, CreakingHeartState.DORMANT)
                            .setValue(CreakingHeartBlock.NATURAL, true)
                    )
                );
            }
        }
    }
}