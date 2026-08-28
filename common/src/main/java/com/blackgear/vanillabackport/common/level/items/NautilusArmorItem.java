package com.blackgear.vanillabackport.common.level.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class NautilusArmorItem extends Item {
	private final int protection;
	private final String texture;

	public NautilusArmorItem(int protection, String identifier, Item.Properties properties) {
		super(properties);
		this.protection = protection;
		this.texture = "textures/entity/nautilus/armor/" + identifier + ".png";
	}

	public ResourceLocation getTexture() {
		return new ResourceLocation(this.texture);
	}

	public int getProtection() {
		return this.protection;
	}
}
