package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;

@Deprecated(forRemoval = true)
public class ChickenVariant extends com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariant {
    public ChickenVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) {
        super(modelAndTexture, spawnConditions);
    }
}