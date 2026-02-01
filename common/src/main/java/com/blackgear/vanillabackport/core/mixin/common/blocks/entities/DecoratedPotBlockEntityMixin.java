package com.blackgear.vanillabackport.core.mixin.common.blocks.entities;

import com.blackgear.vanillabackport.common.api.block.RandomizableContainer;
import com.blackgear.vanillabackport.common.api.block.entity.DecoratedPot;
import com.blackgear.vanillabackport.common.level.blockentities.decoratedpot.WobbleStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Special thanks to Echo2craft
@Mixin(DecoratedPotBlockEntity.class)
public abstract class DecoratedPotBlockEntityMixin extends BlockEntity implements RandomizableContainer, DecoratedPot {
    @Unique private static final String TAG_ITEM = "item";
    @Unique private static final int EVENT_POT_WOBBLES = 1;
    @Unique public long vb$wobbleStartedAtTick;
    @Unique @Nullable public WobbleStyle vb$lastWobbleStyle;
    @Unique private ItemStack vb$item = ItemStack.EMPTY;
    @Unique @Nullable protected ResourceLocation vb$lootTable;
    @Unique protected long vb$lootTableSeed;

    public DecoratedPotBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    public void vb$saveAdditional(CompoundTag tag, CallbackInfo ci) {
        if (!this.trySaveLootTable(tag) && !this.vb$item.isEmpty()) {
            tag.put(TAG_ITEM, this.vb$item.save(new CompoundTag()));
        }
    }

    @Inject(method = "load", at = @At("TAIL"))
    public void vb$load(CompoundTag tag, CallbackInfo ci) {
        if (!this.tryLoadLootTable(tag)) {
            if (tag.contains(TAG_ITEM, 10)) {
                this.vb$item = ItemStack.of(tag.getCompound(TAG_ITEM));
            } else {
                this.vb$item = ItemStack.EMPTY;
            }
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (this.level != null && id == EVENT_POT_WOBBLES && type >= 0 && type < WobbleStyle.values().length) {
            this.vb$wobbleStartedAtTick = this.level.getGameTime();
            this.vb$lastWobbleStyle = WobbleStyle.values()[type];
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    public WobbleStyle getLastWobbleStyle() {
        return this.vb$lastWobbleStyle;
    }

    @Override
    public long getWobbleStartedAtTick() {
        return this.vb$wobbleStartedAtTick;
    }

    @Override
    public void wobble(WobbleStyle style) {
        if (this.level != null && !this.level.isClientSide()) {
            this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), EVENT_POT_WOBBLES, style.ordinal());
        }
    }

    @Override
    public @Nullable ResourceLocation getLootTable() {
        return this.vb$lootTable;
    }

    @Override
    public long getLootTableSeed() {
        return this.vb$lootTableSeed;
    }

    @Override
    public void setLootTableSeed(long seed) {
        this.vb$lootTableSeed = seed;
    }

    @Override
    public void setLootTable(@Nullable ResourceLocation lootTable) {
        this.vb$lootTable = lootTable;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return slot == 0 ? this.getFirstItem() : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack getFirstItem() {
        this.unpackLootTable(null);
        return this.vb$item;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        return slot != 0 ? ItemStack.EMPTY : this.splitFirstItem(amount);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        if (slot == 0) this.setFirstItem(stack);
    }

    @Override
    public void setFirstItem(@NotNull ItemStack stack) {
        this.unpackLootTable(null);
        this.vb$item = stack;
    }

    @Unique
    public ItemStack splitFirstItem(int stack) {
        this.unpackLootTable(null);
        ItemStack itemstack = this.vb$item.split(stack);
        if (this.vb$item.isEmpty()) {
            this.vb$item = ItemStack.EMPTY;
        }

        return itemstack;
    }

    @Override @Nullable
    public Level getLevel() {
        return this.level;
    }

    @Override
    public BlockPos getBlockPos() {
        return this.worldPosition;
    }

    @Override
    public BlockEntity getContainerBlockEntity() {
        return this;
    }

    @Override
    public void setChanged() {
        if (this.level != null) {
            setChanged(this.level, this.worldPosition, this.getBlockState());
        }
    }
}
