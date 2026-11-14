package com.blackgear.vanillabackport.common.level.block_entities;

import com.blackgear.vanillabackport.common.level.blocks.CrafterBlock;
import com.blackgear.vanillabackport.common.level.inventory.CrafterMenu;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CrafterBlockEntity extends RandomizableContainerBlockEntity implements CraftingContainer {
    private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);
    private int craftingTicksRemaining = 0;
    protected final ContainerData containerData = new ContainerData() {
        private final int[] slotStates = new int[9];
        private int triggered = 0;
        
        @Override
        public int get(int index) {
            return index == 9 ? this.triggered : this.slotStates[index];
        }
        
        @Override
        public void set(int index, int value) {
            if (index == 9) {
                this.triggered = value;
            } else {
                this.slotStates[index] = value;
            }
        }
        
        @Override
        public int getCount() {
            return 10;
        }
    };
    
    public CrafterBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CRAFTER.get(), pos, blockState);
    }
    
    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.crafter");
    }
    
    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new CrafterMenu(containerId, inventory, this, this.containerData);
    }
    
    public void setSlotState(int slot, boolean state) {
        if (this.slotCanBeDisabled(slot)) {
            this.containerData.set(slot, state ? 0 : 1);
            this.setChanged();
        }
    }
    
    public boolean isSlotDisabled(int slot) {
        return slot >= 0 && slot < 9 && this.containerData.get(slot) == 1;
    }
    
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (this.containerData.get(slot) == 1) {
            return false;
        } else {
            ItemStack slotStack = this.items.get(slot);
            int currentStackSize = slotStack.getCount();
            if (currentStackSize >= slotStack.getMaxStackSize()) {
                return false;
            } else {
                return slotStack.isEmpty() || !this.smallerStackExists(currentStackSize, slotStack, slot);
            }
        }
    }
    
    private boolean smallerStackExists(int currentSize, ItemStack stack, int slot) {
        for (int i = slot + 1; i < 9; i++) {
            if (!this.isSlotDisabled(i)) {
                ItemStack slotStack = this.getItem(i);
                if (slotStack.isEmpty() || slotStack.getCount() < currentSize && ItemStack.isSameItemSameTags(slotStack, stack)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.craftingTicksRemaining = tag.getInt("crafting_ticks_remaining");
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, this.items);
        }
        
        int[] disabledSlots = tag.getIntArray("disabled_slots");
        
        for (int i = 0; i < 9; i++) {
            this.containerData.set(i, 0);
        }
        
        for (int i : disabledSlots) {
            if (this.slotCanBeDisabled(i)) {
                this.containerData.set(i, 1);
            }
        }
        
        this.containerData.set(9, tag.getInt("triggered"));
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("crafting_ticks_remaining", this.craftingTicksRemaining);
        if (!this.trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, this.items);
        }
        
        this.addDisabledSlots(tag);
        this.addTriggered(tag);
    }
    
    @Override
    public int getContainerSize() {
        return 9;
    }
    
    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }
    
    @Override
    public void setItem(int slot, ItemStack stack) {
        if (this.isSlotDisabled(slot)) {
            this.setSlotState(slot, true);
        }
        
        super.setItem(slot, stack);
    }
    
    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }
    
    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }
    
    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }
    
    @Override
    public int getWidth() {
        return 3;
    }
    
    @Override
    public int getHeight() {
        return 3;
    }
    
    @Override
    public void fillStackedContents(StackedContents contents) {
        for (ItemStack stack : this.items) {
            contents.accountSimpleStack(stack);
        }
    }
    
    private void addDisabledSlots(CompoundTag tag) {
        IntList disabledSlots = new IntArrayList();
        
        for (int i = 0; i < 9; i++) {
            if (this.isSlotDisabled(i)) {
                disabledSlots.add(i);
            }
        }
        
        tag.putIntArray("disabled_slots", disabledSlots);
    }
    
    private void addTriggered(CompoundTag tag) {
        tag.putInt("triggered", this.containerData.get(9));
    }
    
    public void setTriggered(boolean triggered) {
        this.containerData.set(9, triggered ? 1 : 0);
    }
    
    public boolean isTriggered() {
        return this.containerData.get(9) == 1;
    }
    
    public static void serverTick(Level level, BlockPos pos, BlockState state, CrafterBlockEntity crafter) {
        int craftingTicksRemaining = crafter.craftingTicksRemaining - 1;
        if (craftingTicksRemaining >= 0) {
            crafter.craftingTicksRemaining = craftingTicksRemaining;
            if (craftingTicksRemaining == 0) {
                level.setBlockAndUpdate(pos, state.setValue(CrafterBlock.CRAFTING, false));
            }
        }
    }
    
    public void setCraftingTicksRemaining(int craftingTicksRemaining) {
        this.craftingTicksRemaining = craftingTicksRemaining;
    }
    
    public int getRedstoneSignal() {
        int count = 0;
        
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack stack = this.getItem(i);
            if (!stack.isEmpty() || this.isSlotDisabled(i)) {
                count++;
            }
        }
        
        return count;
    }
    
    private boolean slotCanBeDisabled(int slot) {
        return slot > -1 && slot < 9 && this.items.get(slot).isEmpty();
    }
}