package com.blackgear.vanillabackport.core.mixin.common.copper_golem;

import com.blackgear.vanillabackport.common.level.block.CopperChestBlock;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.CopperGolem;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin {
    @Shadow @Final private static Predicate<BlockState> PUMPKINS_PREDICATE;

    @Shadow private static void spawnGolemInWorld(Level level, BlockPattern.BlockPatternMatch patternMatch, Entity golem, BlockPos pos) {}

    @Unique @Nullable private BlockPattern copperGolemBase;
    @Unique @Nullable private BlockPattern copperGolemFull;

    @Inject(method = "canSpawnGolem", at = @At("HEAD"), cancellable = true)
    private void onCanSpawnGolem(LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!VanillaBackport.COMMON_CONFIG.hasCopperGolems.get()) return;
        
        if (this.getOrCreateCopperGolemBase().find(level, pos) != null) cir.setReturnValue(true);
    }

    @Inject(method = "trySpawnGolem", at = @At("HEAD"))
    private void onTrySpawnGolem(Level level, BlockPos pos, CallbackInfo ci) {
        if (!VanillaBackport.COMMON_CONFIG.hasCopperGolems.get()) return;
        
        BlockPattern.BlockPatternMatch copperGolemMatch = this.getOrCreateCopperGolemFull().find(level, pos);
        if (copperGolemMatch != null) {
            CopperGolem golem = ModEntityTypes.COPPER_GOLEM.get().create(level);
            if (golem != null) {
                spawnGolemInWorld(level, copperGolemMatch, golem, copperGolemMatch.getBlock(0, 0, 0).getPos());
                this.replaceCopperBlockWithChest(level, copperGolemMatch);
                golem.spawn(this.getWeatherStateFromPattern(copperGolemMatch));
            }
        }
    }

    @Unique
    private WeatheringCopper.WeatherState getWeatherStateFromPattern(BlockPattern.BlockPatternMatch match) {
        BlockState state = match.getBlock(0, 1, 0).getState();
        return state.getBlock() instanceof WeatheringCopper copper
            ? copper.getAge()
            : Optional.ofNullable(HoneycombItem.WAX_OFF_BY_BLOCK.get().get(state.getBlock()))
            .filter(weathering -> weathering instanceof WeatheringCopper)
            .map(weathering -> (WeatheringCopper) weathering)
            .orElse((WeatheringCopper) Blocks.COPPER_BLOCK)
            .getAge();
    }

    @Unique
    private BlockPattern getOrCreateCopperGolemBase() {
        if (this.copperGolemBase == null) {
            this.copperGolemBase = BlockPatternBuilder.start().aisle(" ", "#").where('#', BlockInWorld.hasState(block -> block.is(ModBlockTags.COPPER))).build();
        }

        return this.copperGolemBase;
    }

    @Unique
    private BlockPattern getOrCreateCopperGolemFull() {
        if (this.copperGolemFull == null) {
            this.copperGolemFull = BlockPatternBuilder.start()
                .aisle("^", "#")
                .where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE))
                .where('#', BlockInWorld.hasState(block -> block.is(ModBlockTags.COPPER)))
                .build();
        }

        return this.copperGolemFull;
    }

    @Unique
    private void replaceCopperBlockWithChest(Level level, BlockPattern.BlockPatternMatch match) {
        BlockInWorld copperBlock = match.getBlock(0, 1, 0);
        BlockInWorld pumpkinBlock = match.getBlock(0, 0, 0);
        Direction facing = pumpkinBlock.getState().getValue(CarvedPumpkinBlock.FACING);
        BlockState state = CopperChestBlock.getFromCopperBlock(copperBlock.getState().getBlock(), facing, level, copperBlock.getPos());
        level.setBlock(copperBlock.getPos(), state, 2);
    }
}