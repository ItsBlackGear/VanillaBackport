package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.core.data.tags.ModDamageTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGenerator extends TagsProvider<DamageType> {
    public DamageTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModDamageTypeTags.SULFUR_CUBE_WITH_BLOCK_IMMUNE_TO)
            .add(
                DamageTypes.ARROW,
                DamageTypes.CACTUS,
                DamageTypes.DRY_OUT,
                DamageTypes.FALL,
                DamageTypes.FALLING_ANVIL,
                DamageTypes.FALLING_BLOCK,
                DamageTypes.FALLING_STALACTITE,
                DamageTypes.FREEZE,
                DamageTypes.MOB_ATTACK,
                DamageTypes.MOB_ATTACK_NO_AGGRO,
                DamageTypes.MOB_PROJECTILE,
                DamageTypes.PLAYER_ATTACK,
                DamageTypes.PLAYER_EXPLOSION,
                DamageTypes.STALAGMITE,
                DamageTypes.STING,
                DamageTypes.SWEET_BERRY_BUSH,
                DamageTypes.THROWN,
                DamageTypes.TRIDENT
            );
    }
}