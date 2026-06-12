package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.entities.ModDamageTypes;
import com.blackgear.vanillabackport.core.data.tags.ModDamageTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagGenerator extends TagsProvider<DamageType> {
    public DamageTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(DamageTypeTags.BYPASSES_SHIELD)
            .add(ModDamageTypes.SULFUR_CUBE_HOT);
        this.tag(DamageTypeTags.IS_FIRE)
            .add(ModDamageTypes.SULFUR_CUBE_HOT);
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
                DamageTypes.HOT_FLOOR,
                DamageTypes.MOB_ATTACK,
                DamageTypes.MOB_ATTACK_NO_AGGRO,
                DamageTypes.MOB_PROJECTILE,
                DamageTypes.PLAYER_ATTACK,
                DamageTypes.EXPLOSION,
                DamageTypes.PLAYER_EXPLOSION,
                DamageTypes.STALAGMITE,
                DamageTypes.STING,
                ModDamageTypes.SULFUR_CUBE_HOT,
                DamageTypes.SWEET_BERRY_BUSH,
                DamageTypes.THROWN,
                DamageTypes.TRIDENT
            );
        this.tag(ModDamageTypeTags.NO_KNOCKBACK)
            .add(
                DamageTypes.EXPLOSION,
                DamageTypes.PLAYER_EXPLOSION,
                DamageTypes.BAD_RESPAWN_POINT,
                DamageTypes.IN_FIRE,
                DamageTypes.LIGHTNING_BOLT,
                DamageTypes.ON_FIRE,
                DamageTypes.LAVA,
                DamageTypes.HOT_FLOOR,
                ModDamageTypes.SULFUR_CUBE_HOT,
                DamageTypes.IN_WALL,
                DamageTypes.CRAMMING,
                DamageTypes.DROWN,
                DamageTypes.STARVE,
                DamageTypes.CACTUS,
                DamageTypes.FALL,
                DamageTypes.FLY_INTO_WALL,
                DamageTypes.FELL_OUT_OF_WORLD,
                DamageTypes.GENERIC,
                DamageTypes.MAGIC,
                DamageTypes.WITHER,
                DamageTypes.DRAGON_BREATH,
                DamageTypes.DRY_OUT,
                DamageTypes.SWEET_BERRY_BUSH,
                DamageTypes.FREEZE,
                DamageTypes.STALAGMITE,
                DamageTypes.OUTSIDE_BORDER,
                DamageTypes.GENERIC_KILL
            );
    }
}