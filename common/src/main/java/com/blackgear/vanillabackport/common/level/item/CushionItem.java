package com.blackgear.vanillabackport.common.level.item;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entity.decoration.Cushion;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
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
        Direction clickedFace = context.getClickedFace();
        if (clickedFace != Direction.UP) return InteractionResult.FAIL;
        
        Level level = context.getLevel();
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        BlockPos pos = placeContext.getClickedPos();
        Vec3 entityPos = new Vec3(pos.getX() + 0.5, context.getClickLocation().y, pos.getZ() + 0.5);
        AABB spawnAABB = ModEntityTypes.CUSHION.get().getSpawnAABB(entityPos.x, entityPos.y, entityPos.z);
        
        if (!Cushion.wouldSurviveAt(level, spawnAABB)) return InteractionResult.FAIL;
        
        ItemStack stack = context.getItemInHand();
        if (level instanceof ServerLevel server) {
            if (!server.getEntitiesOfClass(Cushion.class, spawnAABB).isEmpty()) return InteractionResult.FAIL;
            
            Consumer<Cushion> entityConfig = EntityType.createDefaultStackConfig(server, stack, context.getPlayer());
            Cushion cushion = ModEntityTypes.CUSHION.get().create(server, entityConfig, pos, MobSpawnType.SPAWN_EGG, true, true);
            if (cushion == null) return InteractionResult.FAIL;
            
            cushion.moveTo(entityPos, Direction.fromYRot(placeContext.getRotation()).toYRot(), 0.0F);
            cushion.setColor(this.color);
            server.addFreshEntity(cushion);
            level.playSound(null, cushion.getX(), cushion.getY(), cushion.getZ(), ModSoundEvents.CUSHION_PLACE.get(), SoundSource.BLOCKS, 0.75F, 0.8F);
            cushion.gameEvent(GameEvent.ENTITY_PLACE);
            stack.consume(1, placeContext.getPlayer());
        }
        
        return InteractionResult.SUCCESS;
    }
}