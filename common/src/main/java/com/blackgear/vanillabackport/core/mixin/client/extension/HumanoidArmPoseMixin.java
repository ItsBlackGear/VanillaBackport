package com.blackgear.vanillabackport.core.mixin.client.extension;

import com.blackgear.vanillabackport.common.api.extensions.entity.arms.ArmPoses;
import net.minecraft.client.model.HumanoidModel;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(HumanoidModel.ArmPose.class)
public class HumanoidArmPoseMixin {
    @Shadow @Mutable @Final private static HumanoidModel.ArmPose[] $VALUES;

    @Invoker("<init>")
    public static HumanoidModel.ArmPose create(String name, int ordinal, boolean twoHanded) {
        throw new AssertionError();
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/model/HumanoidModel$ArmPose;$VALUES:[Lnet/minecraft/client/model/HumanoidModel$ArmPose;",
            shift = At.Shift.AFTER,
            opcode = Opcodes.PUTSTATIC))
    private static void vb$addCustomAction(CallbackInfo ci) {
        List<HumanoidModel.ArmPose> actions = new ArrayList<>(Arrays.asList($VALUES));
        int ordinal = actions.get(actions.size() - 1).ordinal() + 1;
        ArmPoses[] additions = ArmPoses.values();
        for (int i = 0; i < additions.length; i++) {
            actions.add(create(additions[i].name(), ordinal + i, additions[i].isTwoHanded()));
        }
        $VALUES = actions.toArray(new HumanoidModel.ArmPose[0]);
    }
}