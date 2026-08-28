package com.blackgear.vanillabackport.common.level.items;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entities.decoration.Cushion;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class CushionItem extends Item {
    private final DyeColor color;
    
    public CushionItem(Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
    }
    
    @Override
    public InteractionResult useOn(UseOnContext context) {
        UseOnContext recalculatedContext = recalculateContextForSpecialCollisionShapes(context);
        Direction clickedFace = recalculatedContext.getClickedFace();
        if (clickedFace != Direction.UP) return InteractionResult.FAIL;
        
        Level level = context.getLevel();
        BlockPlaceContext placeContext = new BlockPlaceContext(recalculatedContext);
        BlockPos pos = placeContext.getClickedPos();
        Vec3 entityPos = new Vec3(pos.getX() + 0.5, recalculatedContext.getClickLocation().y, pos.getZ() + 0.5);
        AABB spawnAABB = ModEntityTypes.CUSHION.get().getAABB(entityPos.x, entityPos.y, entityPos.z);
        
        if (!Cushion.canBePlacedAt(level, spawnAABB)) return InteractionResult.FAIL;
        
        ItemStack stack = context.getItemInHand();
        if (level instanceof ServerLevel server) {
            if (!server.getEntitiesOfClass(Cushion.class, spawnAABB).isEmpty()) return InteractionResult.FAIL;
            
            Consumer<Cushion> entityConfig = EntityType.createDefaultStackConfig(server, stack, context.getPlayer());
            Cushion cushion = ModEntityTypes.CUSHION.get().create(server, stack.getTag(), entityConfig, pos, MobSpawnType.SPAWN_EGG, true, true);
            if (cushion == null) return InteractionResult.FAIL;
            
            cushion.moveTo(entityPos.x, entityPos.y, entityPos.z, Direction.fromYRot(placeContext.getRotation()).toYRot(), 0.0F);
            cushion.setColor(this.color);
            server.addFreshEntity(cushion);
            cushion.destroyIfInFire(server);
            level.playSound(null, cushion.getX(), cushion.getY(), cushion.getZ(), ModSoundEvents.CUSHION_PLACE.get(), SoundSource.BLOCKS, 0.75F, 0.8F);
            cushion.gameEvent(GameEvent.ENTITY_PLACE, context.getPlayer());
            if (!placeContext.getPlayer().getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        
        return InteractionResult.SUCCESS;
    }

    private static UseOnContext recalculateContextForSpecialCollisionShapes(final UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return context;
        } else {
            Level level = context.getLevel();
            BlockPos clickedPos = context.getClickedPos();
            BlockState clickedState = level.getBlockState(clickedPos);
            if (!clickedState.is(ModBlockTags.CUSHION_USES_COLLISION_SHAPE)) {
                return context;
            } else {
                Vec3 rayFrom = player.getEyePosition();
                Vec3 ray = context.getClickLocation().subtract(rayFrom);
                Vec3 rayTo = context.getClickLocation().add(ray.normalize().scale(0.001));
                BlockHitResult collisionHitResult = clickedState.getCollisionShape(level, clickedPos).clip(rayFrom, rayTo, clickedPos);
                return collisionHitResult == null ? context : new UseOnContext(player, context.getHand(), collisionHitResult);
            }
        }
    }
}