package com.blackgear.vanillabackport.core.mixin.compat.everycomp;

import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;

// Modern versions shouldn't have this problem, this is kept for legacy users
@Pseudo @Mixin(WoodType.class)
public abstract class WoodTypeMixin extends BlockType {
    protected WoodTypeMixin(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    @Override
    public boolean isVanilla() {
        var id = this.id;
        if (id.getNamespace().equals("minecraft")) {
            return VANILLA_WOODS.contains(id.getPath());
        }

        return false;
    }

    @Unique
    private static final Set<String> VANILLA_WOODS = Set.of(
        "oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo", "crimson", "warped"
    );
}