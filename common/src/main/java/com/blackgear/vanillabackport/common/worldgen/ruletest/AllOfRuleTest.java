package com.blackgear.vanillabackport.common.worldgen.ruletest;

import com.blackgear.vanillabackport.common.registries.worldgen.ModRuleTestTypes;
import com.mojang.serialization.Codec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.List;

public class AllOfRuleTest extends RuleTest {
    public static final Codec<AllOfRuleTest> CODEC = RuleTest.CODEC.listOf().fieldOf("rules").xmap(AllOfRuleTest::new, rule -> rule.rules).codec();
    private final List<RuleTest> rules;
    
    public static AllOfRuleTest of(RuleTest... rules) {
        return new AllOfRuleTest(List.of(rules));
    }
    
    public AllOfRuleTest(List<RuleTest> rules) {
        this.rules = rules;
    }
    
    @Override
    public boolean test(BlockState state, RandomSource random) {
        for (RuleTest rule : this.rules) {
            if (!rule.test(state, random)) {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    protected RuleTestType<?> getType() {
        return ModRuleTestTypes.ALL_OF.get();
    }
}