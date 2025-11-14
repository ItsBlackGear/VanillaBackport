package com.blackgear.vanillabackport.core.mixin.common.blocks.entities;

import com.blackgear.vanillabackport.common.api.block.RandomizableContainer;
import com.blackgear.vanillabackport.common.api.block.entity.IDecoratedPotBlockEntityHelper;
import com.blackgear.vanillabackport.common.level.blockentities.decoratedpot.WobbleStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DecoratedPotBlockEntity.class)
public abstract class DecoratedPotBlockEntityMixin extends BlockEntity implements RandomizableContainer, IDecoratedPotBlockEntityHelper {
    @Shadow
    @Final
    public static String TAG_SHERDS = "sherds";
    @Unique
    private static final String TAG_ITEM = "item";
    @Unique
    private static final int EVENT_POT_WOBBLES = 1;
    @Unique
    public long vb$wobbleStartedAtTick;
    @Unique
    @Nullable
    public WobbleStyle vb$lastWobbleStyle;
    @Unique
    private ItemStack vb$item = ItemStack.EMPTY;
    @Unique
    @Nullable
    protected ResourceLocation vb$lootTable;
    @Unique
    protected long vb$lootTableSeed;
    public DecoratedPotBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Inject(
            method = "load",
            at = @At("TAIL")
    )
    public void vb$load(CompoundTag tag, CallbackInfo ci){
        if (!this.tryLoadLootTable(tag) && this.level != null) {
            this.vb$item = ItemStack.of(tag.getCompound(TAG_ITEM));
        } else {
            this.vb$item = ItemStack.EMPTY;
        }
    }

    @Inject(
            method = "saveAdditional",
            at = @At("TAIL")
    )
    public void vb$saveAdditional(CompoundTag tag, CallbackInfo ci){
        if (!this.trySaveLootTable(tag) && !this.vb$item.isEmpty()) {
            CompoundTag itemTag = new CompoundTag();
            this.vb$item.save(itemTag);
            tag.put(TAG_ITEM,itemTag);
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
    public void wobble(WobbleStyle pStyle) {
        if (this.level != null && !this.level.isClientSide()) {
            this.level.blockEvent(this.getBlockPos(), this.getBlockState().getBlock(), EVENT_POT_WOBBLES, pStyle.ordinal());
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
    public void setLootTableSeed(long pSeed) {
        this.vb$lootTableSeed = pSeed;
    }

    @Override
    public void setLootTable(@Nullable ResourceLocation pLootTable) {
        this.vb$lootTable = pLootTable;
    }

    @Override
    public @NotNull ItemStack getItem(int pSlot) {
        return pSlot == 0 ? this.getFirstItem() : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack getFirstItem() {
        this.unpackLootTable(null);
        return this.vb$item;
    }

    @Override
    public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
        return pSlot != 0 ? ItemStack.EMPTY : this.splitFirstItem(pAmount);
    }

    @Override
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        if (pSlot == 0) {
            this.setFirstItem(pStack);
        }
    }

    @Override
    public void setFirstItem(@NotNull ItemStack pItem) {
        this.unpackLootTable(null);
        this.vb$item = pItem;
    }

    // From 1.21+ ContainerSingleItem interface.
    @Unique
    public ItemStack splitFirstItem(int pAmount) {
        this.unpackLootTable(null);
        ItemStack itemstack = this.vb$item.split(pAmount);
        if (this.vb$item.isEmpty()) {
            this.vb$item = ItemStack.EMPTY;
        }

        return itemstack;
    }

    // @ExpectPlatform
    /*public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        this.trickytrialsbackport$decoratedPotHandler = this.trickytrialsbackport$decoratedPotHandler != null ? this.trickytrialsbackport$decoratedPotHandler : LazyOptional.of(this::createUnSidedHandler);
        return cap == ForgeCapabilities.ITEM_HANDLER && !this.remove ? this.trickytrialsbackport$decoratedPotHandler.cast() : super.getCapability(cap, side);
    }*/
}
