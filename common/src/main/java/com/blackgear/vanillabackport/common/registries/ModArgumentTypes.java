package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.commands.arguments.ColorArgument;
import com.blackgear.vanillabackport.common.commands.arguments.HexColorArgument;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.common.access.ArgumentTypeInfosAccessor;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class ModArgumentTypes {
    public static final CoreRegistry<ArgumentTypeInfo<?, ?>> REGISTRIES = CoreRegistry.create(Registries.COMMAND_ARGUMENT_TYPE, VanillaBackport.NAMESPACE);
    
    static {
        register("team_color", ColorArgument.class, SingletonArgumentInfo.contextFree(ColorArgument::new));
        register("hex_color", HexColorArgument.class, SingletonArgumentInfo.contextFree(HexColorArgument::new));
    }
    
    public static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> Supplier<ArgumentTypeInfo<A, T>> register(String name, Class<? extends A> type, ArgumentTypeInfo<A, T> info) {
        ArgumentTypeInfosAccessor.getBY_CLASS().put(type, info);
        return REGISTRIES.register(name, () -> info);
    }
}