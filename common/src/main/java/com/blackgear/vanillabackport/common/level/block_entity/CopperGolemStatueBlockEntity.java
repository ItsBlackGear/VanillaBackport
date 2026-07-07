package com.blackgear.vanillabackport.common.level.block_entity;

import com.blackgear.vanillabackport.common.level.block.CopperGolemStatueBlock;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.CopperGolem;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component.Serializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CopperGolemStatueBlockEntity extends BlockEntity implements Nameable {
    private static final Component DEFAULT_NAME = Component.translatable("block.minecraft.copper_golem_statue");
    @Nullable private Component name;
    
    public CopperGolemStatueBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.COPPER_GOLEM_STATUE.get(), pos, blockState);
    }
    
    public void createStatue(CopperGolem golem) {
        this.setCustomName(golem.getCustomName());
        super.setChanged();
    }
    
    @Nullable
    public CopperGolem removeStatue(BlockState state) {
        if (this.level != null) {
            CopperGolem golem = ModEntityTypes.COPPER_GOLEM.get().create(this.level);
            if (golem != null) {
                golem.setCustomName(this.getCustomName());
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
    
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("CustomName", Tag.TAG_STRING))
            this.name = Serializer.fromJson(tag.getString("CustomName"));
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.name != null)
            tag.putString("CustomName", Serializer.toJson(this.name));
    }
    
    public void setCustomName(@Nullable Component name) {
        this.name = name;
    }
    
    @Override
    public @Nullable Component getCustomName() {
        return this.name;
    }
    
    @Override
    public Component getDisplayName() {
        return this.getName();
    }
    
    @Override
    public Component getName() {
        return this.name != null ? this.name : DEFAULT_NAME;
    }
    
    public ItemStack getItem(ItemStack stack, CopperGolemStatueBlock.Pose pose) {
        CompoundTag tag = stack.getOrCreateTag();
        CompoundTag state = tag.getCompound("BlockStateTag");
        state.putString(CopperGolemStatueBlock.POSE.getName(), pose.getSerializedName());
        tag.put("BlockStateTag", state);
        return stack;
    }
}
