package com.blackgear.vanillabackport.common.level.dispenser;

import com.blackgear.vanillabackport.common.level.boat.PaleOakBoat;
import com.blackgear.vanillabackport.common.level.boat.PaleOakChestBoat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;

public class PaleOakBoatDispenseBehavior extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior;
    private final boolean isChestBoat;

    public PaleOakBoatDispenseBehavior() {
        this(false);
    }

    public PaleOakBoatDispenseBehavior(boolean isChestBoat) {
        this.defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
        this.isChestBoat = isChestBoat;
    }

    public ItemStack execute(BlockSource source, ItemStack stack) {
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        ServerLevel level = source.level();
        Vec3 center = source.center();
        double width = 0.5625 + (double) EntityType.BOAT.getWidth() / 2.0;
        double x = center.x() + (double) direction.getStepX() * width;
        double y = center.y() + (double) ((float) direction.getStepY() * 1.125F);
        double z = center.z() + (double) direction.getStepZ() * width;
        BlockPos blockpos = source.pos().relative(direction);
        double offset = 0.0;
        if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
            offset = 1.0;
        } else {
            if (!level.getBlockState(blockpos).isAir() || !level.getFluidState(blockpos.below()).is(FluidTags.WATER)) {
                return this.defaultDispenseItemBehavior.dispense(source, stack);
            }
        }

        Boat boat = this.isChestBoat ? new PaleOakChestBoat(level, x, y, z) : new PaleOakBoat(level, x, y, z);
        boat.setYRot(direction.toYRot());
        boat.setPos(x, y + offset, z);
        level.addFreshEntity(boat);
        stack.shrink(1);
        return stack;
    }

    protected void playSound(BlockSource source) {
        source.level().levelEvent(1000, source.pos(), 0);
    }
}