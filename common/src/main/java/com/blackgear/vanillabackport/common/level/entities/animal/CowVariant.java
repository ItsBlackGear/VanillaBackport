package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;

/**
 * added for retro-compatibility with MC Earth Mobs Addon, highly recommended for the dev to migrate!
 */
@Deprecated(forRemoval = true, since = "1.3")
public class CowVariant extends com.blackgear.vanillabackport.common.level.entity.mob.animal.cow.CowVariant {
    public CowVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) {
        super(modelAndTexture, spawnConditions);
    }
}