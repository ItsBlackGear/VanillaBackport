package com.blackgear.vanillabackport.common.level.worldgen.tree.trunk;

import com.blackgear.vanillabackport.common.registries.worldgen.ModTrunkPlacers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PoplarTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<PoplarTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> trunkPlacerParts(instance).and(instance.group(
        IntProvider.codec(0, 8).fieldOf("trunk_height_above_branches").forGetter(placer -> placer.trunkHeightAboveBranches),
        IntProvider.codec(1, 4).fieldOf("branch_amount").forGetter(placer -> placer.branchAmount)
    )).apply(instance, PoplarTrunkPlacer::new));
    private final IntProvider trunkHeightAboveBranches;
    private final IntProvider branchAmount;
    
    public PoplarTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, IntProvider trunkHeightAboveBranches, IntProvider branchAmount) {
        super(baseHeight, heightRandA, heightRandB);
        this.trunkHeightAboveBranches = trunkHeightAboveBranches;
        this.branchAmount = branchAmount;
    }
    
    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacers.POPLAR_TRUNK_PLACER.get();
    }
    
    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(
        LevelSimulatedReader level,
        BiConsumer<BlockPos, BlockState> blockSetter,
        RandomSource random,
        int freeTreeHeight,
        BlockPos pos,
        TreeConfiguration config
    ) {
        int trunkHeightUpToFoliageBranches = freeTreeHeight - this.trunkHeightAboveBranches.sample(random);
        
        for (int y = 0; y < freeTreeHeight; y++) {
            this.placeLog(level, blockSetter, random, pos.above(y), config);
            List<Direction> directions = getShuffledBranchDirections(random);
            if (trunkHeightUpToFoliageBranches - 1 == y) {
                int branches = this.branchAmount.sample(random);
                
                for (int x = 0; x < branches; x++) {
                    Direction branchDirection = directions.get(x);
                    this.placeLog(level, blockSetter, random, pos.above(y).relative(branchDirection, 1), config, getSidewaysStateModifier(branchDirection));
                }
            }
        }

        return List.of(new FoliagePlacer.FoliageAttachment(pos.above(trunkHeightUpToFoliageBranches), 0, false));
    }
    
    private static Function<BlockState, BlockState> getSidewaysStateModifier(Direction branchDirection) {
        return state -> state.trySetValue(RotatedPillarBlock.AXIS, branchDirection.getAxis());
    }
    
    private static List<Direction> getShuffledBranchDirections(RandomSource random) {
        return Direction.allShuffled(random).stream()
            .filter(direction -> !direction.getAxis().isVertical())
            .collect(Collectors.toList());
    }
}