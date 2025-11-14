package com.blackgear.vanillabackport.core.mixin.common.blocks;

import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.block.entity.IDecoratedPotBlockEntityHelper;
import com.blackgear.vanillabackport.common.level.blockentities.decoratedpot.WobbleStyle;
import com.blackgear.vanillabackport.core.data.tags.ModEnchantmentTags;
import com.blackgear.vanillabackport.core.util.EnchantmentUtils;
import com.blackgear.vanillabackport.core.util.ItemStackUtils;
import com.blackgear.vanillabackport.core.util.LevelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DecoratedPotBlock.class)
public abstract class DecoratedPotBlockMixin extends BaseEntityBlock {
    protected DecoratedPotBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack vStack = player.getItemInHand(hand);
        if (level.getBlockEntity(pos) instanceof DecoratedPotBlockEntity pDecoPotEntity) {
            if(pDecoPotEntity instanceof IDecoratedPotBlockEntityHelper advanceBlockEntity){
                if (!level.isClientSide()) {
                    ItemStack itemstack1 = advanceBlockEntity.getFirstItem();
                    if (!vStack.isEmpty()
                            && (itemstack1.isEmpty() || ItemStack.isSameItemSameTags(itemstack1, vStack) && itemstack1.getCount() < itemstack1.getMaxStackSize())) {
                        advanceBlockEntity.wobble(WobbleStyle.POSITIVE);
                        player.awardStat(Stats.ITEM_USED.get(vStack.getItem()));
                        ItemStack itemstack = ItemStackUtils.consumeAndReturn(vStack, 1, player);
                        float f;
                        if (advanceBlockEntity.isEmpty()) {
                            advanceBlockEntity.setFirstItem(itemstack);
                            f = (float)itemstack.getCount() / itemstack.getMaxStackSize();
                        } else {
                            itemstack1.grow(1);
                            f = (float)itemstack1.getCount() / itemstack1.getMaxStackSize();
                        }

                        level.playSound(null, pos, ModSoundEvents.DECORATED_POT_INSERT.get(), SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * f);
                        if (level instanceof ServerLevel serverlevel) {
                            serverlevel.sendParticles(
                                    ModParticles.DUST_PLUME.get(),
                                    pos.getX() + 0.5,
                                    pos.getY() + 1.2,
                                    pos.getZ() + 0.5,
                                    7,
                                    0.0,
                                    0.0,
                                    0.0,
                                    0.0
                            );
                        }
                        pDecoPotEntity.setChanged();
                    } else {
                        level.playSound(null, pos, ModSoundEvents.DECORATED_POT_INSERT_FAIL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                        advanceBlockEntity.wobble(WobbleStyle.NEGATIVE);
                    }
                    level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Inject(
            method = "getDrops",
            at = @At(
                    value = "RETURN"
            ),
            cancellable = true
    )
    public void vb$getDrops(BlockState state, LootParams.Builder params, CallbackInfoReturnable<List<ItemStack>> cir){
        BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof DecoratedPotBlockEntity decoratedPotBlockEntity) {
            cir.setReturnValue(super.getDrops(decoratedPotBlockEntity.getBlockState(), params));
        }
    }

    @Redirect(
            method = "playerWillDestroy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;hasSilkTouch(Lnet/minecraft/world/item/ItemStack;)Z"
            )
    )
    public boolean playerWillDestroyRedirect(ItemStack pStack){
        return EnchantmentUtils.isItemHasEnchantmentOfTag(pStack, ModEnchantmentTags.PREVENTS_DECORATED_POT_SHATTERING);
    }

    @Override
    public void onProjectileHit(@NotNull Level pLevel, @NotNull BlockState pState, BlockHitResult pHit, @NotNull Projectile pProjectile) {
        BlockPos blockpos = pHit.getBlockPos();
        if (pLevel instanceof ServerLevel serverlevel && pProjectile.mayInteract(serverlevel, blockpos) && LevelUtils.mayBreak(pProjectile, serverlevel)) {
            pLevel.setBlock(blockpos, pState.setValue(BlockStateProperties.CRACKED, Boolean.TRUE), 4);
            pLevel.destroyBlock(blockpos, true, pProjectile);
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(!pState.is(pNewState.getBlock())){
            if(pLevel.getBlockEntity(pPos) instanceof Container container){
                Containers.dropContents(pLevel, pPos, container);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(pLevel.getBlockEntity(pPos));
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(BlockStateProperties.HORIZONTAL_FACING, pRotation.rotate(pState.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }
}
