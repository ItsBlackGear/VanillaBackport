package com.blackgear.vanillabackport.common.registries;

import com.blackgear.vanillabackport.common.criterion.PlayerShearedEquipmentTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;

public class ModCriteriaTriggers {
    public static final PlayerShearedEquipmentTrigger PLAYER_SHEARED_EQUIPMENT = register(new PlayerShearedEquipmentTrigger());

    public static void bootstrap() {}

    private static <T extends CriterionTrigger<?>> T register(T trigger) {
        return CriteriaTriggers.register(trigger);
    }
}