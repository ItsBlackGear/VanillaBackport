package com.blackgear.vanillabackport.client.level.particle;

import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.common.level.block_entities.PotentSulfurBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class NoxiousGasCloudParticle extends NoRenderParticle {
    public NoxiousGasCloudParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.lifetime = 20;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age % 2 == 0) {
            BlockPos sourceBlock = BlockPos.containing(this.x, this.y, this.z);
            Vec3 particlePos = pickRandomParticleSpawnPoint(this.level, sourceBlock);
            if (PotentSulfurBlockEntity.canBeReachedByNoxiousGas(this.level, sourceBlock, particlePos)) {
                spawnNoxiousGasParticle(this.level, particlePos);
            }
        }
    }

    private static Vec3 pickRandomParticleSpawnPoint(Level level, BlockPos centerBlock) {
        RandomSource random = level.getRandom();
        Vec3 horizontalDirection = new Vec3(random.nextFloat() - 0.5F, 0.0, random.nextFloat() - 0.5F).normalize();
        float distance = random.nextFloat() * 3.0F;
        return centerBlock.getCenter().add(horizontalDirection.scale(distance)).subtract(0.0, 0.25, 0.0);
    }

    private static void spawnNoxiousGasParticle(Level level, Vec3 pos) {
        level.addAlwaysVisibleParticle(ModParticles.NOXIOUS_GAS.get(), pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
    }

    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        @Override @Nullable
        public Particle createParticle(
            SimpleParticleType type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new NoxiousGasCloudParticle(level, x, y, z);
        }
    }
}