package com.blackgear.vanillabackport.core.mixin.common.jukebox_playlist;

import java.util.OptionalInt;

import com.blackgear.vanillabackport.common.api.modules.jukebox_playlist.JukeboxPlaylist;
import com.blackgear.vanillabackport.common.api.modules.jukebox_playlist.PlaylistHolder;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

@Mixin(JukeboxBlockEntity.class)
public abstract class JukeboxBlockEntityMixin extends BlockEntity implements PlaylistHolder {
    @Shadow public abstract ItemStack getTheItem();
    @Shadow public abstract void setTheItem(ItemStack stack);
    @Shadow public abstract JukeboxSongPlayer getSongPlayer();

    @Unique private OptionalInt vb$playlistPosition = OptionalInt.empty();

    protected JukeboxBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Inject(method = "onSongChanged", at = @At("HEAD"))
    private void vb$advanceWhenSongEnds(CallbackInfo ci) {
        if (!this.getSongPlayer().isPlaying() && this.vb$shouldLoop()) {
            this.vb$tryToAdvancePlaylist(true);
        }
    }

    @WrapWithCondition(
        method = "notifyItemChangedInJukebox",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V")
    )
    private boolean vb$skipIfPlaylistMode(Level level, Holder<GameEvent> event, BlockPos pos, GameEvent.Context context) {
        return !JukeboxPlaylist.isInPlaylistMode(level, pos);
    }
    
    @WrapOperation(
        method = "popOutTheItem",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;offsetRandom(Lnet/minecraft/util/RandomSource;F)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 vb$popOutPosition(Vec3 instance, RandomSource random, float factor, Operation<Vec3> original) {
        BlockPos pos = this.getBlockPos();
        if (JukeboxPlaylist.isInPlaylistMode(this.level, pos)) {
            return Vec3.atBottomCenterOf(pos).add(0.0, 2.2F, 0.0);
        }
        
        return original.call(instance, random, factor);
    }
    
    @WrapOperation(
        method = "tick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/JukeboxSongPlayer;tick(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/level/block/state/BlockState;)V")
    )
    private static void vb$tickWithRecord(JukeboxSongPlayer player, LevelAccessor level, BlockState state, Operation<Void> original, @Local(argsOnly = true) JukeboxBlockEntity jukebox) {
        if (player.isPlaying()) {
            original.call(player, level, state);
        } else if (((PlaylistHolder) jukebox).vb$shouldLoop()) {
            ((PlaylistHolder) jukebox).vb$tryToAdvancePlaylist(true);
        }
    }
    
    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void vb$loadAdditionalData(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        this.vb$playlistPosition = tag.contains("playlist_position", Tag.TAG_INT)
            ? OptionalInt.of(tag.getInt("playlist_position"))
            : OptionalInt.empty();
    }
    
    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void vb$saveAdditionalData(CompoundTag tag, HolderLookup.Provider registries, CallbackInfo ci) {
        if (this.vb$playlistPosition.isPresent()) {
            tag.putInt("playlist_position", this.vb$playlistPosition.getAsInt());
        }
    }
    
    @Override
    public boolean vb$shouldLoop() {
        return this.level != null && JukeboxPlaylist.isMusicDisc(this.getTheItem()) && JukeboxPlaylist.isInPlaylistMode(this.level, this.worldPosition);
    }
    
    @Unique
    private void vb$advancePlaylist(Container container, int newPosition) {
        ItemStack recordItem = container.getItem(newPosition);
        container.setItem(newPosition, this.getTheItem());
        this.setTheItem(recordItem);
    }
    
    @Override
    public void vb$tryToAdvancePlaylist(boolean replayCurrentSongOtherwise) {
        if (this.level != null && this.level.getBlockEntity(this.worldPosition.above()) instanceof Container container) {
            this.vb$playlistPosition = JukeboxPlaylist.findNextPlaylistPosition(container, this.vb$playlistPosition.orElse(-1));
            
            if (this.vb$playlistPosition.isPresent()) {
                this.vb$advancePlaylist(container, this.vb$playlistPosition.getAsInt());
            } else if (replayCurrentSongOtherwise) {
                this.setTheItem(this.getTheItem());
            }
        }
    }
}