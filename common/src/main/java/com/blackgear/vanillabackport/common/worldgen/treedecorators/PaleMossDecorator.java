package com.blackgear.vanillabackport.common.worldgen.treedecorators;

import com.blackgear.vanillabackport.common.level.blocks.HangingMossBlock;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModTreeDecorators;
import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.List;

public class PaleMossDecorator extends TreeDecorator {
    public static final MapCodec<PaleMossDecorator> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
            Codec.floatRange(0.0F, 1.0F).fieldOf("leaves_probability").forGetter(decorator -> decorator.leavesProbability),
            Codec.floatRange(0.0F, 1.0F).fieldOf("trunk_probability").forGetter(decorator -> decorator.trunkProbability),
            Codec.floatRange(0.0F, 1.0F).fieldOf("ground_probability").forGetter(decorator -> decorator.groundProbability)
        ).apply(instance, PaleMossDecorator::new)
    );

    private final float leavesProbability;
    private final float trunkProbability;
    private final float groundProbability;

    public PaleMossDecorator(float leavesProbability, float trunkProbability, float groundProbability) {
        this.leavesProbability = leavesProbability;
        this.trunkProbability = trunkProbability;
        this.groundProbability = groundProbability;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.PALE_MOSS.get();
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        WorldGenLevel level = (WorldGenLevel) context.level();
        List<BlockPos> positions = Util.shuffledCopy(context.logs(), random);
        if (!positions.isEmpty()) {
            Mutable<BlockPos> mutable = new MutableObject<>(positions.getFirst());
            positions.forEach(pos -> {
                if (pos.getY() < mutable.getValue().getY()) {
                    mutable.setValue(pos);
                }
            });

            BlockPos position = mutable.getValue();
            if (random.nextFloat() < this.groundProbability) {
                level.registryAccess()
                    .lookup(Registries.CONFIGURED_FEATURE)
                    .flatMap(registry -> registry.get(TheGardenAwakensFeatures.PALE_MOSS_PATCH))
                    .ifPresent(reference ->
                        reference.value().place(level, level.getLevel().getChunkSource().getGenerator(), random, position.above())
                    );
            }

            context.logs().forEach(pos -> {
                if (random.nextFloat() < this.trunkProbability) {
                    BlockPos below = pos.below();
                    if (context.isAir(below)) {
                        addMossHanger(below, context);
                    }
                }
            });
            context.leaves().forEach(pos -> {
                if (random.nextFloat() < this.leavesProbability) {
                    BlockPos below = pos.below();
                    if (context.isAir(below)) {
                        addMossHanger(below, context);
                    }
                }
            });
        }
    }

    private static void addMossHanger(BlockPos pos, Context context) {
        while (context.isAir(pos.below()) && context.random().nextFloat() >= 0.5F) {
            context.setBlock(pos, ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState().setValue(HangingMossBlock.TIP, false));
            pos = pos.below();
        }

        context.setBlock(pos, ModBlocks.PALE_HANGING_MOSS.get().defaultBlockState().setValue(HangingMossBlock.TIP, true));
    }
}