package com.blackgear.vanillabackport.common.level.entities.projectile;

import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariant;
import com.blackgear.vanillabackport.core.registries.ModBuiltinRegistries;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class VariantThrownEgg extends ThrowableItemProjectile {
    private final ResourceKey<ChickenVariant> variant;

    public VariantThrownEgg(Level level, LivingEntity shooter, ItemStack stack, ResourceKey<ChickenVariant> variant) {
        super(EntityType.EGG, shooter, level);
        this.variant = variant;
        this.setItem(stack);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; i++) {
                this.level()
                    .addParticle(
                        new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
                        this.getX(),
                        this.getY(),
                        this.getZ(),
                        ((double) this.random.nextFloat() - 0.5) * 0.08,
                        ((double) this.random.nextFloat() - 0.5) * 0.08,
                        ((double) this.random.nextFloat() - 0.5) * 0.08
                    );
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            if (this.random.nextInt(8) == 0) {
                int i = 1;
                if (this.random.nextInt(32) == 0) {
                    i = 4;
                }

                for (int j = 0; j < i; j++) {
                    Chicken chicken = EntityType.CHICKEN.create(this.level());
                    if (chicken != null) {
                        ChickenVariant variant = ModBuiltinRegistries.CHICKEN_VARIANTS.getOrThrow(this.variant);
                        if (variant != null && chicken instanceof VariantHolder<?> holder) {
                            ((VariantHolder<ChickenVariant>) holder).setVariant(variant);
                        }
                        chicken.setAge(-24000);
                        chicken.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                        this.level().addFreshEntity(chicken);
                    }
                }
            }

            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.EGG;
    }
}