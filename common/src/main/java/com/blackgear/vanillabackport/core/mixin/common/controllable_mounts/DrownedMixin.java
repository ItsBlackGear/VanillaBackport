package com.blackgear.vanillabackport.core.mixin.common.controllable_mounts;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus.ZombieNautilus;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Drowned.class)
public class DrownedMixin extends Zombie {
    public DrownedMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "finalizeSpawn", at = @At("HEAD"))
    private void vb$spawnAlongNautilus(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        if (!VanillaBackport.COMMON_CONFIG.hasZombieNautilus.get()) return;
        
        if ((reason == MobSpawnType.NATURAL || reason == MobSpawnType.STRUCTURE)
            && this.getMainHandItem().is(Items.TRIDENT)
            && level.getRandom().nextFloat() < 0.5F
            && !this.isBaby()
            && !level.getBiome(this.blockPosition()).is(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
            ZombieNautilus nautilus = ModEntityTypes.ZOMBIE_NAUTILUS.get().create(this.level());
            if (nautilus != null) {
                if (reason == MobSpawnType.STRUCTURE) nautilus.setPersistenceRequired();
                
                nautilus.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                nautilus.finalizeSpawn(level, difficulty, reason, null, dataTag);
                this.startRiding(nautilus, false);
                level.addFreshEntity(nautilus);
            }
        }
    }
}