package com.blackgear.vanillabackport.common.integrations.dispenser;

import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantUtils;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariants;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EggVariantProjectileDispenseBehavior extends AbstractProjectileDispenseBehavior {
    public final RegistryKey<ChickenVariant> variant;
    
    public EggVariantProjectileDispenseBehavior(RegistryKey<ChickenVariant> variant) {
        this.variant = variant;
    }
    
    @Override
    protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
        ThrownEgg thrownEgg = new ThrownEgg(level, position.x(), position.y(), position.z());
        VariantDataHolder.<ChickenVariant>getHolder(thrownEgg).setVariantData(VariantUtils.getDefault(ChickenVariants.REGISTRIES, this.variant));
        return Util.make(thrownEgg, egg -> egg.setItem(stack));
    }
}
