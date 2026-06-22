package com.blackgear.vanillabackport.client.integrations.rendering;

import com.blackgear.vanillabackport.client.level.particles.*;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import static com.blackgear.platform.client.GameRendering.*;

@Environment(EnvType.CLIENT)
public class ParticleRendering {
    public static void factories(ParticleFactoryEvent event) {
        event.register(ModParticles.PALE_OAK_LEAVES, FallingLeavesParticle.PaleOakProvider::new);
        event.register(ModParticles.TRAIL, TrailParticle.Provider::new);

        event.register(ModParticles.FIREFLY, FireflyParticle.Provider::new);
        event.register(ModParticles.TINTED_LEAVES, FallingLeavesParticle.TintedLeavesProvider::new);
        event.register(ModParticles.TINTED_NEEDLES, FallingLeavesParticle.TintedLeavesProvider::new);

        event.register(ModParticles.SULFUR_BUBBLES, SulfurBubbleParticle.Provider::new);
        event.register(ModParticles.NOXIOUS_GAS, NoxiousGasParticle.Provider::new);
        event.register(ModParticles.NOXIOUS_GAS_CLOUD, new NoxiousGasCloudParticle.Provider());
        event.register(ModParticles.SULFUR_CUBE_GOO, SulfurCubeParticle.Provider::new);
        event.register(ModParticles.GEYSER, GeyserEruptionParticle.Provider::new);
        event.register(ModParticles.GEYSER_BASE, GeyserBaseParticle.Provider::new);
        event.register(ModParticles.GEYSER_POOF, GeyserBaseParticle.Provider::new);
        event.register(ModParticles.GEYSER_PLUME, GeyserPlumeParticle.Provider::new);
    }
}