package com.blackgear.vanillabackport.common.level.block;

import com.blackgear.vanillabackport.common.level.block_entity.CopperChestBlockEntity;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.google.common.base.Suppliers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class CopperChestBlock extends ChestBlock {
    private static final Supplier<Map<Block, Supplier<Block>>> COPPER_TO_COPPER_CHEST_MAPPING = Suppliers.memoize(() -> Map.of(
        Blocks.COPPER_BLOCK, ModBlocks.COPPER_CHEST,
        Blocks.EXPOSED_COPPER, ModBlocks.EXPOSED_COPPER_CHEST,
        Blocks.WEATHERED_COPPER, ModBlocks.WEATHERED_COPPER_CHEST,
        Blocks.OXIDIZED_COPPER, ModBlocks.OXIDIZED_COPPER_CHEST,
        Blocks.WAXED_COPPER_BLOCK, ModBlocks.COPPER_CHEST,
        Blocks.WAXED_EXPOSED_COPPER, ModBlocks.EXPOSED_COPPER_CHEST,
        Blocks.WAXED_WEATHERED_COPPER, ModBlocks.WEATHERED_COPPER_CHEST,
        Blocks.WAXED_OXIDIZED_COPPER, ModBlocks.OXIDIZED_COPPER_CHEST
    ));
    private final WeatherState weatherState;
    private final SoundEvent openSound;
    private final SoundEvent closeSound;
    
    public CopperChestBlock(WeatherState weatherState, SoundEvent openSound, SoundEvent closeSound, Properties properties) {
        super(properties, () -> ModBlockEntities.COPPER_CHEST.get());
        this.weatherState = weatherState;
        this.openSound = openSound;
        this.closeSound = closeSound;
    }
    
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CopperChestBlockEntity(pos, state);
    }
    
    public boolean chestCanConnectTo(BlockState blockState) {
        return blockState.is(ModBlockTags.COPPER_CHESTS) && blockState.hasProperty(ChestBlock.TYPE);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return getLeastOxidizedChestOfConnectedBlocks(state, context.getLevel(), context.getClickedPos());
    }
    
    private static BlockState getLeastOxidizedChestOfConnectedBlocks(BlockState state, Level level, BlockPos pos) {
        BlockState connectedState = level.getBlockState(pos.relative(getConnectedDirection(state)));
        if (state.getValue(ChestBlock.TYPE) != ChestType.SINGLE
            && state.getBlock() instanceof CopperChestBlock copperChest
            && connectedState.getBlock() instanceof CopperChestBlock connectedCopperChest) {
            BlockState updatedBlockState = state;
            BlockState connectedPredicatedBlockState = connectedState;
            if (copperChest.isWaxed() != connectedCopperChest.isWaxed()) {
                updatedBlockState = unwaxBlock(copperChest, state).orElse(state);
                connectedPredicatedBlockState = unwaxBlock(connectedCopperChest, connectedState).orElse(connectedState);
            }
            
            Block leastOxidizedBlock = copperChest.weatherState.ordinal() <= connectedCopperChest.weatherState.ordinal()
                ? updatedBlockState.getBlock()
                : connectedPredicatedBlockState.getBlock();
            return leastOxidizedBlock.withPropertiesOf(updatedBlockState);
        } else {
            return state;
        }
    }
    
    @Override
    public BlockState updateShape(
        BlockState state,
        Direction direction,
        BlockState neighborState,
        LevelAccessor level,
        BlockPos pos,
        BlockPos neighborPos
    ) {
        BlockState blockState = super.updateShape(state, direction, neighborState, level, pos, neighborPos);
        
        if (this.chestCanConnectTo(neighborState) && direction.getAxis().isHorizontal()) {
            ChestType neighbourType = neighborState.getValue(TYPE);
            if (state.getValue(TYPE) == ChestType.SINGLE
                && neighbourType != ChestType.SINGLE
                && state.getValue(FACING) == neighborState.getValue(FACING)
                && getConnectedDirection(neighborState) == direction.getOpposite()) {
                blockState = state.setValue(TYPE, neighbourType.getOpposite());
            }
        }
        
        if (this.chestCanConnectTo(neighborState)) {
            ChestType chestType = state.getValue(ChestBlock.TYPE);
            if (!chestType.equals(ChestType.SINGLE) && getConnectedDirection(state) == direction) {
                return neighborState.getBlock().withPropertiesOf(state);
            }
        }
        
        return blockState;
    }
    
    private static Optional<BlockState> unwaxBlock(CopperChestBlock copperChest, BlockState state) {
        return !copperChest.isWaxed()
            ? Optional.of(state)
            : Optional.ofNullable(HoneycombItem.WAX_OFF_BY_BLOCK.get().get(state.getBlock())).map(block -> block.withPropertiesOf(state));
    }
    
    public WeatherState getState() {
        return this.weatherState;
    }
    
    public static BlockState getFromCopperBlock(Block copperBlock, Direction facing, Level level, BlockPos pos) {
        CopperChestBlock block = (CopperChestBlock) COPPER_TO_COPPER_CHEST_MAPPING.get().getOrDefault(copperBlock, ModBlocks.COPPER_CHEST).get();
        ChestType chestType = block.getChestType(level, pos, facing);
        BlockState state = block.defaultBlockState().setValue(FACING, facing).setValue(TYPE, chestType);
        return getLeastOxidizedChestOfConnectedBlocks(state, level, pos);
    }
    
    public boolean isWaxed() {
        return true;
    }
    
    private ChestType getChestType(Level level, BlockPos pos, Direction facing) {
        if (facing == this.candidatePartnerFacing(level, pos, facing.getClockWise())) {
            return ChestType.LEFT;
        } else {
            return facing == this.candidatePartnerFacing(level, pos, facing.getCounterClockWise())
                ? ChestType.RIGHT
                : ChestType.SINGLE;
        }
    }
    
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!oldState.is(newState.getBlock()) && oldState.hasBlockEntity() && !newState.is(ModBlockTags.COPPER_CHESTS)) {
            if (level.getBlockEntity(pos) instanceof Container container) {
                Containers.dropContents(level, pos, container);
                level.updateNeighbourForOutputSignal(pos, oldState.getBlock());
            }
            
            level.removeBlockEntity(pos);
        }
    }
    
    private Direction candidatePartnerFacing(Level level, BlockPos pos, Direction direction) {
        BlockState state = level.getBlockState(pos.relative(direction));
        return this.chestCanConnectTo(state) && state.getValue(TYPE) == ChestType.SINGLE ? state.getValue(FACING) : null;
    }
    
    public SoundEvent getOpenChestSound() {
        return this.openSound;
    }
    
    public SoundEvent getCloseChestSound() {
        return this.closeSound;
    }
}