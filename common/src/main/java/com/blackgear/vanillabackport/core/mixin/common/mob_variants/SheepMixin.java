package com.blackgear.vanillabackport.core.mixin.common.mob_variants;

import com.blackgear.vanillabackport.common.level.entity.mob.animal.sheep.SheepColorSpawnRules;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Sheep.class)
public abstract class SheepMixin extends Animal {
    protected SheepMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(
        method = "finalizeSpawn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/Sheep;getRandomSheepColor(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/item/DyeColor;"
        )
    )
    private DyeColor vb$updateColors(RandomSource random) {
        DyeColor originalColor = Sheep.getRandomSheepColor(random);
        return SheepColorSpawnRules.getRandomSheepColor(originalColor, this.level(), this.blockPosition(), random);
    }
}