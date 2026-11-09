package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CrevasseFeature extends Feature<CrevasseConfiguration> {
    public CrevasseFeature(Codec<CrevasseConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<CrevasseConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        CrevasseConfiguration config = context.config();
        RandomSource random = context.random();
        int carveDepth = config.carveDepth().sample(random);
        if (origin.getY() <= level.getMinBuildHeight() + carveDepth) {
            return false;
        } else {
            int branchLength = config.maxLength() / 2;
            float yaw = random.nextFloat() * Mth.TWO_PI;
            int fillDepth = config.fillDepth().sample(random);
            Long2ObjectOpenHashMap<CarveColumn> columns = new Long2ObjectOpenHashMap<>(50);
            
            for (int direction = 0; direction < 2; direction++) {
                this.appendBranchColumns(columns, level, origin, branchLength, carveDepth, yaw + direction * Mth.PI, random, config);
            }
            
            this.removeDisconnectedColumns(columns, origin);
            if (columns.size() < config.minFootprint()) {
                return false;
            } else {
                int fillLevel = this.getFillLevel(columns, origin.getY(), fillDepth);
                
                for (CarveColumn column : columns.values().stream().sorted(CarveColumn::compareTo).toList()) {
                    this.carveColumn(level, columns, column, fillLevel, random, config);
                }
                
                return true;
            }
        }
    }
    
    private boolean wouldExposeSideLiquid(WorldGenLevel region, BlockPos pos) {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (!region.getFluidState(pos.relative(dir)).isEmpty()) {
                return true;
            }
        }
        
        return false;
    }
    
    private Optional<Surface> findCarvableSurface(WorldGenLevel region, BlockPos centerPos, int searchRange, CrevasseConfiguration config) {
        BlockPos pos = centerPos.above(searchRange / 2);
        boolean isBlockAboveEmpty = region.getBlockState(pos.above()).isAir();
        int minY = Math.max(region.getMinBuildHeight(), pos.getY() - searchRange);
        BlockPos.MutableBlockPos blockPos = pos.mutable();
        
        while (blockPos.getY() >= minY) {
            BlockState block = region.getBlockState(blockPos);
            if (config.carvableBlocks().test(region, blockPos) && isBlockAboveEmpty) {
                return Optional.of(new CrevasseFeature.Surface(blockPos.immutable(), block));
            }
            
            isBlockAboveEmpty = block.isAir();
            blockPos.move(Direction.DOWN);
        }
        
        return Optional.empty();
    }
    
    @Nullable
    private CarveColumn findColumn(Long2ObjectOpenHashMap<CarveColumn> columns, int x, int z) {
        return columns.getOrDefault(ChunkPos.asLong(x, z), null);
    }
    
    private int getFillLevel(Long2ObjectOpenHashMap<CarveColumn> columns, int originSurfaceY, int fillDepth) {
        CarveColumn lowestColumn = columns.values()
            .stream()
            .min(Comparator.comparingInt(a -> a.surfacePos.getY()))
            .get();
        return Math.min(originSurfaceY - fillDepth, lowestColumn.surfacePos.getY() - 1);
    }
    
    private boolean hasSteepNeighbor(Long2ObjectOpenHashMap<CarveColumn> columns, CarveColumn column) {
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = column.surfacePos.relative(facing);
            CarveColumn neighbor = this.findColumn(columns, neighborPos.getX(), neighborPos.getZ());
            if (neighbor != null && Math.abs(column.surfacePos.getY() - neighbor.surfacePos.getY()) > 1) {
                return true;
            }
        }
        
        return false;
    }
    
    private void removeDisconnectedColumns(Long2ObjectOpenHashMap<CarveColumn> columns, BlockPos originSurfacePos) {
        CarveColumn originColumn = this.findColumn(columns, originSurfacePos.getX(), originSurfacePos.getZ());
        if (originColumn != null && !this.hasSteepNeighbor(columns, originColumn)) {
            List<CarveColumn> connectedColumns = new ArrayList<>();
            LongSet processedColumns = new LongOpenHashSet();
            connectedColumns.add(originColumn);
            processedColumns.add(ChunkPos.asLong(originSurfacePos.getX(), originSurfacePos.getZ()));
            
            for (int index = 0; index < connectedColumns.size(); index++) {
                CarveColumn column = connectedColumns.get(index);
                
                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    BlockPos neighborPos = column.surfacePos().relative(facing);
                    CarveColumn neighbor = this.findColumn(columns, neighborPos.getX(), neighborPos.getZ());
                    if (neighbor != null) {
                        long neighborIndex = ChunkPos.asLong(neighborPos.getX(), neighborPos.getZ());
                        boolean wasProcessed = processedColumns.contains(neighborIndex);
                        if (!wasProcessed && !this.hasSteepNeighbor(columns, neighbor)) {
                            connectedColumns.add(neighbor);
                            processedColumns.add(neighborIndex);
                        }
                    }
                }
            }
            
            columns.clear();
            
            for (CarveColumn column : connectedColumns) {
                columns.put(ChunkPos.asLong(column.surfacePos.getX(), column.surfacePos.getZ()), column);
            }
        } else {
            columns.clear();
        }
    }
    
    private void appendBranchColumns(Long2ObjectOpenHashMap<CarveColumn> columns, WorldGenLevel region, BlockPos originSurfacePos, int branchLength, int carveDepth, float yaw, RandomSource random, CrevasseConfiguration config) {
        double cursorX = originSurfacePos.getX();
        double cursorZ = originSurfacePos.getZ();
        int previousSurfaceY = originSurfacePos.getY();
        float yawAcceleration = 0.0F;
        
        for (int step = 0; step < branchLength; step++) {
            cursorX += Math.cos(yaw);
            cursorZ += Math.sin(yaw);
            float turnDirection = (float) random.nextGaussian();
            float turnMagnitude = random.nextFloat();
            float var35 = yawAcceleration * 0.5F;
            yawAcceleration = var35 + turnDirection * turnMagnitude * config.twistiness();
            yaw += yawAcceleration * config.turnFactor();
            int blockX = Mth.floor(cursorX);
            int blockZ = Mth.floor(cursorZ);
            Optional<Surface> surface = this.findCarvableSurface(region, new BlockPos(blockX, previousSurfaceY, blockZ), config.cursorSurfaceSearchRange(), config);
            if (surface.isPresent()) {
                float progress = (float) step / branchLength;
                float taper = 1.0F - progress * config.tapering();
                float radius = Math.max(config.maxWidth() * 0.5F * taper, 0.5F);
                float radiusSquared = Mth.square(radius);
                int maxDepth = Math.max((int) (carveDepth * taper), 1);
                int expandedRadius = Mth.ceil(radius);
                
                for (int x = blockX - expandedRadius; x <= blockX + expandedRadius; x++) {
                    for (int z = blockZ - expandedRadius; z <= blockZ + expandedRadius; z++) {
                        int dx = x - blockX;
                        int dz = z - blockZ;
                        if (Mth.lengthSquared((float) dx, (float) dz) <= radiusSquared) {
                            long columnIndex = ChunkPos.asLong(x, z);
                            if (!columns.containsKey(columnIndex)) {
                                Optional<Surface> columnSurface = this.findCarvableSurface(region, new BlockPos(x, surface.get().pos.getY(), z), config.columnSurfaceSearchRange(), config);
                                if (columnSurface.isPresent()) {
                                    int depth = 1;
                                    
                                    for (BlockPos.MutableBlockPos testPos = columnSurface.get().pos.mutable().move(Direction.DOWN);
                                         depth < maxDepth && config.carvableBlocks().test(region, testPos);
                                         depth++
                                    ) {
                                        testPos.move(Direction.DOWN);
                                    }
                                    
                                    columns.put(columnIndex, new CarveColumn(columnSurface.get().pos, columnSurface.get().block, depth - 1));
                                }
                            }
                        }
                    }
                }
                
                previousSurfaceY = surface.get().pos.getY();
            }
        }
    }
    
    private void sealExternalSides(WorldGenLevel region, Long2ObjectOpenHashMap<CarveColumn> columns, CarveColumn column, BlockPos pos, Block placementBlock, CrevasseConfiguration config) {
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            BlockPos neighborPos = pos.relative(facing);
            if (!config.acceptableBarrier().test(region, neighborPos)) {
                CarveColumn neighborColumn = this.findColumn(columns, neighborPos.getX(), neighborPos.getZ());
                if (neighborColumn == null || !neighborColumn.containsY(pos.getY())) {
                    BlockState neighborBlock = region.getBlockState(neighborPos);
                    if (!neighborBlock.is(placementBlock)) {
                        region.setBlock(neighborPos, column.originalSurfaceBlock, 2);
                    }
                }
            }
        }
    }
    
    private void sealFilledBottom(WorldGenLevel region, CarveColumn column, BlockPos pos, Block fillBlock, CrevasseConfiguration config) {
        BlockPos belowPos = pos.below();
        if (!config.acceptableBarrier().test(region, belowPos)) {
            BlockState belowBlock = region.getBlockState(belowPos);
            if (!belowBlock.is(fillBlock)) {
                region.setBlock(belowPos, column.originalSurfaceBlock, 2);
            }
        }
    }
    
    private void carveColumn(WorldGenLevel region, Long2ObjectOpenHashMap<CarveColumn> columns, CarveColumn column, int fillLevel, RandomSource random, CrevasseConfiguration config) {
        BlockState air = Blocks.AIR.defaultBlockState();
        int carveMinY = column.getBottomY();
        boolean carvedAnyBlock = false;
        BlockPos.MutableBlockPos carvePos = column.surfacePos.mutable();
        
        while (carvePos.getY() >= carveMinY) {
            BlockState fillBlock = config.fillBlock().getState(random, carvePos);
            boolean shouldFill = carvePos.getY() <= fillLevel;
            if (!shouldFill && this.wouldExposeSideLiquid(region, carvePos)) {
                break;
            }
            
            Block placementBlock = shouldFill ? fillBlock.getBlock() : air.getBlock();
            this.sealExternalSides(region, columns, column, carvePos, placementBlock, config);
            region.setBlock(carvePos, shouldFill ? fillBlock : air, 2);
            if (shouldFill && carvePos.getY() == carveMinY) {
                this.sealFilledBottom(region, column, carvePos, placementBlock, config);
            }
            
            carvedAnyBlock = true;
            carvePos.move(Direction.DOWN);
        }
        
        if (carvedAnyBlock) {
            BlockState blockState = config.surfaceBlock().getState(random, column.surfacePos);
            region.setBlock(column.surfacePos, blockState, 2);
        }
    }
    
    private record CarveColumn(BlockPos surfacePos, BlockState originalSurfaceBlock, int depth) implements Comparable<CarveColumn> {
        public int getBottomY() {
            return this.surfacePos.getY() - this.depth;
        }
        
        public boolean containsY(int y) {
            return y >= this.getBottomY() && y <= this.surfacePos.getY();
        }
        
        public int compareTo(CarveColumn other) {
            int xDiff = this.surfacePos.getX() - other.surfacePos.getX();
            return xDiff == 0 ? this.surfacePos.getZ() - other.surfacePos.getZ() : xDiff;
        }
    }
    
    private record Surface(BlockPos pos, BlockState block) {}
}