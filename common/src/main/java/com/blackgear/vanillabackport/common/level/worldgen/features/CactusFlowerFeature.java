package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CactusFlowerFeature extends Feature<NoneFeatureConfiguration> {
    public CactusFlowerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        
        BlockState flower = ModBlocks.CACTUS_FLOWER.get().defaultBlockState();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        
        double spawnChance = VanillaBackport.COMMON_CONFIG.cactusFlowerSpawnChance.get();
        boolean placedAny = false;
        
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (random.nextFloat() >= spawnChance) continue;
                
                int worldX = origin.getX() + x;
                int worldZ = origin.getZ() + z;
                
                int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, worldX, worldZ) - 1;
                pos.set(worldX, surfaceY, worldZ);
                
                if (!level.getBlockState(pos).is(Blocks.CACTUS)) continue;
                
                pos.move(Direction.UP);
                
                if (level.getBlockState(pos).isAir() && flower.canSurvive(level, pos)) {
                    level.setBlock(pos, flower, Block.UPDATE_CLIENTS);
                    placedAny = true;
                }
            }
        }
        
        return placedAny;
    }
}