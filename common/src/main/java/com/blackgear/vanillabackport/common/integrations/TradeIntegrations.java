package com.blackgear.vanillabackport.common.integrations;

import com.blackgear.platform.common.integration.TradeIntegration;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.entity.npc.VillagerTrades;

public class TradeIntegrations {
    public static void bootstrap(TradeIntegration.Event event) {
        if (VanillaBackport.COMMON_CONFIG.doMerchantTradeTheGardenAwakensContents.get()) {
            event.registerWandererTrade(
                true,
                new VillagerTrades.ItemsForEmeralds(ModBlocks.PALE_OAK_LOG.get(), 1, 8, 4, 1)
            );
            event.registerWandererTrade(
                false,
                new VillagerTrades.ItemsForEmeralds(ModBlocks.OPEN_EYEBLOSSOM.get(), 1, 1, 7, 1),
                new VillagerTrades.ItemsForEmeralds(ModBlocks.PALE_OAK_SAPLING.get(), 5, 1, 8, 1),
                new VillagerTrades.ItemsForEmeralds(ModBlocks.PALE_HANGING_MOSS.get(), 1, 3, 4, 1),
                new VillagerTrades.ItemsForEmeralds(ModBlocks.PALE_MOSS_BLOCK.get(), 1, 2, 5, 1)
            );
        }
        
        if (VanillaBackport.COMMON_CONFIG.doMerchantTradeSpringToLifeContents.get()) {
            event.registerWandererTrade(
                false,
                new VillagerTrades.ItemsForEmeralds(ModBlocks.WILDFLOWERS.get(), 1, 1, 12, 1),
                new VillagerTrades.ItemsForEmeralds(ModBlocks.TALL_DRY_GRASS.get(), 1, 1, 12, 1),
                new VillagerTrades.ItemsForEmeralds(ModBlocks.FIREFLY_BUSH.get(), 3, 1, 12, 1)
            );
        }
        
        if (VanillaBackport.COMMON_CONFIG.doMerchantTradeChaosCubedContents.get()) {
            event.registerWandererTrade(
                false,
                new VillagerTrades.ItemsForEmeralds(ModBlocks.SULFUR_SPIKE.get(), 1, 2, 5, 1)
            );
        }
    }
}