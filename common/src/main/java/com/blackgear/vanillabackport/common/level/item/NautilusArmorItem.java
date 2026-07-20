package com.blackgear.vanillabackport.common.level.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class NautilusArmorItem extends ArmorItem {
	private final ResourceLocation textureLocation;

	public NautilusArmorItem(Holder<ArmorMaterial> material, Properties properties) {
		super(material, Type.BODY, properties);
		this.textureLocation = material.unwrapKey().orElseThrow().location().withPath( s -> "textures/entity/nautilus/armor/" + s + ".png");
	}

	public ResourceLocation getTexture() {
		return this.textureLocation;
	}
}
