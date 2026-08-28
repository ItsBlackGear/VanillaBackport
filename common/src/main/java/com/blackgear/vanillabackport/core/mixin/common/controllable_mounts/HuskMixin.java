package com.blackgear.vanillabackport.core.mixin.common.controllable_mounts;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.camel.CamelHusk;
import com.blackgear.vanillabackport.common.level.entities.mob.monster.skeleton.Parched;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Husk.class)
public class HuskMixin extends Zombie {
    public HuskMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
        RandomSource random = level.getRandom();
        data = super.finalizeSpawn(level, difficulty, reason, data);
        float difficultyModifier = difficulty.getSpecialMultiplier();
        if (reason != MobSpawnType.CONVERSION) {
            this.setCanPickUpLoot(random.nextFloat() < 0.55F * difficultyModifier);
        }
        
        if (data != null) {
            data = new CamelHusk.HuskGroupData((ZombieGroupData) data);
            ((CamelHusk.HuskGroupData) data).triedToSpawnCamelHusk = reason != MobSpawnType.NATURAL;
        }
        
        if (data instanceof CamelHusk.HuskGroupData huskData && !huskData.triedToSpawnCamelHusk) {
            BlockPos pos = this.blockPosition();
            if (level.noCollision(ModEntityTypes.CAMEL_HUSK.get().getSpawnAABB(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5))) {
                huskData.triedToSpawnCamelHusk = true;
                if (random.nextFloat() < 0.1F) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR.get()));
                    CamelHusk camel = ModEntityTypes.CAMEL_HUSK.get().create(this.level());
                    if (camel != null) {
                        camel.setPos(this.getX(), this.getY(), this.getZ());
                        camel.finalizeSpawn(level, difficulty, reason, null);
                        this.startRiding(camel, true);
                        level.addFreshEntity(camel);
                        Parched parched = ModEntityTypes.PARCHED.get().create(this.level());
                        if (parched != null) {
                            parched.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                            parched.finalizeSpawn(level, difficulty, reason, null);
                            parched.startRiding(camel, false);
                            level.addFreshEntityWithPassengers(parched);
                        }
                    }
                }
            }
        }
        
        return data;
    }
}