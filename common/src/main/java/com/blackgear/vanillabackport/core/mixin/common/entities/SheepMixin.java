package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.level.entities.animal.SheepColorSpawnRules;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Sheep.class)
public abstract class SheepMixin extends MobMixin {
    protected SheepMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(
        method = "finalizeSpawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/Sheep;setColor(Lnet/minecraft/world/item/DyeColor;)V"
        ),
        index = 0
    )
    private DyeColor vb$updateColors(DyeColor color) {
        return SheepColorSpawnRules.getRandomSheepColor(color, this.level(), this.blockPosition());
    }
}