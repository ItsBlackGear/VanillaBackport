package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiConsumer;

@Mixin(GameRules.BooleanValue.class)
public interface BooleanValueAccessor {
    @Invoker
    static GameRules.Type<GameRules.BooleanValue> callCreate(boolean defaultValue, BiConsumer<MinecraftServer, GameRules.BooleanValue> changeListener) {
        throw new UnsupportedOperationException();
    }
}
