//package com.blackgear.vanillabackport.common.worldgen.grower;
//
//import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
//import com.blackgear.vanillabackport.core.VanillaBackport;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
//import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//
//public class PaleOakTreeGrower extends TreeGrower {
//    @Override
//    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
//        return null;
//    }
//
//    @Override
//    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
//        if (VanillaBackport.CONFIG.spawnCreakingHeartsFromSaplings.get()) {
//            return random.nextFloat() < 0.1F
//                ? TheGardenAwakensFeatures.PALE_OAK_BONEMEAL
//                : TheGardenAwakensFeatures.PALE_OAK_CREAKING_BONEMEAL;
//        }
//
//        return TheGardenAwakensFeatures.PALE_OAK_BONEMEAL;
//    }
//}