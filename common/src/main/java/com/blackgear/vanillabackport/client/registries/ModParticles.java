package com.blackgear.vanillabackport.client.registries;

import com.blackgear.platform.core.helper.ParticleRegistry;
import com.blackgear.vanillabackport.client.level.particles.particleoptions.ColorParticleOption;
import com.blackgear.vanillabackport.client.level.particles.particleoptions.GeyserBaseParticleOptions;
import com.blackgear.vanillabackport.client.level.particles.particleoptions.GeyserParticleOptions;
import com.blackgear.vanillabackport.client.level.particles.particleoptions.TrailParticleOption;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.BlockPos;
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
    public static final ParticleRegistry REGISTRIES = ParticleRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<SimpleParticleType> PALE_OAK_LEAVES = REGISTRIES.register("pale_oak_leaves", false);
    public static final Supplier<ParticleType<TrailParticleOption>> TRAIL = REGISTRIES.register(
        "trail",
        false,
        TrailParticleOption.DESERIALIZER,
        particle -> TrailParticleOption.CODEC
    );

    public static final Supplier<SimpleParticleType> FIREFLY = REGISTRIES.register("firefly", false);
    public static final Supplier<ParticleType<ColorParticleOption>> TINTED_LEAVES = REGISTRIES.register(
        "tinted_leaves",
        false,
        ColorParticleOption.DESERIALIZER,
        ColorParticleOption::codec
    );
    public static final Supplier<ParticleType<ColorParticleOption>> TINTED_NEEDLES = REGISTRIES.register(
        "tinted_needles",
        false,
        ColorParticleOption.DESERIALIZER,
        ColorParticleOption::codec
    );

    public static final Supplier<SimpleParticleType> DUST_PLUME = REGISTRIES.register("dust_plume",false);
    public static final Supplier<SimpleParticleType> SULFUR_BUBBLES = REGISTRIES.register("sulfur_bubbles", false);
    public static final Supplier<SimpleParticleType> NOXIOUS_GAS = REGISTRIES.register("noxious_gas", false);
    public static final Supplier<SimpleParticleType> NOXIOUS_GAS_CLOUD = REGISTRIES.register("noxious_gas_cloud", false);
    public static final Supplier<SimpleParticleType> SULFUR_CUBE_GOO = REGISTRIES.register("sulfur_cube_goo", false);

    public static final Supplier<ParticleType<GeyserParticleOptions>> GEYSER = REGISTRIES.register(
        "geyser",
        true,
        GeyserParticleOptions.DESERIALIZER,
        GeyserParticleOptions::codec
    );
    public static final Supplier<ParticleType<GeyserBaseParticleOptions>> GEYSER_BASE = REGISTRIES.register(
        "geyser_base",
        true,
        GeyserBaseParticleOptions.DESERIALIZER,
        GeyserBaseParticleOptions::codec
    );
    public static final Supplier<ParticleType<GeyserBaseParticleOptions>> GEYSER_POOF = REGISTRIES.register(
        "geyser_poof",
        true,
        GeyserBaseParticleOptions.DESERIALIZER,
        GeyserBaseParticleOptions::codec
    );
    public static final Supplier<ParticleType<GeyserParticleOptions>> GEYSER_PLUME = REGISTRIES.register(
        "geyser_plume",
        true,
        GeyserParticleOptions.DESERIALIZER,
        GeyserParticleOptions::codec
    );

    public static <T extends ParticleOptions> void sendParticles(ServerLevel level, T particle, double x, double y, double z, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        sendParticles(level, particle, false, false, x, y, z, particleCount, xOffset, yOffset, zOffset, speed);
    }

    public static <T extends ParticleOptions> void sendParticles(ServerLevel level, T particle, boolean longDistance, boolean overrideLimiter, double x, double y, double z, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(particle, overrideLimiter, x, y, z, (float) xOffset, (float) yOffset, (float) zOffset, (float) speed, particleCount);
        for (int i = 0; i < level.players().size(); i++) {
            ServerPlayer player = level.players().get(i);
            sendParticles(level, player, longDistance, x, y, z, packet);
        }
    }

    public static <T extends ParticleOptions> void sendParticles(ServerLevel level, ServerPlayer player, T particle, boolean longDistance, boolean overrideLimiter, double x, double y, double z, int particleCount, double xOffset, double yOffset, double zOffset, double speed) {
        ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(particle, overrideLimiter, x, y, z, (float) xOffset, (float) yOffset, (float) zOffset, (float) speed, particleCount);
        sendParticles(level, player, longDistance, x, y, z, packet);
    }

    private static void sendParticles(ServerLevel level, ServerPlayer player, boolean longDistance, double x, double y, double z, Packet<?> packet) {
        if (player.level() == level) {
            BlockPos pos = player.blockPosition();
            if (pos.closerToCenterThan(new Vec3(x, y, z), longDistance ? 512.0 : 32.0)) {
                player.connection.send(packet);
            }
        }
    }
}