package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.block_entities.ShelfBlockEntity;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockStateProperties;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.util.BlockShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

public class ShelfBlock extends BaseEntityBlock implements SelectableSlotContainer, SideChainPartBlock, SimpleWaterloggedBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<SideChainPart> SIDE_CHAIN_PART = ModBlockStateProperties.SIDE_CHAIN_PART;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final Map<Direction, VoxelShape> SHAPES = BlockShaper.rotateHorizontal(Shapes.or(Block.box(0.0, 12.0, 11.0, 16.0, 16.0, 13.0), Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0), Block.box(0.0, 0.0, 11.0, 16.0, 4.0, 13.0)));

    public ShelfBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.stateDefinition
                .any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(SIDE_CHAIN_PART, SideChainPart.UNCONNECTED)
                .setValue(WATERLOGGED, false)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }
    
    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return type == PathComputationType.WATER && state.getFluidState().is(FluidTags.WATER);
    }

    @Override @Nullable
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ShelfBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, SIDE_CHAIN_PART, WATERLOGGED);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof Container container) {
                Containers.dropContents(level, pos, container);
                level.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
            
            super.onRemove(state, level, pos, newState, movedByPiston);
            level.updateNeighbourForOutputSignal(pos, state.getBlock());
            this.updateNeighborsAfterPoweringDown(level, pos, state);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide()) {
            boolean signal = level.hasNeighborSignal(pos);
            if (state.getValue(POWERED) != signal) {
                BlockState newState = state.setValue(POWERED, signal);
                if (!signal) {
                    newState = newState.setValue(SIDE_CHAIN_PART, SideChainPart.UNCONNECTED);
                }

                level.setBlock(pos, newState, 3);
                this.playSound(level, pos, signal ? ModSoundEvents.SHELF_ACTIVATE.get() : ModSoundEvents.SHELF_DEACTIVATE.get());
                level.gameEvent(signal ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos, GameEvent.Context.of(newState));
            }
        }
    }

    @Override @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
            .setValue(FACING, context.getHorizontalDirection().getOpposite())
            .setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()))
            .setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public int getRows() {
        return 1;
    }

    @Override
    public int getColumns() {
        return 3;
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.getBlockEntity(pos) instanceof ShelfBlockEntity shelf && !hand.equals(InteractionHand.OFF_HAND)) {
            OptionalInt hitSlot = this.getHitSlot(hit, state.getValue(FACING));
            if (hitSlot.isEmpty()) {
                return InteractionResult.SUCCESS;
            } else {
                Inventory inventory = player.getInventory();
                if (level.isClientSide()) {
                    return inventory.getSelected().isEmpty() ? InteractionResult.PASS : InteractionResult.SUCCESS;
                } else if (!state.getValue(POWERED)) {
                    boolean itemRemoved = swapSingleItem(stack, player, shelf, hitSlot.getAsInt(), inventory);
                    if (itemRemoved) {
                        this.playSound(level, pos, stack.isEmpty() ? ModSoundEvents.SHELF_TAKE_ITEM.get() : ModSoundEvents.SHELF_SINGLE_SWAP.get());
                    } else {
                        if (stack.isEmpty()) {
                            return InteractionResult.PASS;
                        }
                        
                        this.playSound(level, pos, ModSoundEvents.SHELF_PLACE_ITEM.get());
                    }
                    
                    return InteractionResult.SUCCESS;
                } else {
                    boolean anySwapped = this.swapHotbar(level, pos, inventory);
                    if (!anySwapped) {
                        return InteractionResult.CONSUME;
                    } else {
                        this.playSound(level, pos, ModSoundEvents.SHELF_MULTI_SWAP.get());
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    private boolean swapSingleItem(ItemStack stack, Player player, ShelfBlockEntity shelf, int hitSlot, Inventory inventory) {
        ItemStack removedItem = shelf.swapItemNoUpdate(hitSlot, stack);
        ItemStack newInventoryItem = player.getAbilities().instabuild && removedItem.isEmpty() ? stack.copy() : removedItem;
        inventory.setItem(inventory.selected, newInventoryItem);
        inventory.setChanged();
        shelf.setChanged(GameEvent.ITEM_INTERACT_FINISH);
        return !removedItem.isEmpty();
    }

    private boolean swapHotbar(Level level, BlockPos pos, Inventory inventory) {
        List<BlockPos> connectedBlocks = this.getAllBlocksConnectedTo(level, pos);
        if (connectedBlocks.isEmpty()) {
            return false;
        } else {
            boolean anySwapped = false;

            for (int shelfPartIndex = 0; shelfPartIndex < connectedBlocks.size(); shelfPartIndex++) {
                ShelfBlockEntity shelfPart = (ShelfBlockEntity) level.getBlockEntity(connectedBlocks.get(shelfPartIndex));
                if (shelfPart != null) {
                    for (int slot = 0; slot < shelfPart.getContainerSize(); slot++) {
                        int inventorySlot = 9 - (connectedBlocks.size() - shelfPartIndex) * shelfPart.getContainerSize() + slot;
                        if (inventorySlot >= 0 && inventorySlot <= inventory.getContainerSize()) {
                            ItemStack placedInventoryItem = inventory.removeItemNoUpdate(inventorySlot);
                            ItemStack removedShelfItem = shelfPart.swapItemNoUpdate(slot, placedInventoryItem);
                            if (!placedInventoryItem.isEmpty() || !removedShelfItem.isEmpty()) {
                                inventory.setItem(inventorySlot, removedShelfItem);
                                anySwapped = true;
                            }
                        }
                    }

                    inventory.setChanged();
                    shelfPart.setChanged(GameEvent.ENTITY_INTERACT);
                }
            }

            return anySwapped;
        }
    }

    @Override
    public SideChainPart getSideChainPart(BlockState state) {
        return state.getValue(SIDE_CHAIN_PART);
    }

    @Override
    public BlockState setSideChainPart(BlockState state, SideChainPart newPart) {
        return state.setValue(SIDE_CHAIN_PART, newPart);
    }

    @Override
    public Direction getFacing(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    public boolean isConnectable(BlockState state) {
        return state.is(ModBlockTags.WOODEN_SHELVES) && state.hasProperty(POWERED) && state.getValue(POWERED);
    }

    @Override
    public int getMaxChainLength() {
        return 3;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (state.getValue(POWERED)) {
            this.updateSelfAndNeighborsOnPoweringUp(level, pos, state, oldState);
        } else {
            this.updateNeighborsAfterPoweringDown(level, pos, state);
        }
    }

    private void playSound(LevelAccessor level, BlockPos pos, SoundEvent sound) {
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return 0;
        } else if (level.getBlockEntity(pos) instanceof ShelfBlockEntity shelf) {
            int item1Bit = shelf.getItem(0).isEmpty() ? 0 : 1;
            int item2Bit = shelf.getItem(1).isEmpty() ? 0 : 1;
            int item3Bit = shelf.getItem(2).isEmpty() ? 0 : 1;
            return item1Bit | item2Bit << 1 | item3Bit << 2;
        } else {
            return 0;
        }
    }
}