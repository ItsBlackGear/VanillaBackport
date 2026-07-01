package com.blackgear.vanillabackport.core.mixin.common.weathering_lightning_rods;

import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin extends Entity {
    @Shadow protected abstract BlockPos getStrikePosition();
    
    public LightningBoltMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "powerLightningRod", at = @At("HEAD"))
    private void shouldPowerLightningRod(CallbackInfo ci) {
        BlockPos strikePos = this.getStrikePosition();
        BlockState struckState = this.level().getBlockState(strikePos);
        if (struckState.is(ModBlockTags.LIGHTNING_RODS) && struckState.getBlock() instanceof LightningRodBlock rod) {
            rod.onLightningStrike(struckState, this.level(), strikePos);
        }
    }
    
    @WrapOperation(
        method = "clearCopperOnLightningStrike",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"
        )
    )
    private static Block preventCleaningOnStrike(BlockState instance, Operation<Block> original) {
        Block block = original.call(instance);
        if (block instanceof WeatheringCopper && HoneycombItem.WAX_OFF_BY_BLOCK.get().containsKey(block)) {
            return null;
        }
        
        return block;
    }
    
    @WrapOperation(
        method = "randomStepCleaningCopper",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"
        )
    )
    private static Block preventCleaningOnRandomStep(BlockState instance, Operation<Block> original) {
        Block block = original.call(instance);
        if (block instanceof WeatheringCopper && HoneycombItem.WAX_OFF_BY_BLOCK.get().containsKey(block)) {
            return null;
        }
        
        return block;
    }
}