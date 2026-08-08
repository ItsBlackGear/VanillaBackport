package com.blackgear.vanillabackport.common.level.worldgen.tree_decorators;

import com.blackgear.vanillabackport.common.level.block.HangingMossBlock;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.worldgen.ModTreeDecorators;
import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PaleMossDecorator extends TreeDecorator {
    public static final Codec<PaleMossDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.floatRange(0.0F, 1.0F).fieldOf("leaves_probability").forGetter(decorator -> decorator.leavesProbability),
        Codec.floatRange(0.0F, 1.0F).fieldOf("trunk_probability").forGetter(decorator -> decorator.trunkProbability),
        Codec.floatRange(0.0F, 1.0F).fieldOf("ground_probability").forGetter(decorator -> decorator.groundProbability)
    ).apply(instance, PaleMossDecorator::new));

    private final float leavesProbability;
    private final float trunkProbability;
    private final float groundProbability;
    
    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.PALE_MOSS.get();
    }

    public PaleMossDecorator(float leavesProbability, float trunkProbability, float groundProbability) {
        this.leavesProbability = leavesProbability;
        this.trunkProbability = trunkProbability;
        this.groundProbability = groundProbability;
    }

    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        WorldGenLevel level = (WorldGenLevel) context.level();
        List<BlockPos> logs = Util.shuffledCopy(context.logs(), random);
        if (!logs.isEmpty()) {
            BlockPos position = Collections.min(logs, Comparator.comparingInt(Vec3i::getY));
            if (random.nextFloat() < this.groundProbability) {
                level.registryAccess()
                    .lookup(Registries.CONFIGURED_FEATURE)
                    .flatMap(registry -> registry.get(TheGardenAwakensFeatures.PALE_MOSS_PATCH))
                    .ifPresent(feature -> feature.value().place(level, level.getLevel().getChunkSource().getGenerator(), random, position.above()));
            }

            context.logs().forEach(pos -> {
                if (random.nextFloat() < this.trunkProbability) {
                    BlockPos down = pos.below();
                    if (context.isAir(down)) {
                        addMossHanger(down, context);
                    }
                }
            });
            context.leaves().forEach(pos -> {
                if (random.nextFloat() < this.leavesProbability) {
                    BlockPos down = pos.below();
                    if (context.isAir(down)) {
                        addMossHanger(down, context);
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