package com.blackgear.vanillabackport.core.mixin.common.spear_handler_mob;

import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Drowned.class)
public class DrownedMixin extends Zombie {
    public DrownedMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        return !stack.is(ModItemTags.SPEARS) && super.wantsToPickUp(stack);
    }
}