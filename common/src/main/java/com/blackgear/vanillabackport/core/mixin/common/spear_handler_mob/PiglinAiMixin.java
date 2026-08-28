package com.blackgear.vanillabackport.core.mixin.common.spear_handler_mob;

import com.blackgear.vanillabackport.common.level.entities.ai.behavior.SpearApproach;
import com.blackgear.vanillabackport.common.level.entities.ai.behavior.SpearAttack;
import com.blackgear.vanillabackport.common.level.entities.ai.behavior.SpearRetreat;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {
    @ModifyArg(
        method = "initFightActivity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/Brain;addActivityAndRemoveMemoryWhenStopped(Lnet/minecraft/world/entity/schedule/Activity;ILcom/google/common/collect/ImmutableList;Lnet/minecraft/world/entity/ai/memory/MemoryModuleType;)V"
        ),
        index = 2
    )
    private static ImmutableList<? extends BehaviorControl<? super Piglin>> vb$initFightActivity(ImmutableList<? extends BehaviorControl<? super Piglin>> original) {
        List<BehaviorControl<? super Piglin>> mutable = new ArrayList<>(original);
        mutable.add(3, new SpearApproach(1.0, 10.0F));
        mutable.add(4, new SpearAttack(1.0, 1.0, 2.0F));
        mutable.add(5, new SpearRetreat(1.0));
        return ImmutableList.copyOf(mutable);
    }
}