package com.blackgear.vanillabackport.client.level.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;

@Environment(EnvType.CLIENT)
public record AdultAndBabyModelPair<T extends EntityModel<?>>(T adultModel, T babyModel) {
    public T getModel(boolean isBaby) {
        return isBaby ? this.babyModel : this.adultModel;
    }
}