package com.blackgear.vanillabackport.client.registries;

import com.blackgear.platform.core.helper.SoundRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

public class ModSoundTypes {
    public static final SoundRegistry SOUNDS = SoundRegistry.create(VanillaBackport.NAMESPACE);

    public static final SoundType CREAKING_HEART = SOUNDS.soundType(
        ModSoundEvents.CREAKING_HEART_BREAK,
        ModSoundEvents.CREAKING_HEART_STEP,
        ModSoundEvents.CREAKING_HEART_PLACE,
        ModSoundEvents.CREAKING_HEART_HIT,
        ModSoundEvents.CREAKING_HEART_FALL
    );
    public static final SoundType RESIN = SOUNDS.soundType(
        ModSoundEvents.RESIN_BREAK,
        ModSoundEvents.RESIN_STEP,
        ModSoundEvents.RESIN_PLACE,
        () -> SoundEvents.EMPTY,
        ModSoundEvents.RESIN_FALL
    );
    public static final SoundType RESIN_BRICKS = SOUNDS.soundType(
        ModSoundEvents.RESIN_BRICKS_BREAK,
        ModSoundEvents.RESIN_BRICKS_STEP,
        ModSoundEvents.RESIN_BRICKS_PLACE,
        ModSoundEvents.RESIN_BRICKS_HIT,
        ModSoundEvents.RESIN_BRICKS_FALL
    );
    public static final SoundType DRIED_GHAST = SOUNDS.soundType(
        ModSoundEvents.DRIED_GHAST_BREAK,
        ModSoundEvents.DRIED_GHAST_STEP,
        () -> SoundEvents.EMPTY,
        () -> SoundEvents.EMPTY,
        ModSoundEvents.DRIED_GHAST_FALL
    );
    public static final SoundType LEAF_LITTER = SOUNDS.soundType(
        ModSoundEvents.LEAF_LITTER_BREAK,
        ModSoundEvents.LEAF_LITTER_STEP,
        ModSoundEvents.LEAF_LITTER_PLACE,
        ModSoundEvents.LEAF_LITTER_HIT,
        ModSoundEvents.LEAF_LITTER_FALL
    );
    public static final SoundType CACTUS_FLOWER = SOUNDS.soundType(
        ModSoundEvents.CACTUS_FLOWER_BREAK,
        () -> SoundEvents.EMPTY,
        ModSoundEvents.CACTUS_FLOWER_PLACE,
        () -> SoundEvents.EMPTY,
        () -> SoundEvents.EMPTY
    );
}