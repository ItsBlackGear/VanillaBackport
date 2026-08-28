package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

public class LeafLitterFeature extends Feature<LeafLitterConfiguration> {
    private static final int MIN_TRUNK_HEIGHT = 2;
    
    public LeafLitterFeature(Codec<LeafLitterConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<LeafLitterConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        ChunkPos chunkPos = new ChunkPos(origin);
        int minX = chunkPos.getMinBlockX();
        int minZ = chunkPos.getMinBlockZ();
        
        boolean placedAny = false;
        
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int x = minX + dx;
                int z = minZ + dz;
                
                BlockPos treeBase = findTreeBase(level, x, z);
                if (treeBase != null) {
                    placedAny |= placeLeafLitterAround(level, random, treeBase, context.config());
                }
            }
        }
        
        return placedAny;
    }
    
    @Nullable
    private BlockPos findTreeBase(WorldGenLevel level, int x, int z) {
        int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
        int bottom = level.getMinBuildHeight();
        
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, top, z);
        int consecutiveLogs = 0;
        
        for (int y = top; y > bottom; y--) {
            pos.setY(y);
            
            if (!isVerticalLog(level.getBlockState(pos))) {
                return null;
            }
            
            consecutiveLogs++;
            
            if (level.getBlockState(pos.below()).is(BlockTags.DIRT)) {
                if (consecutiveLogs < MIN_TRUNK_HEIGHT) return null;
                
                if (!hasLeafCanopyAbove(level, x, z, top)) {
                    return null;
                }
                
                return pos.immutable();
            }
        }
        
        return null;
    }
    
    private static boolean isVerticalLog(BlockState state) {
        return state.is(BlockTags.LOGS) && state.getOptionalValue(BlockStateProperties.AXIS)
            .map(axis -> axis == Direction.Axis.Y)
            .orElse(true);
    }
    
    private static boolean hasLeafCanopyAbove(WorldGenLevel level, int x, int z, int trunkTopY) {
        int leafTop = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) - 1;
        
        if (leafTop <= trunkTopY) {
            return false;
        }
        
        BlockState leafState = level.getBlockState(new BlockPos(x, leafTop, z));
        return leafState.is(BlockTags.LEAVES) && leafState.is(ModBlockTags.ALLOWS_LEAF_LITTER);
    }
    
    private boolean placeLeafLitterAround(WorldGenLevel level, RandomSource random, BlockPos base, LeafLitterConfiguration config) {
        BoundingBox bb = new BoundingBox(base.getX() - config.radius(), base.getY() - config.height(), base.getZ() - config.radius(), base.getX() + config.radius(), base.getY() + config.height(), base.getZ() + config.radius());
        
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        boolean placedAny = false;
        
        for (int i = 0; i < config.tries(); i++) {
            pos.set(
                random.nextIntBetweenInclusive(bb.minX(), bb.maxX()),
                random.nextIntBetweenInclusive(bb.minY(), bb.maxY()),
                random.nextIntBetweenInclusive(bb.minZ(), bb.maxZ())
            );
            
            if (!level.ensureCanWrite(pos)) continue;
            
            if (tryPlaceLeafLitter(level, random, pos, config)) {
                placedAny = true;
            }
        }
        
        return placedAny;
    }
    
    private boolean tryPlaceLeafLitter(WorldGenLevel level, RandomSource random, BlockPos pos, LeafLitterConfiguration config) {
        if (!level.getBlockState(pos).isAir()) return false;
        
        BlockPos below = pos.below();
        if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) return false;
        
        if (level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos).getY() > pos.getY()) {
            return false;
        }
        
        BlockState litterState = config.blockStateProvider().getState(random, pos);
        
        if (!litterState.canSurvive(level, pos)) return false;
        
        level.setBlock(pos, litterState, Block.UPDATE_CLIENTS);
        return true;
    }
}