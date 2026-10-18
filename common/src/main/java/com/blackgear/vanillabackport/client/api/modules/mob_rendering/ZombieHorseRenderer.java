package com.blackgear.vanillabackport.client.api.modules.mob_rendering;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ZombieHorseRenderer extends SpecialMobRenderer<AbstractHorse, HorseModel<AbstractHorse>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(VanillaBackport.MOD_ID, "textures/entity/horse/horse_zombie.png");
    
    public ZombieHorseRenderer(EntityRendererProvider.Context context) { /* NO-OP */ }
    
    @Override
    public Optional<ResourceLocation> getTexture(AbstractHorse entity) {
        return entity.getType() == EntityType.ZOMBIE_HORSE ? Optional.of(TEXTURE) : Optional.empty();
    }
    
    @Override
    public Optional<HorseModel<AbstractHorse>> getModel(AbstractHorse entity) {
        return Optional.empty();
    }
}