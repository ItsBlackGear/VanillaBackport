package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.common.level.block_entities.CopperGolemStatueBlockEntity;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem.CopperGolem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WeatheringCopperGolemStatueBlock extends CopperGolemStatueBlock implements WeatheringCopper {
    public WeatheringCopperGolemStatueBlock(WeatherState weatheringState, Properties properties) {
        super(weatheringState, properties);
    }
    
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }
    
    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.onRandomTick(state, level, pos, random);
    }
    
    @Override
    public WeatherState getAge() {
        return this.getWeatheringState();
    }
    
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (level.getBlockEntity(pos) instanceof CopperGolemStatueBlockEntity statue) {
            if (!heldItem.is(ItemTags.AXES)) {
                if (heldItem.is(Items.HONEYCOMB)) {
                    return InteractionResult.PASS;
                }
                
                this.updatePose(level, state, pos, player);
                return InteractionResult.SUCCESS;
            }
            
            if (this.getAge() == WeatherState.UNAFFECTED) {
                CopperGolem golem = statue.removeStatue(state);
                heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                if (golem != null) {
                    level.addFreshEntity(golem);
                    level.removeBlock(pos, false);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        
        return InteractionResult.PASS;
    }
}