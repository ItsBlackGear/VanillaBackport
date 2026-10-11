package com.blackgear.vanillabackport.common.level.worldgen.tree.foliage;

import com.blackgear.vanillabackport.common.registries.worldgen.ModFoliagePlacers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.function.Function;

public class PoplarFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<PoplarFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> foliagePlacerParts(instance).and(instance.group(
        IntProvider.codec(5, 16).fieldOf("height").forGetter(placer -> placer.height),
        Codec.floatRange(0.0F, 1.0F).fieldOf("side_hole_chance").forGetter(placer -> placer.sideHoleChance)
    )).apply(instance, PoplarFoliagePlacer::new));
    private final IntProvider height;
    private final float sideHoleChance;
    
    public PoplarFoliagePlacer(IntProvider radius, IntProvider offset, IntProvider height, float sideHoleChance) {
        super(radius, offset);
        this.height = height;
        this.sideHoleChance = sideHoleChance;
    }
    
    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.POPLAR_FOLIAGE_PLACER.get();
    }
    
    @Override
    protected void createFoliage(
        LevelSimulatedReader level,
        FoliageSetter blockSetter,
        RandomSource random,
        TreeConfiguration config,
        int maxFreeTreeHeight,
        FoliageAttachment attachment,
        int foliageHeight,
        int foliageRadius,
        int offset
    ) {
        boolean doubleTrunk = attachment.doubleTrunk();
        BlockPos foliagePos = attachment.pos().above(offset);
        int currentRadius = foliageRadius + attachment.radiusOffset() - 1;
        boolean flipRhombusShape = random.nextBoolean();
        this.placeLeavesRow(level, blockSetter, random, config, foliagePos, currentRadius - 2, foliageHeight - 1, doubleTrunk, foliageHeight, flipRhombusShape);
        this.placeLeavesRow(level, blockSetter, random, config, foliagePos, currentRadius - 1, foliageHeight - 2, doubleTrunk, foliageHeight, flipRhombusShape);
        this.placeLeavesRow(level, blockSetter, random, config, foliagePos, currentRadius - 1, foliageHeight - 3, doubleTrunk, foliageHeight, flipRhombusShape);
        
        for (int y = foliageHeight - 4; y >= 1; y--) {
            this.placeLeavesRow(level, blockSetter, random, config, foliagePos, currentRadius, y, doubleTrunk, foliageHeight, flipRhombusShape);
        }
        
        this.replaceLeavesWithLog(level, blockSetter, config, random, foliagePos, currentRadius, foliageHeight - 4, doubleTrunk, foliageHeight, flipRhombusShape);
        this.placeLeavesRow(level, blockSetter, random, config, foliagePos, currentRadius - 1, 0, doubleTrunk, foliageHeight, flipRhombusShape);
        this.placeLeavesRow(level, blockSetter, random, config, foliagePos, Mth.clamp(currentRadius - 2, 1, 2), -1, doubleTrunk, foliageHeight, flipRhombusShape);
    }
    
    private void replaceLeavesWithLog(
        LevelSimulatedReader level,
        FoliagePlacer.FoliageSetter foliageSetter,
        TreeConfiguration tree,
        RandomSource random,
        BlockPos origin,
        int currentRadius,
        int y,
        boolean doubleTrunk,
        int foliageHeight,
        boolean flipRhombusShape
    ) {
        int offset = doubleTrunk ? 1 : 0;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        
        for (int dx = -currentRadius; dx <= currentRadius + offset; dx++) {
            for (int dz = -currentRadius; dz <= currentRadius + offset; dz++) {
                int absDz = Mth.abs(dz);
                int absDx = Mth.abs(dx);
                if (isWithinRhombusShape(currentRadius, absDx, absDz, this.getCornerBlocksToCutForRhombusShape(dx, dz, currentRadius, this.shouldRowBePartialRhombusShape(foliageHeight, y), flipRhombusShape), 2)
                    && (absDz == 0 && currentRadius - absDx >= 4 || absDx == 0 && currentRadius - absDz >= 4)) {
                    pos.setWithOffset(origin, dx, y, dz);
                    tryPlaceLog(level, foliageSetter, random, tree, pos, getSidewaysStateModifier(Direction.fromAxisAndDirection(absDz == 0 ? Direction.Axis.X : Direction.Axis.Z, Direction.AxisDirection.POSITIVE)));
                }
            }
        }
    }
    
    private static void tryPlaceLog(
        LevelSimulatedReader level,
        FoliagePlacer.FoliageSetter foliageSetter,
        RandomSource random,
        TreeConfiguration tree,
        BlockPos pos,
        Function<BlockState, BlockState> stateModifier
    ) {
        if (level.isStateAtPosition(pos, state -> state.equals(tree.foliageProvider.getState(random, pos)))) {
            foliageSetter.set(pos, stateModifier.apply(tree.trunkProvider.getState(random, pos)));
        }
    }
    
    private static Function<BlockState, BlockState> getSidewaysStateModifier(Direction branchDirection) {
        return state -> state.trySetValue(RotatedPillarBlock.AXIS, branchDirection.getAxis());
    }
    
    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return this.height.sample(random);
    }
    
    private void placeLeavesRow(
        LevelSimulatedReader level,
        FoliagePlacer.FoliageSetter foliageSetter,
        RandomSource random,
        TreeConfiguration tree,
        BlockPos origin,
        int currentRadius,
        int y,
        boolean doubleTrunk,
        int foliageHeight,
        boolean flipRhombusShape
    ) {
        int offset = doubleTrunk ? 1 : 0;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        
        for (int dx = -currentRadius; dx <= currentRadius + offset; dx++) {
            for (int dz = -currentRadius; dz <= currentRadius + offset; dz++) {
                if (!this.shouldSkipLocation(random, dx, y, dz, currentRadius, foliageHeight, flipRhombusShape)) {
                    pos.setWithOffset(origin, dx, y, dz);
                    tryPlaceLeaf(level, foliageSetter, random, tree, pos);
                }
            }
        }
    }
    
    private boolean shouldSkipLocation(
        RandomSource random,
        int dx,
        int y,
        int dz,
        int currentRadius,
        int foliageHeight,
        boolean flipRhombusShape
    ) {
        boolean shouldRowBePartialRhombusShape = this.shouldRowBePartialRhombusShape(foliageHeight, y);
        int cornerBlocksToCutForRhombusShape = this.getCornerBlocksToCutForRhombusShape(dx, dz, currentRadius, shouldRowBePartialRhombusShape, flipRhombusShape);
        int absDx = Mth.abs(dx);
        int absDz = Mth.abs(dz);
        boolean isRhombusEdgeBlock = absDx == currentRadius || absDz == currentRadius;
        if (shouldRowBePartialRhombusShape && isRhombusEdgeBlock) {
            return true;
        } else {
            int additionalSideRemoval = random.nextFloat() <= this.sideHoleChance ? 1 : 0;
            return !isWithinRhombusShape(currentRadius, absDx, absDz, cornerBlocksToCutForRhombusShape, additionalSideRemoval);
        }
    }
    
    @Override
    protected boolean shouldSkipLocationSigned(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        throw new IllegalStateException("Overridden method needs more context");
    }
    
    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        throw new IllegalStateException("Overridden method needs more context");
    }
    
    private int getCornerBlocksToCutForRhombusShape(int x, int z, int currentRadius, boolean shouldRowBePartialRhombusShape, boolean flipRhombusShape) {
        boolean isSmallCornerOfShape = flipRhombusShape ? isLeftTopCornerOrRightLowerCorner(x, z) : isLeftLowerCornerOrRightTopCorner(x, z);
        return isSmallCornerOfShape ? currentRadius - 1 : (shouldRowBePartialRhombusShape ? currentRadius + 1 : currentRadius);
    }
    
    private static boolean isWithinRhombusShape(int currentRadius, int absDx, int absDz, int cornerBlocksToCutForRhombusShape, int additionalSideRemoval) {
        return absDx + absDz <= currentRadius * 2 - (cornerBlocksToCutForRhombusShape + additionalSideRemoval);
    }
    
    private static boolean isLeftLowerCornerOrRightTopCorner(int dx, int dz) {
        return dx > 0 && dz < 0 || dz > 0 && dx < 0;
    }
    
    private static boolean isLeftTopCornerOrRightLowerCorner(int dx, int dz) {
        return dx > 0 && dz > 0 || dz < 0 && dx < 0;
    }
    
    private boolean shouldRowBePartialRhombusShape(int foliageHeight, int y) {
        return foliageHeight - 1 == y || foliageHeight - 2 == y;
    }
}