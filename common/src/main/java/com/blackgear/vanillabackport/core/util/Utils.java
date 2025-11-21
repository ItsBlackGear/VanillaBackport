package com.blackgear.vanillabackport.core.util;

import com.blackgear.platform.core.Environment;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;

public class Utils {
    public static RegistryAccess getRegistryAccess() {
        var s = Environment.getCurrentServer();
        if (Environment.isClientSide()) {
            if (s.isPresent() && (s.get().isSameThread() || !Minecraft.getInstance().isSameThread())) return s.get().registryAccess();
            var level = Minecraft.getInstance().level;
            if (level != null) return level.registryAccess();
            var hack = VanillaBackport.EARLY_REGISTRY_ACCESS.get();
            if (hack != null) return hack.get();
            throw new UnsupportedOperationException("Registry access is not available yet!");
        }

        if (s.isPresent()) return s.get().registryAccess();
        var hack = VanillaBackport.EARLY_REGISTRY_ACCESS.get();
        if (hack != null) return hack.get();
        throw new UnsupportedOperationException("Registry access is not available yet!");
    }
}