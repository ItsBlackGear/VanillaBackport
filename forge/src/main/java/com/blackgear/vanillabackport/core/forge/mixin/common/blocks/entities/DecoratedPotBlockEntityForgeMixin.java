package com.blackgear.vanillabackport.core.forge.mixin.common.blocks.entities;

import com.blackgear.vanillabackport.common.api.block.RandomizableContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

// Create mod compat.
@Mixin(DecoratedPotBlockEntity.class)
public abstract class DecoratedPotBlockEntityForgeMixin extends BlockEntity implements RandomizableContainer {
    @Unique
    private LazyOptional<IItemHandler> vb$decoratedPotHandler;

    @Unique
    protected IItemHandler vb$createUnSidedHandler() {
        return new InvWrapper(this);
    }

    public DecoratedPotBlockEntityForgeMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        this.vb$decoratedPotHandler = this.vb$decoratedPotHandler != null ? this.vb$decoratedPotHandler : LazyOptional.of(this::vb$createUnSidedHandler);
        return cap == ForgeCapabilities.ITEM_HANDLER && !this.remove ? this.vb$decoratedPotHandler.cast() : super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (this.vb$decoratedPotHandler != null) {
            this.vb$decoratedPotHandler.invalidate();
            this.vb$decoratedPotHandler = null;
        }
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.vb$decoratedPotHandler = LazyOptional.of(this::vb$createUnSidedHandler);
    }
}
