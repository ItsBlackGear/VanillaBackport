package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.EntityRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.Codec;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.function.Supplier;

public class ModMemoryModules {
    public static final EntityRegistry REGISTRAR = EntityRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<MemoryModuleType<Boolean>> DANGER_DETECTED_RECENTLY = REGISTRAR.memory("danger_detected_recently", Codec.BOOL);
}