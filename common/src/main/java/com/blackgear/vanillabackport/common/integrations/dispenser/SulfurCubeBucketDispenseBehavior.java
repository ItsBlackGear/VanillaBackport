package com.blackgear.vanillabackport.common.integrations.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
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
        BlockPos blockPos = blockSource.pos().relative(blockSource.state().getValue(DispenserBlock.FACING));
        Level level = blockSource.level();
        if (dispensibleContainerItem.emptyContents(null, level, blockPos, null)) {
            dispensibleContainerItem.checkExtraContent(null, level, item, blockPos);
            return this.consumeWithRemainder(blockSource, item, new ItemStack(Items.BUCKET));
        } else {
            return this.defaultDispenseItemBehavior.dispense(blockSource, item);
        }
    }
}