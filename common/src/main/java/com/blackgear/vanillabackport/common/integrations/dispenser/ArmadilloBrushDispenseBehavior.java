package com.blackgear.vanillabackport.common.integrations.dispenser;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.armadillo.Armadillo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class ArmadilloBrushDispenseBehavior extends OptionalDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        ServerLevel level = source.getLevel();
        BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        List<Armadillo> armadillos = level.getEntitiesOfClass(Armadillo.class, new AABB(pos), EntitySelector.NO_SPECTATORS);
        if (!armadillos.isEmpty()) {
            for (Armadillo armadillo : armadillos) {
                if (armadillo.brushOffScute() && stack.hurt(16, level.random, null)) {
                    stack.setCount(0);
                    return stack;
                }
            }
        }
        
        this.setSuccess(false);
        return stack;
    }
}