package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiConsumer;

@Mixin(BooleanValue.class)
public interface BooleanValueAccessor {
    @Invoker
    static GameRules.Type<BooleanValue> callCreate(boolean defaultValue, BiConsumer<MinecraftServer, BooleanValue> changeListener) {
        throw new UnsupportedOperationException();
    }
}
