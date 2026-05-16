package com.blackgear.vanillabackport.common.level.items;

import com.blackgear.vanillabackport.common.registries.ModEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SulfurCubeBucketItem extends BucketItem {
    private final EntityType<?> type;
    private final SoundEvent emptySound;

    public SulfurCubeBucketItem(EntityType<?> type, SoundEvent emptySound, Item.Properties properties) {
        super(Fluids.EMPTY, properties);
        this.type = type;
        this.emptySound = emptySound;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

        if (hit.getType() == HitResult.Type.MISS || hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        BlockPos clickedPos = hit.getBlockPos();
        Direction direction = hit.getDirection();
        BlockPos placePos = clickedPos.relative(direction);

        if (!level.mayInteract(player, clickedPos) || !player.mayUseItemAt(placePos, direction, stack)) {
            return InteractionResultHolder.fail(stack);
        }

        if (this.emptyContents(player, level, placePos, hit)) {
            this.checkExtraContent(player, level, stack, placePos);
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, placePos, stack);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(stack, player), level.isClientSide());
        }

        return InteractionResultHolder.fail(stack);
    }

    @Override
    public boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult result) {
        this.playEmptySound(player, level, pos);
        return true;
    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level level, ItemStack containerStack, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            this.spawn(serverLevel, containerStack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    @Override
    protected void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos) {
        level.playSound(player, pos, this.emptySound, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    private void spawn(ServerLevel level, ItemStack stack, BlockPos pos) {
        Entity entity = this.type.spawn(level, stack, null, pos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            CustomData customData = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
            bucketable.loadFromBucketTag(customData.copyTag());
            bucketable.setFromBucket(true);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (this.type == ModEntityTypes.SULFUR_CUBE) {
            CustomData customData = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
            HolderLookup.Provider registries = context.registries();
            if (customData.isEmpty() || registries == null) return;

            CompoundTag tag = customData.copyTag();
            if (tag.contains("absorbed_block", CompoundTag.TAG_COMPOUND)) {
                ItemStack absorbedBlock = ItemStack.parse(registries, tag.getCompound("absorbed_block")).orElse(ItemStack.EMPTY);
                if (!absorbedBlock.isEmpty()) {
                    tooltipComponents.add(Component.translatable("entity.minecraft.sulfur_cube.content", absorbedBlock.getHoverName()).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
                }
            }
        }
    }
}