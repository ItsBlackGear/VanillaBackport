package com.blackgear.vanillabackport.common.worldgen.treedecorators;

import com.blackgear.vanillabackport.common.registries.ModTreeDecorators;
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
    public static final Codec<PlaceOnGroundDecorator> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(decorator -> decorator.tries),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").orElse(2).forGetter(decorator -> decorator.radius),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("height").orElse(1).forGetter(decorator -> decorator.height),
            BlockStateProvider.CODEC.fieldOf("block_state_provider").forGetter(decorator -> decorator.blockStateProvider)
        )
        .apply(instance, PlaceOnGroundDecorator::new)
    );
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
        List<BlockPos> list = Lists.newArrayList();
        List<BlockPos> list2 = context.roots();
        List<BlockPos> list3 = context.logs();
        if (list2.isEmpty()) {
            list.addAll(list3);
        } else if (!list3.isEmpty() && list2.get(0).getY() == list3.get(0).getY()) {
            list.addAll(list3);
            list.addAll(list2);
        } else {
            list.addAll(list2);
        }

        if (!list.isEmpty()) {
            BlockPos blockPos = list.get(0);
            int i = blockPos.getY();
            int j = blockPos.getX();
            int k = blockPos.getX();
            int l = blockPos.getZ();
            int m = blockPos.getZ();

            for (BlockPos blockPos2 : list) {
                if (blockPos2.getY() == i) {
                    j = Math.min(j, blockPos2.getX());
                    k = Math.max(k, blockPos2.getX());
                    l = Math.min(l, blockPos2.getZ());
                    m = Math.max(m, blockPos2.getZ());
                }
            }

            RandomSource randomSource = context.random();
            BoundingBox base = new BoundingBox(j, i, l, k, i, m);
            BoundingBox boundingBox = new BoundingBox(
                base.minX() - this.radius, base.minY() - this.height, base.minZ() - this.radius,
                base.maxX() + this.radius, base.maxY() + this.height, base.maxZ() + this.radius
            );
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

            for (int n = 0; n < this.tries; n++) {
                mutableBlockPos.set(
                    randomSource.nextIntBetweenInclusive(boundingBox.minX(), boundingBox.maxX()),
                    randomSource.nextIntBetweenInclusive(boundingBox.minY(), boundingBox.maxY()),
                    randomSource.nextIntBetweenInclusive(boundingBox.minZ(), boundingBox.maxZ())
                );
                this.attemptToPlaceBlockAbove(context, mutableBlockPos);
            }
        }
    }

    private void attemptToPlaceBlockAbove(TreeDecorator.Context context, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.above();
        if (context.level().isStateAtPosition(blockPos2, blockState -> blockState.isAir() || blockState.is(Blocks.VINE))
            && context.level().isStateAtPosition(blockPos, state -> state.isSolidRender((BlockGetter) context.level(), blockPos))
            && context.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockPos).getY() <= blockPos2.getY()) {
            context.setBlock(blockPos2, this.blockStateProvider.getState(context.random(), blockPos2));
        }
    }
}
