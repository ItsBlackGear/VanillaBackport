package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.helper.EntityRegistry;
import com.blackgear.vanillabackport.common.level.entity.ai.behavior.SpearStatus;
import com.blackgear.vanillabackport.core.FeatureFlag;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;

import java.util.Set;
import java.util.function.Supplier;

public class ModMemoryModuleTypes {
    public static final EntityRegistry REGISTRIES = EntityRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<MemoryModuleType<Boolean>> DANGER_DETECTED_RECENTLY = REGISTRIES.memory("danger_detected_recently", Codec.BOOL);
    public static final Supplier<MemoryModuleType<Integer>> TRANSPORT_ITEMS_COOLDOWN_TICKS = REGISTRIES.memory("transport_items_cooldown_ticks");
    public static final Supplier<MemoryModuleType<Set<GlobalPos>>> VISITED_BLOCK_POSITIONS = REGISTRIES.memory("visited_block_positions", GlobalPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList));
    public static final Supplier<MemoryModuleType<Set<GlobalPos>>> UNREACHABLE_TRANSPORT_BLOCK_POSITIONS = REGISTRIES.memory("unreachable_transport_block_positions", GlobalPos.CODEC.listOf().xmap(Sets::newHashSet, Lists::newArrayList));
    
    public static final Supplier<MemoryModuleType<Integer>> CHARGE_COOLDOWN_TICKS = REGISTRIES.memory("charge_cooldown_ticks", Codec.INT);
    public static final Supplier<MemoryModuleType<Integer>> ATTACK_TARGET_COOLDOWN = REGISTRIES.memory("attack_target_cooldown", Codec.INT);
    
    public static final Supplier<MemoryModuleType<Integer>> SPEAR_FLEEING_TIME = REGISTRIES.memory("spear_fleeing_time");
    public static final Supplier<MemoryModuleType<Vec3>> SPEAR_FLEEING_POSITION = REGISTRIES.memory("spear_fleeing_position");
    public static final Supplier<MemoryModuleType<Vec3>> SPEAR_CHARGE_POSITION = REGISTRIES.memory("spear_charge_position");
    public static final Supplier<MemoryModuleType<Integer>> SPEAR_ENGAGE_TIME = REGISTRIES.memory("spear_engage_time");
    public static final Supplier<MemoryModuleType<SpearStatus>> SPEAR_STATUS = REGISTRIES.memory("spear_status");
}