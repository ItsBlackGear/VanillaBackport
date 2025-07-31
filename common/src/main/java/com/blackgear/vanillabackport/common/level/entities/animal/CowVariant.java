package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.common.api.variant.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public record CowVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<CowVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(CowVariant::modelAndTexture),
        SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(CowVariant::spawnConditions)
    ).apply(instance, CowVariant::new));

    private CowVariant(ModelAndTexture<ModelType> modelAndTexture) {
        this(modelAndTexture, SpawnPrioritySelectors.EMPTY);
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