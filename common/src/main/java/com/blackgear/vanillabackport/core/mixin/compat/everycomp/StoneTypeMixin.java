package com.blackgear.vanillabackport.core.mixin.compat.everycomp;

import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;

// Modern versions shouldn't have this problem, this is kept for legacy users
@Pseudo @Mixin(StoneType.class)
public abstract class StoneTypeMixin extends BlockType {
    protected StoneTypeMixin(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public boolean isVanilla() {
        var id = this.id;
        if (id.getNamespace().equals("minecraft")) {
            return VANILLA_STONE.contains(id.getPath());
        }

        return false;
    }

    @Unique
    private static final Set<String> VANILLA_STONE = Set.of(
        "stone", "andesite", "granite", "diorite", "tuff", "blackstone", "sandstone", "basalt", "deepslate", "prismarine", "nether", "end_stone"
    );
}