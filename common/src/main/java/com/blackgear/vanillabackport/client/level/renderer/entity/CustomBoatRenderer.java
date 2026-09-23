package com.blackgear.vanillabackport.client.level.renderer.entity;

import com.blackgear.platform.client.api.model.CustomBoatModel;
import com.blackgear.vanillabackport.common.level.entities.boat.BoatRegistry;
import com.blackgear.vanillabackport.common.level.entities.boat.BoatRegistry.BoatType;
import com.blackgear.vanillabackport.common.level.entities.boat.CustomBoatBehavior;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class CustomBoatRenderer extends BoatRenderer implements CustomBoatModel {
    private final Map<BoatType, Pair<ResourceLocation, ListModel<Boat>>> boatResource;

    public CustomBoatRenderer(EntityRendererProvider.Context context, boolean chestBoat) {
        super(context, chestBoat);
        this.boatResource = BoatRegistry.createModels(context, chestBoat);
    }

    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        return boat instanceof CustomBoatBehavior custom ? this.boatResource.get(custom.getBoatType()) : this.boatResource.get(BoatRegistry.getType(BoatRegistry.DEFAULT_TYPE));
    }
}