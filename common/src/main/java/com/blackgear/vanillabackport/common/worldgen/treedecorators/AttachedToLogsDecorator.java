package com.blackgear.vanillabackport.common.worldgen.treedecorators;

import com.blackgear.vanillabackport.common.registries.ModTreeDecorators;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class AttachedToLogsDecorator extends TreeDecorator {
    public static final Codec<AttachedToLogsDecorator> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(decorator -> decorator.probability),
            BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(decorator -> decorator.blockProvider),
            ExtraCodecs.nonEmptyList(Direction.CODEC.listOf()).fieldOf("directions").forGetter(decorator -> decorator.directions)
        )
        .apply(instance, AttachedToLogsDecorator::new)
    );
    private final float probability;
    private final BlockStateProvider blockProvider;
    private final List<Direction> directions;

    public AttachedToLogsDecorator(float probability, BlockStateProvider blockProvider, List<Direction> directions) {
        this.probability = probability;
        this.blockProvider = blockProvider;
        this.directions = directions;
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();

        for (BlockPos pos : Util.shuffledCopy(context.logs(), random)) {
            Direction direction = Util.getRandom(this.directions, random);
            BlockPos offset = pos.relative(direction);

            if (random.nextFloat() <= this.probability && context.isAir(offset)) {
                context.setBlock(offset, this.blockProvider.getState(random, offset));
            }
        }
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.ATTACHED_TO_LOGS.get();
    }
}
