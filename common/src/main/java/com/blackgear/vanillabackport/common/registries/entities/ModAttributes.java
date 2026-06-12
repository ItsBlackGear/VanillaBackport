package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModAttributes {
    public static final CoreRegistry<Attribute> REGISTRIES = CoreRegistry.create(BuiltInRegistries.ATTRIBUTE, VanillaBackport.NAMESPACE);

    public static final Holder<Attribute> AIR_DRAG_MODIFIER = REGISTRIES.holder("air_drag_modifier",
        () -> new RangedAttribute(
            "attribute.name.air_drag_modifier",
            1.0,
            0.0,
            2048.0
        ).setSyncable(true)
    );
    public static final Holder<Attribute> BOUNCINESS = REGISTRIES.holder("bounciness",
        () -> new RangedAttribute(
            "attribute.name.bounciness",
            0.0,
            0.0,
            1.0
        ).setSyncable(true)
    );
    public static final Holder<Attribute> FRICTION_MODIFIER = REGISTRIES.holder("friction_modifier",
        () -> new RangedAttribute(
            "attribute.name.friction_modifier",
            1.0,
            0.0,
            2048.0
        ).setSyncable(true)
    );
}