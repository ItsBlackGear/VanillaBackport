package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

import java.util.Map;

public class CopperGolemOxidationLevels {
    private static final CopperGolemOxidationLevel UNAFFECTED = new CopperGolemOxidationLevel(
        ModSoundEvents.COPPER_GOLEM_SPIN.get(),
        ModSoundEvents.COPPER_GOLEM_HURT.get(),
        ModSoundEvents.COPPER_GOLEM_DEATH.get(),
        ModSoundEvents.COPPER_GOLEM_STEP.get(),
        new ResourceLocation("textures/entity/copper_golem/copper_golem.png"),
        new ResourceLocation("textures/entity/copper_golem/copper_golem_eyes.png")
    );
    private static final CopperGolemOxidationLevel EXPOSED = new CopperGolemOxidationLevel(
        ModSoundEvents.COPPER_GOLEM_SPIN.get(),
        ModSoundEvents.COPPER_GOLEM_HURT.get(),
        ModSoundEvents.COPPER_GOLEM_DEATH.get(),
        ModSoundEvents.COPPER_GOLEM_STEP.get(),
        new ResourceLocation("textures/entity/copper_golem/exposed_copper_golem.png"),
        new ResourceLocation("textures/entity/copper_golem/exposed_copper_golem_eyes.png")
    );
    private static final CopperGolemOxidationLevel WEATHERED = new CopperGolemOxidationLevel(
        ModSoundEvents.COPPER_GOLEM_WEATHERED_SPIN.get(),
        ModSoundEvents.COPPER_GOLEM_WEATHERED_HURT.get(),
        ModSoundEvents.COPPER_GOLEM_WEATHERED_DEATH.get(),
        ModSoundEvents.COPPER_GOLEM_WEATHERED_STEP.get(),
        new ResourceLocation("textures/entity/copper_golem/weathered_copper_golem.png"),
        new ResourceLocation("textures/entity/copper_golem/weathered_copper_golem_eyes.png")
    );
    private static final CopperGolemOxidationLevel OXIDIZED = new CopperGolemOxidationLevel(
        ModSoundEvents.COPPER_GOLEM_OXIDIZED_SPIN.get(),
        ModSoundEvents.COPPER_GOLEM_OXIDIZED_HURT.get(),
        ModSoundEvents.COPPER_GOLEM_OXIDIZED_DEATH.get(),
        ModSoundEvents.COPPER_GOLEM_OXIDIZED_STEP.get(),
        new ResourceLocation("textures/entity/copper_golem/oxidized_copper_golem.png"),
        new ResourceLocation("textures/entity/copper_golem/oxidized_copper_golem_eyes.png")
    );

    private static final Map<WeatherState, CopperGolemOxidationLevel> WEATHERED_STATES = Map.of(
        WeatherState.UNAFFECTED, UNAFFECTED,
        WeatherState.EXPOSED, EXPOSED,
        WeatherState.WEATHERED, WEATHERED,
        WeatherState.OXIDIZED, OXIDIZED
    );

    public static CopperGolemOxidationLevel getOxidationLevel(WeatherState state) {
        return WEATHERED_STATES.get(state);
    }
    
    public static CopperGolemOxidationLevel getOxidationLevel(WeatheredState state) {
        return WEATHERED_STATES.get(WeatheredState.parse(state));
    }
}
