package com.blackgear.vanillabackport.common.integrations.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class SulfurCubeBucketDispenseBehavior extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
    
    @Override
    public ItemStack execute(BlockSource blockSource, ItemStack item) {
        DispensibleContainerItem dispensibleContainerItem = (DispensibleContainerItem) item.getItem();
        BlockPos blockPos = blockSource.getPos().relative(blockSource.getBlockState().getValue(DispenserBlock.FACING));
        Level level = blockSource.getLevel();
        if (dispensibleContainerItem.emptyContents(null, level, blockPos, null)) {
            dispensibleContainerItem.checkExtraContent(null, level, item, blockPos);
            
            item.shrink(1);
            if (item.isEmpty()) {
                return new ItemStack(Items.BUCKET);
            } else {
                return this.defaultDispenseItemBehavior.dispense(blockSource, item);
            }
        } else {
            return this.defaultDispenseItemBehavior.dispense(blockSource, item);
        }
    }
}