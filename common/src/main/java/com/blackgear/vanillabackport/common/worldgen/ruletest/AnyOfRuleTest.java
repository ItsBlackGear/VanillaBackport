package com.blackgear.vanillabackport.common.worldgen.ruletest;

import com.blackgear.vanillabackport.common.registries.worldgen.ModRuleTestTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.List;

public class AnyOfRuleTest extends RuleTest {
    public static final MapCodec<AnyOfRuleTest> CODEC = RuleTest.CODEC.listOf().fieldOf("rules").xmap(AnyOfRuleTest::new, rule -> rule.rules);
    private final List<RuleTest> rules;
    
    public static AnyOfRuleTest of(RuleTest... rules) {
        return new AnyOfRuleTest(List.of(rules));
    }
    
    public AnyOfRuleTest(List<RuleTest> rules) {
        this.rules = rules;
    }
    
    @Override
    public boolean test(BlockState state, RandomSource random) {
        for (RuleTest rule : this.rules) {
            if (rule.test(state, random)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
    protected RuleTestType<?> getType() {
        return ModRuleTestTypes.ANY_OF.get();
    }
}