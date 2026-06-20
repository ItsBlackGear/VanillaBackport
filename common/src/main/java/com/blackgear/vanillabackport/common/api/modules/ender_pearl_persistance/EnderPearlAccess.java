package com.blackgear.vanillabackport.common.api.modules.ender_pearl_persistance;

import net.minecraft.world.entity.projectile.ThrownEnderpearl;

import java.util.Set;

public interface EnderPearlAccess {
    void registerEnderPearl(ThrownEnderpearl pearl);
    
    void deregisterEnderPearl(ThrownEnderpearl pearl);
    
    Set<ThrownEnderpearl> getEnderPearls();
}