package com.blackgear.vanillabackport.common.worldgen.treedecorators;

import com.blackgear.vanillabackport.common.level.blocks.CreakingHeartBlock;
import com.blackgear.vanillabackport.common.level.blocks.blockstates.CreakingHeartState;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModTreeDecorators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CreakingHeartDecorator extends TreeDecorator {
    public static final MapCodec<CreakingHeartDecorator> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(decorator -> decorator.probability))
            .apply(instance, CreakingHeartDecorator::new));

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
                List<BlockPos> validPositions = new ArrayList<>(context.logs());
                Collections.shuffle(validPositions);

                Optional<BlockPos> validPosition = validPositions.stream().filter(pos -> {
                    for (Direction direction : Direction.values()) {
                        if (!context.level().isStateAtPosition(pos.relative(direction), state -> state.is(BlockTags.LOGS))) {
                            return false;
                        }
                    }

                    return true;
                }).findFirst();

                validPosition.ifPresent(pos ->
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