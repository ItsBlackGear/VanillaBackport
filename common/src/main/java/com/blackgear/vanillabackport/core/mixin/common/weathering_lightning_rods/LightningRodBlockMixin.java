package com.blackgear.vanillabackport.core.mixin.common.weathering_lightning_rods;

import com.blackgear.vanillabackport.common.api.extensions.access.block.BlockExtension;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightningRodBlock.class)
public class LightningRodBlockMixin extends Block implements WeatheringCopper, BlockExtension {
    public LightningRodBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void vb$randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.is(Blocks.LIGHTNING_ROD)) this.changeOverTime(state, level, pos, random);
    }
    
    @Override
    public boolean vb$isRandomlyTicking(BlockState state) {
        return state.is(Blocks.LIGHTNING_ROD);
    }
    
    @Override
    public WeatherState getAge() {
        return WeatherState.UNAFFECTED;
    }
    
    @Override
    public void vb$onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!state.is(Blocks.LIGHTNING_ROD) && state.is(ModBlockTags.LIGHTNING_RODS)) {
            level.registryAccess()
                .lookup(Registries.ENCHANTMENT)
                .flatMap(lookup -> lookup.get(Enchantments.CHANNELING))
                .ifPresent(channeling -> {
                    if (level.isThundering() && projectile instanceof ThrownTrident trident && EnchantmentHelper.getItemEnchantmentLevel(channeling, trident.getWeaponItem()) > 0) {
                        BlockPos pos = hit.getBlockPos();
                        if (level.canSeeSky(pos)) {
                            LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
                            if (lightningBolt != null) {
                                lightningBolt.moveTo(Vec3.atBottomCenterOf(pos.above()));
                                Entity entity = projectile.getOwner();
                                lightningBolt.setCause(entity instanceof ServerPlayer player ? player : null);
                                level.addFreshEntity(lightningBolt);
                            }
                            
                            level.playSound(null, pos, SoundEvents.TRIDENT_THUNDER.value(), SoundSource.WEATHER, 5.0F, 1.0F);
                        }
                    }
                });
        }
    }
}