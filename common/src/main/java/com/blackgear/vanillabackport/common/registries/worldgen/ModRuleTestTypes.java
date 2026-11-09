package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.worldgen.ruletest.AllOfRuleTest;
import com.blackgear.vanillabackport.common.worldgen.ruletest.AnyOfRuleTest;
import com.blackgear.vanillabackport.common.worldgen.ruletest.HeightMatchTest;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.function.Supplier;

public class ModRuleTestTypes {
    public static final CoreRegistry<RuleTestType<?>> REGISTRY = CoreRegistry.create(Registries.RULE_TEST, ResourceLocation.DEFAULT_NAMESPACE);
    
    public static final Supplier<RuleTestType<AnyOfRuleTest>> ANY_OF = REGISTRY.register("any_of", () -> () -> AnyOfRuleTest.CODEC);
    public static final Supplier<RuleTestType<AllOfRuleTest>> ALL_OF = REGISTRY.register("all_of", () -> () -> AllOfRuleTest.CODEC);
    public static final Supplier<RuleTestType<HeightMatchTest>> HEIGHT_MATCH = REGISTRY.register("height_match", () -> () -> HeightMatchTest.CODEC);
}