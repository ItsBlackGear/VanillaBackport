package com.blackgear.vanillabackport.core.mixin.server;

import com.blackgear.vanillabackport.common.level.effect.IRaidOmenHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements IRaidOmenHelper {
    @Unique
    private static final String RAID_OMEN_POSITION_TAG_NAME = "raid_omen_position";
    @Unique
    @Nullable
    private BlockPos vb$raidOmenPosition;

    @Inject(
            method = "readAdditionalSaveData",
            at = @At("TAIL")
    )
    public void vb$onReadAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains(RAID_OMEN_POSITION_TAG_NAME)) {
            vb$setRaidOmenPosition(NbtUtils.readBlockPos(compound.getCompound(RAID_OMEN_POSITION_TAG_NAME)));
        }
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("TAIL")
    )
    public void vb$onAddAdditionalSaveData(CompoundTag compound, CallbackInfo ci){
        if(this.vb$raidOmenPosition != null){
            compound.put(RAID_OMEN_POSITION_TAG_NAME, NbtUtils.writeBlockPos(this.vb$raidOmenPosition));
        }
    }

    @Unique
    public @Nullable BlockPos vb$getRaidOmenPosition() {
        return this.vb$raidOmenPosition;
    }
    @Unique
    public void vb$setRaidOmenPosition(BlockPos pos) {
        this.vb$raidOmenPosition = pos;
    }

    @Unique
    public void vb$clearRaidOmenPosition() {
        this.vb$raidOmenPosition = null;
    }
}
