package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.common.level.block_entities.CrafterBlockEntity;
import com.blackgear.vanillabackport.common.level.recipes.RecipeCache;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockEntities;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlockStateProperties;
import com.blackgear.vanillabackport.core.network.handlers.LevelEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CrafterBlock extends BaseEntityBlock {
    public static final BooleanProperty CRAFTING = ModBlockStateProperties.CRAFTING;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
    private static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
    private static final RecipeCache RECIPE_CACHE = new RecipeCache(10);
    
    public CrafterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(ORIENTATION, FrontAndTop.NORTH_UP).setValue(TRIGGERED, false).setValue(CRAFTING, false));
    }
    
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof CrafterBlockEntity crafter ? crafter.getRedstoneSignal() : 0;
    }
    
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        boolean shouldTrigger = level.hasNeighborSignal(pos);
        boolean isTriggered = state.getValue(TRIGGERED);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (shouldTrigger && !isTriggered) {
            level.scheduleTick(pos, this, 4);
            level.setBlock(pos, state.setValue(TRIGGERED, true), 2);
            this.setBlockEntityTriggered(blockEntity, true);
        } else if (!shouldTrigger && isTriggered) {
            level.setBlock(pos, state.setValue(TRIGGERED, false).setValue(CRAFTING, false), 2);
            this.setBlockEntityTriggered(blockEntity, false);
        }
    }
    
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.dispenseFrom(state, level, pos);
    }
    
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.CRAFTER.get(), CrafterBlockEntity::serverTick);
    }
    
    private void setBlockEntityTriggered(@Nullable BlockEntity blockEntity, boolean triggered) {
        if (blockEntity instanceof CrafterBlockEntity crafterBlockEntity) {
            crafterBlockEntity.setTriggered(triggered);
        }
    }
    
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        CrafterBlockEntity crafter = new CrafterBlockEntity(pos, state);
        crafter.setTriggered(state.hasProperty(TRIGGERED) && state.getValue(TRIGGERED));
        return crafter;
    }
    
    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction nearestLookingDirection = context.getNearestLookingDirection().getOpposite();
        
        Direction verticalDirection = switch (nearestLookingDirection) {
            case DOWN -> context.getHorizontalDirection().getOpposite();
            case UP -> context.getHorizontalDirection();
            case NORTH, SOUTH, WEST, EAST -> Direction.UP;
        };
        return this.defaultBlockState()
            .setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(nearestLookingDirection, verticalDirection))
            .setValue(TRIGGERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
    }
    
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (state.getValue(TRIGGERED)) {
            level.scheduleTick(pos, this, 4);
        }
    }
    
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof Container container) {
                Containers.dropContents(level, pos, container);
                level.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
        }
        
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof CrafterBlockEntity crafter) {
                player.openMenu(crafter);
        }
        
        return InteractionResult.SUCCESS;
    }
    
    protected void dispenseFrom(BlockState state, ServerLevel level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CrafterBlockEntity crafter) {
            Optional<CraftingRecipe> recipe = getPotentialResults(level, crafter);
            if (recipe.isEmpty()) {
                LevelEvents.CRAFTER_FAIL.trigger(level, pos, 0);
            } else {
                CraftingRecipe pickedRecipe = recipe.get();
                ItemStack results = pickedRecipe.assemble(crafter, level.registryAccess());
                if (results.isEmpty()) {
                    LevelEvents.CRAFTER_FAIL.trigger(level, pos, 0);
                } else {
                    crafter.setCraftingTicksRemaining(6);
                    level.setBlock(pos, state.setValue(CRAFTING, true), 2);
//                    results.onCraftedBySystem(level); NOTE: this is only used for map post-processing
                    this.dispenseItem(level, pos, crafter, results, state, pickedRecipe);
                    
                    for (ItemStack remainingItem : pickedRecipe.getRemainingItems(crafter)) {
                        if (!remainingItem.isEmpty()) {
                            this.dispenseItem(level, pos, crafter, remainingItem, state, pickedRecipe);
                        }
                    }
                    
                    crafter.getItems().forEach(stack -> {
                        if (!stack.isEmpty()) stack.shrink(1);
                    });
                    crafter.setChanged();
                }
            }
        }
    }
    
    public static Optional<CraftingRecipe> getPotentialResults(Level level, CraftingContainer input) {
        return RECIPE_CACHE.get(level, input);
    }
    
    private void dispenseItem(ServerLevel level, BlockPos pos, CrafterBlockEntity crafter, ItemStack stack, BlockState state, CraftingRecipe recipe) {
        Direction direction = state.getValue(ORIENTATION).front();
        Container into = HopperBlockEntity.getContainerAt(level, pos.relative(direction));
        ItemStack remaining = stack.copy();
        if (into != null && (into instanceof CrafterBlockEntity || stack.getCount() > into.getMaxStackSize())) {
            while (!remaining.isEmpty()) {
                ItemStack copy = remaining.copyWithCount(1);
                ItemStack itemStack = HopperBlockEntity.addItem(crafter, into, copy, direction.getOpposite());
                if (!itemStack.isEmpty()) {
                    break;
                }
                
                remaining.shrink(1);
            }
        } else if (into != null) {
            while (!remaining.isEmpty()) {
                int oldSize = remaining.getCount();
                remaining = HopperBlockEntity.addItem(crafter, into, remaining, direction.getOpposite());
                if (oldSize == remaining.getCount()) {
                    break;
                }
            }
        }
        
        if (!remaining.isEmpty()) {
            Vec3 centerPos = Vec3.atCenterOf(pos);
            Vec3 itemSpawnOffset = centerPos.relative(direction, 0.7);
            DefaultDispenseItemBehavior.spawnItem(level, remaining, 6, direction, itemSpawnOffset);
            
            for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, AABB.ofSize(centerPos, 17.0, 17.0, 17.0))) {
//                CriteriaTriggers.CRAFTER_RECIPE_CRAFTED.trigger(player, recipe, crafter.getItems());
            }
            
            LevelEvents.CRAFTER_CRAFT.trigger(level, pos, 0);
            LevelEvents.PARTICLES_SHOOT_WHITE_SMOKE.trigger(level, pos, direction.get3DDataValue());
        }
    }
    
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(ORIENTATION, rotation.rotation().rotate(state.getValue(ORIENTATION)));
    }
    
    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ORIENTATION, mirror.rotation().rotate(state.getValue(ORIENTATION)));
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ORIENTATION, TRIGGERED, CRAFTING);
    }
}