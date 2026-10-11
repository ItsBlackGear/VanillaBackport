package com.blackgear.vanillabackport.core.mixin.common.controllable_mounts;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.MobBehaviorAccess;
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
public class HuskMixin extends Zombie implements MobBehaviorAccess {
    public HuskMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public SpawnGroupData vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData data) {
        Husk husk = (Husk) (Object) this;
        RandomSource random = level.getRandom();
        
        if (data != null) {
            data = new CamelHusk.HuskGroupData((Zombie.ZombieGroupData) data);
            ((CamelHusk.HuskGroupData) data).triedToSpawnCamelHusk = (reason != MobSpawnType.NATURAL);
        }
        
        if (data instanceof CamelHusk.HuskGroupData huskData && !huskData.triedToSpawnCamelHusk) {
            BlockPos pos = husk.blockPosition();
            if (level.noCollision(ModEntityTypes.CAMEL_HUSK.get().getSpawnAABB(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5))) {
                huskData.triedToSpawnCamelHusk = true;
                if (random.nextFloat() < 0.1F) {
                    husk.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_SPEAR.get()));
                    CamelHusk camel = ModEntityTypes.CAMEL_HUSK.get().create(husk.level());
                    if (camel != null) {
                        camel.setPos(husk.getX(), husk.getY(), husk.getZ());
                        camel.finalizeSpawn(level, difficulty, reason, null);
                        husk.startRiding(camel, true);
                        level.addFreshEntity(camel);
                        
                        Parched parched = ModEntityTypes.PARCHED.get().create(husk.level());
                        if (parched != null) {
                            parched.moveTo(husk.getX(), husk.getY(), husk.getZ(), husk.getYRot(), 0.0F);
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