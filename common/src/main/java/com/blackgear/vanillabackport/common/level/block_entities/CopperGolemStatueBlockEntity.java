package com.blackgear.vanillabackport.common.level.block_entities;

import com.blackgear.vanillabackport.common.level.blocks.CopperGolemStatueBlock;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem.CopperGolem;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CopperGolemStatueBlockEntity extends BlockEntity {
    public CopperGolemStatueBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.COPPER_GOLEM_STATUE.get(), pos, blockState);
    }
    
    public void createStatue(CopperGolem golem) {
        this.setComponents(DataComponentMap.builder().addAll(this.components()).set(DataComponents.CUSTOM_NAME, golem.getCustomName()).build());
        super.setChanged();
    }
    
    @Nullable
    public CopperGolem removeStatue(BlockState state) {
        if (this.level != null) {
            CopperGolem golem = ModEntityTypes.COPPER_GOLEM.get().create(this.level);
            if (golem != null) {
                golem.setCustomName(this.components().get(DataComponents.CUSTOM_NAME));
                return this.initCopperGolem(state, golem);
            }
        }
        
        return null;
    }
    
    private CopperGolem initCopperGolem(BlockState state, CopperGolem golem) {
        BlockPos pos = this.getBlockPos();
        golem.moveTo(pos.getCenter().x, pos.getY(), pos.getCenter().z, state.getValue(CopperGolemStatueBlock.FACING).toYRot(), 0.0F);
        golem.yHeadRot = golem.getYRot();
        golem.yBodyRot = golem.getYRot();
        golem.playSpawnSound();
        return golem;
    }
    
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    public ItemStack getItem(ItemStack stack, CopperGolemStatueBlock.Pose pose) {
        stack.applyComponents(this.collectComponents());
        stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CopperGolemStatueBlock.POSE, pose));
        return stack;
    }
}
