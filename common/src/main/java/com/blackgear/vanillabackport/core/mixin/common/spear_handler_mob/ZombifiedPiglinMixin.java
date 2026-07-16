package com.blackgear.vanillabackport.core.mixin.common.spear_handler_mob;

import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombifiedPiglin.class)
public class ZombifiedPiglinMixin extends Zombie {
    public ZombifiedPiglinMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "populateDefaultEquipmentSlots", at = @At("TAIL"))
    private void vb$spawnWithSpear(RandomSource random, DifficultyInstance difficulty, CallbackInfo ci) {
        if (random.nextInt(20) == 0) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.GOLDEN_SPEAR.get()));
        }
    }
}
