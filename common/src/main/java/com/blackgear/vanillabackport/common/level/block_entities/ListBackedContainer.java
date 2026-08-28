package com.blackgear.vanillabackport.common.level.block_entities;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public interface ListBackedContainer extends Container {
    NonNullList<ItemStack> getItems();
    
    default int count() {
        return (int) this.getItems().stream().filter(Predicate.not(ItemStack::isEmpty)).count();
    }
    
    @Override
    default int getContainerSize() {
        return this.getItems().size();
    }
    
    @Override
    default void clearContent() {
        this.getItems().clear();
    }
    
    @Override
    default boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }
    
    @Override
    default ItemStack getItem(int slot) {
        return this.getItems().get(slot);
    }
    
    @Override
    default ItemStack removeItem(int slot, int count) {
        ItemStack result = ContainerHelper.removeItem(this.getItems(), slot, count);
        if (!result.isEmpty()) {
            this.setChanged();
        }
        
        return result;
    }
    
    @Override
    default ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.removeItem(this.getItems(), slot, this.getMaxStackSize());
    }
    
    @Override
    default boolean canPlaceItem(int slot, ItemStack stack) {
        return this.acceptsItemType(stack) && (this.getItem(slot).isEmpty() || this.getItem(slot).getCount() < this.getMaxStackSize(stack));
    }
    
    default boolean acceptsItemType(ItemStack stack) {
        return true;
    }
    
    @Override
    default void setItem(int slot, ItemStack stack) {
        this.setItemNoUpdate(slot, stack);
        this.setChanged();
    }
    
    default void setItemNoUpdate(int slot, ItemStack stack) {
        this.getItems().set(slot, stack);
        int maxSize = this.getMaxStackSize(stack);
        if (!stack.isEmpty() && stack.getCount() > maxSize)
            stack.setCount(maxSize);
    }
}