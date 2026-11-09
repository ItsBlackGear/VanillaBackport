package com.blackgear.vanillabackport.common.worldgen.ruletest;

import com.blackgear.vanillabackport.common.api.extensions.worldgen.PosAwareRuleTest;
import com.blackgear.vanillabackport.common.registries.worldgen.ModRuleTestTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class HeightMatchTest extends RuleTest implements PosAwareRuleTest {
    public static final Codec<HeightMatchTest> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("min_inclusive").forGetter(rule -> rule.minInclusive),
        Codec.INT.fieldOf("max_inclusive").forGetter(rule -> rule.maxInclusive)
    ).apply(instance, HeightMatchTest::new));
    private final int minInclusive;
    private final int maxInclusive;
    
    public HeightMatchTest(int minInclusive, int maxInclusive) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }
    
    public static RuleTest min(int minInclusive) {
        return new HeightMatchTest(minInclusive, DimensionType.MAX_Y);
    }
    
    public static RuleTest max(int maxInclusive) {
        return new HeightMatchTest(DimensionType.MIN_Y, maxInclusive);
    }
    
    @Override
    public boolean test(BlockState state, BlockPos pos, RandomSource random) {
        return this.minInclusive <= pos.getY() && pos.getY() <= this.maxInclusive;
    }
    
    @Override
    public boolean test(BlockState state, RandomSource random) {
        return false;
    }
    
    @Override
    protected RuleTestType<?> getType() {
        return ModRuleTestTypes.HEIGHT_MATCH.get();
    }
}