package com.blackgear.vanillabackport.core.mixin.common.jukebox_playlist;

import com.blackgear.vanillabackport.common.api.modules.jukebox_playlist.JukeboxPlaylist;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JukeboxBlock.class)
public abstract class JukeboxBlockMixin extends BaseEntityBlock {
    @Shadow @Final public static BooleanProperty HAS_RECORD;
    
    protected JukeboxBlockMixin(Properties properties) {
        super(properties);
    }
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$setupPlaylistMode(Properties properties, CallbackInfo ci) {
        this.registerDefaultState(this.defaultBlockState().setValue(JukeboxPlaylist.PLAYLIST_MODE, false));
    }
    
    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void vb$addPlaylistMode(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(JukeboxPlaylist.PLAYLIST_MODE);
    }
    
    @Override @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null
            ? null
            : state.setValue(JukeboxPlaylist.PLAYLIST_MODE, JukeboxPlaylist.isInPlaylistMode(context.getLevel(), context.getClickedPos()));
    }
    
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        BlockState updated = vb$withUpdatedPlaylistMode(state, level, pos);
        if (updated != state) level.setBlock(pos, updated, 2);
    }
    
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return vb$withUpdatedPlaylistMode(super.updateShape(state, direction, neighborState, level, pos, neighborPos), level, pos);
    }
    
    @Unique
    private static BlockState vb$withUpdatedPlaylistMode(BlockState state, LevelReader level, BlockPos pos) {
        boolean should = JukeboxPlaylist.isInPlaylistMode(level, pos);
        return state.getValue(JukeboxPlaylist.PLAYLIST_MODE) == should ? state : state.setValue(JukeboxPlaylist.PLAYLIST_MODE, should);
    }
    
    @Inject(method = "getTicker", at = @At("HEAD"), cancellable = true)
    private <T extends BlockEntity> void vb$playlistTicker(Level level, BlockState state, BlockEntityType<T> type, CallbackInfoReturnable<BlockEntityTicker<T>> cir) {
        if (!state.getValue(HAS_RECORD) && state.getValue(JukeboxPlaylist.PLAYLIST_MODE) && !level.isClientSide) {
            cir.setReturnValue(createTickerHelper(type, BlockEntityType.JUKEBOX, JukeboxPlaylist::tickWithoutRecord));
        }
    }
}