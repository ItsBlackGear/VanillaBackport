package com.blackgear.vanillabackport.client.registries;

import com.blackgear.platform.core.helper.ParticleRegistry;
import com.blackgear.vanillabackport.client.level.particle.particleoptions.GeyserBaseParticleOptions;
import com.blackgear.vanillabackport.client.level.particle.particleoptions.GeyserParticleOptions;
import com.blackgear.vanillabackport.client.level.particle.particleoptions.TrailParticleOption;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class ModParticles {
    public static final ParticleRegistry PARTICLES = ParticleRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<SimpleParticleType> PALE_OAK_LEAVES = PARTICLES.register("pale_oak_leaves", false);
    public static final Supplier<ParticleType<TrailParticleOption>> TRAIL = PARTICLES.register(
        "trail",
        false,
        particle -> TrailParticleOption.CODEC,
        particle -> TrailParticleOption.STREAM_CODEC
    );
    public static final Supplier<SimpleParticleType> FIREFLY = PARTICLES.register("firefly", false);
    public static final Supplier<ParticleType<ColorParticleOption>> TINTED_LEAVES = PARTICLES.register(
        "tinted_leaves",
        false,
        ColorParticleOption::codec,
        ColorParticleOption::streamCodec
    );
    public static final Supplier<ParticleType<ColorParticleOption>> TINTED_NEEDLES = PARTICLES.register(
        "tinted_needles",
        false,
        ColorParticleOption::codec,
        ColorParticleOption::streamCodec
    );
    public static final Supplier<SimpleParticleType> SULFUR_BUBBLES = PARTICLES.register("sulfur_bubbles", false);
    public static final Supplier<SimpleParticleType> NOXIOUS_GAS = PARTICLES.register("noxious_gas", false);
    public static final Supplier<SimpleParticleType> NOXIOUS_GAS_CLOUD = PARTICLES.register("noxious_gas_cloud", false);
    public static final Supplier<SimpleParticleType> SULFUR_CUBE_GOO = PARTICLES.register("sulfur_cube_goo", false);
    public static final Supplier<ParticleType<GeyserParticleOptions>> GEYSER = PARTICLES.register(
        "geyser",
        true,
        GeyserParticleOptions::codec,
        GeyserParticleOptions::streamCodec
    );
    public static final Supplier<ParticleType<GeyserBaseParticleOptions>> GEYSER_BASE = PARTICLES.register(
        "geyser_base",
        true,
        GeyserBaseParticleOptions::codec,
        GeyserBaseParticleOptions::streamCodec
    );
    public static final Supplier<ParticleType<GeyserBaseParticleOptions>> GEYSER_POOF = PARTICLES.register(
        "geyser_poof",
        true,
        GeyserBaseParticleOptions::codec,
        GeyserBaseParticleOptions::streamCodec
    );
    public static final Supplier<ParticleType<GeyserParticleOptions>> GEYSER_PLUME = PARTICLES.register(
        "geyser_plume",
        true,
        GeyserParticleOptions::codec,
        GeyserParticleOptions::streamCodec
    );

    public static <T extends ParticleOptions> int sendParticles(ServerLevel level, T particle, double x, double y, double z, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        return sendParticles(level, particle, false, false, x, y, z, particleCount, xOffset, yOffset, zOffset, speed);
    }

    public static <T extends ParticleOptions> int sendParticles(ServerLevel level, T particle, boolean longDistance, boolean overrideLimiter, double x, double y, double z, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(particle, overrideLimiter, x, y, z, (float) xOffset, (float) yOffset, (float) zOffset, (float) speed, particleCount);
        int sent = 0;

        for (int i = 0; i < level.players().size(); i++) {
            ServerPlayer player = level.players().get(i);
            if (sendParticles(level, player, longDistance, x, y, z, packet)) {
                sent++;
            }
        }

        return sent;
    }

    public static <T extends ParticleOptions> boolean sendParticles(ServerLevel level, ServerPlayer player, T particle, boolean longDistance, boolean overrideLimiter, double x, double y, double z, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(particle, overrideLimiter, x, y, z, (float) xOffset, (float) yOffset, (float) zOffset, (float) speed, particleCount);
        return sendParticles(level, player, longDistance, x, y, z, packet);
    }

    private static boolean sendParticles(ServerLevel level, ServerPlayer player, boolean longDistance, double x, double y, double z, Packet<?> packet) {
        if (player.level() != level) {
            return false;
        } else {
            BlockPos pos = player.blockPosition();
            if (pos.closerToCenterThan(new Vec3(x, y, z), longDistance ? 512.0 : 32.0)) {
                player.connection.send(packet);
                return true;
            } else {
                return false;
            }
        }
    }
}