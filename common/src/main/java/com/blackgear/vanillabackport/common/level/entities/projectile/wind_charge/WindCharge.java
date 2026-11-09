package com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge;

import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;

public class WindCharge extends AbstractWindCharge {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(true, false, Optional.of(1.22F), BuiltInRegistries.BLOCK.getTag(ModBlockTags.BLOCKS_WIND_CHARGE_EXPLOSIONS).map(Function.identity()));
    private int noDeflectTicks = 5;
    
    public WindCharge(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }
    
    public WindCharge(Player player, Level level, double x, double y, double z) {
        super(ModEntityTypes.WIND_CHARGE.get(), level, player, x, y, z);
    }
    
    public WindCharge(Level level, double x, double y, double z, Vec3 movement) {
        super(ModEntityTypes.WIND_CHARGE.get(), x, y, z, movement, level);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.noDeflectTicks > 0) {
            this.noDeflectTicks--;
        }
    }
    
    @Override
    protected void explode(Vec3 pos) {
        WindBurst.explode(
            this.level(),
            this,
            null,
            EXPLOSION_DAMAGE_CALCULATOR,
            pos.x(),
            pos.y(),
            pos.z(),
            1.2F,
            false,
            Level.ExplosionInteraction.NONE,
            ModParticles.GUST_EMITTER_SMALL.get(),
            ModParticles.GUST_EMITTER_LARGE.get(),
            ModSoundEvents.WIND_CHARGE_BURST
        );
    }
}
