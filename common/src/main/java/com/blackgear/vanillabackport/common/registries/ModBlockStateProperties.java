package com.blackgear.vanillabackport.common.registries;

import com.blackgear.vanillabackport.common.level.blocks.states.CreakingHeartState;
import com.blackgear.vanillabackport.common.level.blocks.states.PotentSulfurState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {
    public static final BooleanProperty TIP = BooleanProperty.create("tip");
    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
    public static final EnumProperty<CreakingHeartState> CREAKING_HEART_STATE = EnumProperty.create("creaking_heart_state", CreakingHeartState.class);
    public static final EnumProperty<PotentSulfurState> POTENT_SULFUR_STATE = EnumProperty.create("potent_sulfur_state", PotentSulfurState.class);
    public static final IntegerProperty HYDRATION_LEVEL = IntegerProperty.create("hydration", 0, 3);
    public static final IntegerProperty SEGMENT_AMOUNT = IntegerProperty.create("segment_amount", 1, 4);
}