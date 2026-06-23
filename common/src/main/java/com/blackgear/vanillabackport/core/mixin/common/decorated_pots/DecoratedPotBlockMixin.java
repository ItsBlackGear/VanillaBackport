package com.blackgear.vanillabackport.core.mixin.common.decorated_pots;

import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.block.entity.DecoratedPot;
import com.blackgear.vanillabackport.common.level.block_entity.decorated_pot.WobbleStyle;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.DecoratedPotBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

// Special thanks to Echo2craft
@Mixin(DecoratedPotBlock.class)
public abstract class DecoratedPotBlockMixin extends BaseEntityBlock {
    @Shadow @Final private static DirectionProperty HORIZONTAL_FACING;

    protected DecoratedPotBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (level.getBlockEntity(pos) instanceof DecoratedPotBlockEntity blockEntity && blockEntity instanceof DecoratedPot decoratedPot) {
            if (!level.isClientSide()) {
                ItemStack potItem = decoratedPot.getFirstItem();
                if (!heldItem.isEmpty() && (potItem.isEmpty() || ItemStack.isSameItemSameTags(potItem, heldItem) && potItem.getCount() < potItem.getMaxStackSize())) {
                    decoratedPot.wobble(WobbleStyle.POSITIVE);
                    player.awardStat(Stats.ITEM_USED.get(heldItem.getItem()));
                    ItemStack awardedItem = heldItem.copyWithCount(1);
                    if (!player.getAbilities().instabuild) heldItem.shrink(1);
                    
                    float pitchBend = (float) potItem.getCount() / potItem.getMaxStackSize();
                    if (decoratedPot.isEmpty()) {
                        decoratedPot.setFirstItem(awardedItem);
                        pitchBend = (float) awardedItem.getCount() / awardedItem.getMaxStackSize();
                    } else {
                        potItem.grow(1);
                    }
                    
                    level.playSound(null, pos, ModSoundEvents.DECORATED_POT_INSERT.get(), SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * pitchBend);
                    if (level instanceof ServerLevel server) {
                        server.sendParticles(
                            ModParticles.DUST_PLUME.get(),
                            pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5,
                            7,
                            0.0, 0.0, 0.0,
                            0.0
                        );
                    }
                    
                    blockEntity.setChanged();
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                } else {
                    level.playSound(null, pos, ModSoundEvents.DECORATED_POT_INSERT_FAIL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    decoratedPot.wobble(WobbleStyle.NEGATIVE);
                }
            }
            
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Inject(method = "getDrops", at = @At("RETURN"), cancellable = true)
    public void vb$getDrops(BlockState state, LootParams.Builder params, CallbackInfoReturnable<List<ItemStack>> cir) {
        BlockEntity block = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (block instanceof DecoratedPotBlockEntity pot) {
            cir.setReturnValue(super.getDrops(pot.getBlockState(), params));
        }
    }

    @Override
    public void onProjectileHit(@NotNull Level level, @NotNull BlockState state, BlockHitResult hit, @NotNull Projectile projectile) {
        BlockPos pos = hit.getBlockPos();
        if (level instanceof ServerLevel server && projectile.mayInteract(server, pos) && projectile.getType().is(EntityTypeTags.IMPACT_PROJECTILES)) {
            level.setBlock(pos, state.setValue(BlockStateProperties.CRACKED, Boolean.TRUE), 4);
            level.destroyBlock(pos, true, projectile);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof Container container) {
                Containers.dropContents(level, pos, container);
                level.updateNeighbourForOutputSignal(pos, this);
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, Level level, @NotNull BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(HORIZONTAL_FACING, rotation.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(HORIZONTAL_FACING)));
    }
}
