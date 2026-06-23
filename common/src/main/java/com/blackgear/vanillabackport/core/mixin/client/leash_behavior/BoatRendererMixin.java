package com.blackgear.vanillabackport.core.mixin.client.leash_behavior;

import net.minecraft.client.renderer.entity.BoatRenderer;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BoatRenderer.class)
public abstract class BoatRendererMixin extends EntityRendererMixin<Boat> { /* NO-OP */ }