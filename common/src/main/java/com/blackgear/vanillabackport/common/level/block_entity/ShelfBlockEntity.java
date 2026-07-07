package com.blackgear.vanillabackport.common.level.block_entity;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

public class ShelfBlockEntity extends BlockEntity implements ListBackedContainer {
    private static final String ALIGN_ITEMS_TO_BOTTOM_TAG = "align_items_to_bottom";
    private final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private boolean alignItemsToBottom;
    
    public ShelfBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SHELF.get(), pos, blockState);
    }
    
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items.clear();
        ContainerHelper.loadAllItems(tag, this.items);
        this.alignItemsToBottom = tag.getBoolean(ALIGN_ITEMS_TO_BOTTOM_TAG);
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putBoolean(ALIGN_ITEMS_TO_BOTTOM_TAG, this.alignItemsToBottom);
    }
    
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putBoolean(ALIGN_ITEMS_TO_BOTTOM_TAG, this.alignItemsToBottom);
        return tag;
    }
    
    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }
    
    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }
    
    public ItemStack swapItemNoUpdate(int slot, ItemStack stack) {
        ItemStack retrievedItem = this.removeItemNoUpdate(slot);
        this.setItemNoUpdate(slot, stack);
        return retrievedItem;
    }
    
    public void setChanged(@Nullable GameEvent event) {
        super.setChanged();
        if (this.level != null) {
            if (event != null) {
                this.level.gameEvent(event, this.worldPosition, GameEvent.Context.of(this.getBlockState()));
            }
        
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }
    
    @Override
    public void setChanged() {
        this.setChanged(GameEvent.BLOCK_ACTIVATE);
    }
    
    public boolean getAlignItemsToBottom() {
        return this.alignItemsToBottom;
    }
}