package com.blackgear.vanillabackport.common.integrations.compat;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.wolf.WolfVariant;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.wolf.WolfVariants;
import com.blackgear.vanillabackport.core.ModChecker;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class BackportedWolvesConversion {
    public static final String BW_VARIANT_KEY = "Variant";
    public static final String VANILLA_VARIANT_KEY = "variant";

    public static void migrateWolfVariant(VariantDataHolder<WolfVariant> entity, CompoundTag tag, BuiltInCoreRegistry<WolfVariant> registry) {
        // Don't apply conversion if backported wolves is loaded or wolf variants are disabled
        if (ModChecker.BACKPORTED_WOLVES || !VanillaBackport.COMMON_CONFIG.hasWolfVariants.get()) return;

        if (tag.contains(BW_VARIANT_KEY, Tag.TAG_INT) && !tag.contains(VANILLA_VARIANT_KEY, Tag.TAG_STRING)) {
            int legacyId = tag.getInt(BW_VARIANT_KEY);
            WolfVariant mapped = switch (legacyId) {
                case 0 -> registry.getOrThrow(WolfVariants.PALE);
                case 1 -> registry.getOrThrow(WolfVariants.WOODS);
                case 2 -> registry.getOrThrow(WolfVariants.ASHEN);
                case 3 -> registry.getOrThrow(WolfVariants.BLACK);
                case 4 -> registry.getOrThrow(WolfVariants.CHESTNUT);
                case 5 -> registry.getOrThrow(WolfVariants.RUSTY);
                case 6 -> registry.getOrThrow(WolfVariants.SPOTTED);
                case 7 -> registry.getOrThrow(WolfVariants.STRIPED);
                case 8 -> registry.getOrThrow(WolfVariants.SNOWY);
                default -> registry.getOrThrow(WolfVariants.PALE); // safe fallback
            };

            entity.setVariantData(mapped);
            VariantUtils.addVariantSaveData(entity, tag, registry);
            tag.remove(BW_VARIANT_KEY);
        }
    }
}