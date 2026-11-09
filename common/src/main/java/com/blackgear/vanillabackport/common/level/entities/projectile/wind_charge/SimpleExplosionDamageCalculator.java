package com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SimpleExplosionDamageCalculator extends ExplosionDamageCalculator {
   private final boolean explodesBlocks;
   private final boolean damagesEntities;
   private final Optional<Float> knockbackMultiplier;
   private final Optional<HolderSet<Block>> immuneBlocks;

   public SimpleExplosionDamageCalculator(boolean bl, boolean bl2, Optional<Float> optional, Optional<HolderSet<Block>> optional2) {
      this.explodesBlocks = bl;
      this.damagesEntities = bl2;
      this.knockbackMultiplier = optional;
      this.immuneBlocks = optional2;
   }

   @Override
   public Optional<Float> getBlockExplosionResistance(
       Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, FluidState fluidState
   ) {
       return this.immuneBlocks.<Optional<Float>>map(holders ->
            blockState.is(holders) ? Optional.of(3600000.0F) : Optional.empty())
        .orElseGet(() ->
            super.getBlockExplosionResistance(explosion, blockGetter, blockPos, blockState, fluidState));
   }

   @Override
   public boolean shouldBlockExplode(Explosion explosion, BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, float f) {
      return this.explodesBlocks;
   }

   public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
      return this.damagesEntities;
   }

   public float getKnockbackMultiplier(Entity entity) {
      boolean bl = entity instanceof Player player && player.getAbilities().flying;
      return bl ? 0.0F : this.knockbackMultiplier.orElse(1.0F);
   }
}
