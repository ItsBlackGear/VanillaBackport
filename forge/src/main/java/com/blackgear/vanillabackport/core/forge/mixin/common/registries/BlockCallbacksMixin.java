package com.blackgear.vanillabackport.core.forge.mixin.common.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraftforge.registries.GameData$BlockCallbacks", remap = false)
public class BlockCallbacksMixin {
    @Inject(
        method = "onAdd(Lnet/minecraftforge/registries/IForgeRegistryInternal;Lnet/minecraftforge/registries/RegistryManager;ILnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$preventDuplicate(IForgeRegistryInternal<Block> owner, RegistryManager stage, int id, ResourceKey<Block> key, Block block, @Nullable Block oldBlock, CallbackInfo ci) {
        if (oldBlock != null && "minecraft".equals(key.location().getNamespace())) ci.cancel();
    }
}