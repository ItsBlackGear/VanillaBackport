package com.blackgear.vanillabackport.core.mixin.common.end_flashes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalLong;

@Mixin(DimensionType.class)
public class DimensionTypeMixin {
    @Mutable @Shadow @Final private boolean hasSkyLight;
    @Mutable @Shadow @Final private DimensionType.MonsterSettings monsterSettings;
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$addEndSkylight(
        OptionalLong fixedTime,
        boolean hasSkyLight,
        boolean hasCeiling,
        boolean ultraWarm,
        boolean natural,
        double coordinateScale,
        boolean bedWorks,
        boolean respawnAnchorWorks,
        int minY,
        int height,
        int localHeight,
        TagKey<BlockTags> infiniburn,
        ResourceLocation effectsLocation,
        float ambientLight,
        DimensionType.MonsterSettings monsterSettings,
        CallbackInfo ci
    ) {
        if (effectsLocation.equals(BuiltinDimensionTypes.END_EFFECTS)) {
            this.hasSkyLight = true;
            this.monsterSettings = new DimensionType.MonsterSettings(false, true, ConstantInt.of(15), 0);
        }
    }
}