package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.api.registrar.Registrar;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ModAttributes {
    public static final Registrar<Attribute> ATTRIBUTES = Registrar.create(Registries.ATTRIBUTE, VanillaBackport.NAMESPACE);

    public static final Attribute AIR_DRAG_MODIFIER = ATTRIBUTES.register(
        "air_drag_modifier",
        new RangedAttribute(
            "attribute.name.air_drag_modifier",
            1.0,
            0.0,
            2048.0
        ).setSyncable(true)
    );

    public static final Attribute BOUNCINESS = ATTRIBUTES.register(
        "bounciness",
        new RangedAttribute(
            "attribute.name.bounciness",
            0.0,
            0.0,
            1.0
        ).setSyncable(true)
    );

    public static final Attribute FRICTION_MODIFIER = ATTRIBUTES.register(
        "friction_modifier",
        new RangedAttribute(
            "attribute.name.friction_modifier",
            1.0,
            0.0,
            2048.0
        ).setSyncable(true)
    );
}