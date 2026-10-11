package com.blackgear.vanillabackport.core.mixin.common.monsters_on_peaceful;

import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {
//    @ModifyVariable(
//        method = "spawnForChunk",
//        at = @At("HEAD"),
//        ordinal = 1,
//        argsOnly = true
//    )
//    private static boolean vb$allowMonsterLoopInPeaceful(boolean spawnMonsters, ServerLevel level) {
//        return level.getDifficulty() == Difficulty.PEACEFUL || spawnMonsters;
//    }
//
//    @Inject(
//        method = "isValidSpawnPostitionForType",
//        at = @At("HEAD"),
//        cancellable = true
//    )
//    private static void vb$filterPeacefulMonsters(
//        ServerLevel level,
//        MobCategory category,
//        StructureManager structureManager,
//        ChunkGenerator generator,
//        MobSpawnSettings.SpawnerData data,
//        BlockPos.MutableBlockPos pos,
//        double distance,
//        CallbackInfoReturnable<Boolean> cir
//    ) {
//        if (category == MobCategory.MONSTER && level.getDifficulty() == Difficulty.PEACEFUL) {
//            if (!data.type.is(ModEntityTypeTags.MONSTERS_THAT_SPAWN_ON_PEACEFUL)) {
//                cir.setReturnValue(false);
//            }
//        }
//    }
}