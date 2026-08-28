package com.blackgear.vanillabackport.common.level.entities.mob.animal.cow;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public class CowVariant implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<CowVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(CowVariant::modelAndTexture),
        SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(CowVariant::spawnConditions)
    ).apply(instance, CowVariant::new));
    private final ModelAndTexture<ModelType> modelAndTexture;
    private final SpawnPrioritySelectors spawnConditions;
    
    public CowVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) {
        this.modelAndTexture = modelAndTexture;
        this.spawnConditions = spawnConditions;
    }

    private CowVariant(ModelAndTexture<ModelType> modelAndTexture) {
        this(modelAndTexture, SpawnPrioritySelectors.EMPTY);
    }
    
    public ModelAndTexture<ModelType> modelAndTexture() {
        return this.modelAndTexture;
    }
    
    public SpawnPrioritySelectors spawnConditions() {
        return this.spawnConditions;
    }
    
    @Override
    public List<Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    public enum ModelType implements StringRepresentable {
        NORMAL("normal"),
        COLD("cold"),
        WARM("warm");

        public static final Codec<ModelType> CODEC = StringRepresentable.fromEnum(ModelType::values);
        private final String name;

        ModelType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}