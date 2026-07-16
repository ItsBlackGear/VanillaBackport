package com.blackgear.vanillabackport.core.mixin.common.spear_handler_mob;

import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zombie.class)
public class ZombieMixin extends Monster {
    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }
    
    @ModifyArg(method = "addBehaviourGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/GoalSelector;addGoal(ILnet/minecraft/world/entity/ai/goal/Goal;)V", ordinal = 0))
    private int vb$readdressZombieAttackPriority(int original) {
        return 3;
    }
    
    @ModifyArg(method = "populateDefaultEquipmentSlots", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"), index = 0)
    private int vb$chanceForWeapons(int value) {
        return value == 3 ? 6 : value;
    }
    
    @Inject(method = "populateDefaultEquipmentSlots", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"))
    private void vb$setSpearEquipment(RandomSource random, DifficultyInstance difficulty, CallbackInfo ci, @Local(ordinal = 0) int chance) {
        if (chance == 1) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR.get()));
        }
    }
}