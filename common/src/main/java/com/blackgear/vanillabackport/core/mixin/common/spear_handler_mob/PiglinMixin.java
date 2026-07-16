package com.blackgear.vanillabackport.core.mixin.common.spear_handler_mob;

import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Collection;

@Mixin(Piglin.class)
public abstract class PiglinMixin extends AbstractPiglin {
    public PiglinMixin(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }
    
    @ModifyArg(
        method = "brainProvider",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/Brain;provider(Ljava/util/Collection;Ljava/util/Collection;)Lnet/minecraft/world/entity/ai/Brain$Provider;"
        ),
        index = 0
    )
    private Collection<? extends MemoryModuleType<?>> vb$provideSpearMemories(Collection<? extends MemoryModuleType<?>> original) {
        ImmutableList.Builder<MemoryModuleType<?>> builder = ImmutableList.builder();
        builder.addAll(original);
        builder.add(ModMemoryModuleTypes.SPEAR_FLEEING_TIME.get());
        builder.add(ModMemoryModuleTypes.SPEAR_FLEEING_POSITION.get());
        builder.add(ModMemoryModuleTypes.SPEAR_CHARGE_POSITION.get());
        builder.add(ModMemoryModuleTypes.SPEAR_ENGAGE_TIME.get());
        builder.add(ModMemoryModuleTypes.SPEAR_STATUS.get());
        return builder.build();
    }
    
    @ModifyArg(
        method = "createSpawnWeapon",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;<init>(Lnet/minecraft/world/level/ItemLike;)V",
            ordinal = 1
        )
    )
    private ItemLike vb$spawnWithSpear(ItemLike original) {
        return this.random.nextInt(10) == 0 ? ModItems.GOLDEN_SPEAR.get() : original;
    }
}