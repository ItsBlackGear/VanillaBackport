package com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.ModelAndTexture;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnPrioritySelectors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;

import java.util.List;

public class ChickenVariant implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<ChickenVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ModelAndTexture.codec(ModelType.CODEC, ModelType.NORMAL).forGetter(ChickenVariant::modelAndTexture),
        SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(ChickenVariant::spawnConditions)
    ).apply(instance, ChickenVariant::new));
    private final ModelAndTexture<ModelType> modelAndTexture;
    private final SpawnPrioritySelectors spawnConditions;
    
    public ChickenVariant(ModelAndTexture<ModelType> modelAndTexture, SpawnPrioritySelectors spawnConditions) {
        this.modelAndTexture = modelAndTexture;
        this.spawnConditions = spawnConditions;
    }
    
    private ChickenVariant(ModelAndTexture<ModelType> modelAndTexture) {
        this(modelAndTexture, SpawnPrioritySelectors.EMPTY);
    }
    
    @Override
    public List<Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }
    
    public ModelAndTexture<ModelType> modelAndTexture() {
        return this.modelAndTexture;
    }
    
    public SpawnPrioritySelectors spawnConditions() {
        return this.spawnConditions;
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