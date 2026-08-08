package com.blackgear.vanillabackport.common.level.worldgen.tree_decorators;

import com.blackgear.vanillabackport.common.registries.worldgen.ModTreeDecorators;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;

public class PlaceOnGroundDecorator extends TreeDecorator {
    public static final Codec<PlaceOnGroundDecorator> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(decorator -> decorator.tries),
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").orElse(2).forGetter(decorator -> decorator.radius),
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("height").orElse(1).forGetter(decorator -> decorator.height),
        BlockStateProvider.CODEC.fieldOf("block_state_provider").forGetter(decorator -> decorator.blockStateProvider)
    ).apply(instance, PlaceOnGroundDecorator::new));
    private final int tries;
    private final int radius;
    private final int height;
    private final BlockStateProvider blockStateProvider;

    public PlaceOnGroundDecorator(int tries, int radius, int height, BlockStateProvider blockStateProvider) {
        this.tries = tries;
        this.radius = radius;
        this.height = height;
        this.blockStateProvider = blockStateProvider;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.PLACE_ON_GROUND.get();
    }

    @Override
    public void place(TreeDecorator.Context context) {
        List<BlockPos> blockPositions = Lists.newArrayList();
        List<BlockPos> roots = context.roots();
        List<BlockPos> logs = context.logs();
        if (roots.isEmpty()) {
            blockPositions.addAll(logs);
        } else if (!logs.isEmpty() && roots.get(0).getY() == logs.get(0).getY()) {
            blockPositions.addAll(logs);
            blockPositions.addAll(roots);
        } else {
            blockPositions.addAll(roots);
        }

        if (!blockPositions.isEmpty()) {
            BlockPos blockPos = blockPositions.get(0);
            int minY = blockPos.getY();
            int minX = blockPos.getX();
            int maxX = blockPos.getX();
            int minZ = blockPos.getZ();
            int maxZ = blockPos.getZ();

            for (BlockPos position : blockPositions) {
                if (position.getY() == minY) {
                    minX = Math.min(minX, position.getX());
                    maxX = Math.max(maxX, position.getX());
                    minZ = Math.min(minZ, position.getZ());
                    maxZ = Math.max(maxZ, position.getZ());
                }
            }

            RandomSource random = context.random();
            BoundingBox bb = new BoundingBox(minX - this.radius, minY - this.height, minZ - this.radius, maxX + this.radius, minY + this.height, maxZ + this.radius);
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            for (int i = 0; i < this.tries; i++) {
                pos.set(
                    random.nextIntBetweenInclusive(bb.minX(), bb.maxX()),
                    random.nextIntBetweenInclusive(bb.minY(), bb.maxY()),
                    random.nextIntBetweenInclusive(bb.minZ(), bb.maxZ())
                );
                this.attemptToPlaceBlockAbove(context, pos);
            }
        }
    }

    private void attemptToPlaceBlockAbove(TreeDecorator.Context context, BlockPos pos) {
        BlockPos abovePos = pos.above();
        if (context.level().isStateAtPosition(abovePos, state -> state.isAir() || state.is(Blocks.VINE))
            && context.level().isStateAtPosition(pos, state -> state.isSolidRender((BlockGetter) context.level(), pos))
            && context.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).getY() <= abovePos.getY()) {
            context.setBlock(abovePos, this.blockStateProvider.getState(context.random(), abovePos));
        }
    }
}