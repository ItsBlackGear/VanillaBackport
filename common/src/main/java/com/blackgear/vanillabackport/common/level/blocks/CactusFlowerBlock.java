package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.util.BlockShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CactusFlowerBlock extends BushBlock {
    private static final VoxelShape SHAPE = BlockShaper.column(14.0, 0.0, 12.0);

    public CactusFlowerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        BlockState belowPos = level.getBlockState(pos);
        return belowPos.is(ModBlockTags.SUPPORT_OVERRIDE_CACTUS_FLOWER) || belowPos.isFaceSturdy(level, pos, Direction.UP, SupportType.CENTER);
    }
}