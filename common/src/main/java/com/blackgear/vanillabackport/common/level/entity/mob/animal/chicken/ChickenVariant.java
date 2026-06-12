package com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken;

import com.blackgear.vanillabackport.common.api.variants.*;
import com.blackgear.vanillabackport.common.api.variants.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.variants.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.variants.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.variants.spawn.SpawnPrioritySelectors;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public record ChickenVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ChickenVariant> STREAM_CODEC = ByteBufCodecs.registry(ModRegistries.CHICKEN_VARIANT_KEY);
    public static final Codec<ChickenVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(ChickenVariant::modelAndTexture),
        SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(ChickenVariant::spawnConditions)
    ).apply(instance, ChickenVariant::new));

    private ChickenVariant(ModelAndTexture<ModelType> modelAndTexture) {
        this(modelAndTexture, SpawnPrioritySelectors.EMPTY);
    }

    @Override
    public List<Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    public enum ModelType implements StringRepresentable {
        NORMAL("normal"),
        COLD("cold");

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