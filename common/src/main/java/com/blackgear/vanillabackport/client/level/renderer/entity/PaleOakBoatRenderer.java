package com.blackgear.vanillabackport.client.level.renderer.entity;

import com.blackgear.platform.client.api.model.CustomBoatModel;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;

@Environment(EnvType.CLIENT)
public class PaleOakBoatRenderer extends BoatRenderer implements CustomBoatModel {
    private static final ResourceLocation PALE_OAK_BOAT = ResourceLocation.withDefaultNamespace("textures/entity/boat/pale_oak.png");
    private static final ResourceLocation PALE_OAK_CHEST_BOAT = ResourceLocation.withDefaultNamespace("textures/entity/chest_boat/pale_oak.png");
    private final Pair<ResourceLocation, ListModel<Boat>> boatResource;

    public PaleOakBoatRenderer(EntityRendererProvider.Context context, boolean chestBoat) {
        super(context, chestBoat);
        this.boatResource = Pair.of(
            chestBoat ? PALE_OAK_CHEST_BOAT : PALE_OAK_BOAT,
            chestBoat ? new ChestBoatModel(context.bakeLayer(ModModelLayers.PALE_OAK_CHEST_BOAT)) : new BoatModel(context.bakeLayer(ModModelLayers.PALE_OAK_BOAT))
        );
    }

    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(Boat boat) {
        return this.boatResource;
    }
}