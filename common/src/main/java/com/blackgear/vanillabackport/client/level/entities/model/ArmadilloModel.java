package com.blackgear.vanillabackport.client.level.entities.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;

/**
 * added for retro-compatibility with Tiny takeover backport, highly recommended for the dev to migrate!
 */
@Environment(EnvType.CLIENT) @Deprecated(forRemoval = true)
public class ArmadilloModel extends com.blackgear.vanillabackport.client.level.model.entity.ArmadilloModel {
	public ArmadilloModel(ModelPart root) {
		super(root);
	}
}
