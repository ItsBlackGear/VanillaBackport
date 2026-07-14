package com.blackgear.vanillabackport.core.mixin.common.spears;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.ItemUseAnimations;
import net.minecraft.world.item.UseAnim;
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

@Mixin(UseAnim.class)
public class UseAnimMixin {
    @Shadow @Mutable @Final private static UseAnim[] $VALUES;

    @Invoker("<init>")
    public static UseAnim create(String name, int ordinal) {
        throw new AssertionError();
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/UseAnim;$VALUES:[Lnet/minecraft/world/item/UseAnim;",
            shift = At.Shift.AFTER,
            opcode = Opcodes.PUTSTATIC
        ))
    private static void vb$addCustomAction(CallbackInfo ci) {
        List<UseAnim> actions = new ArrayList<>(Arrays.asList($VALUES));
        int ordinal = actions.get(actions.size() - 1).ordinal() + 1;
        ItemUseAnimations[] additions = ItemUseAnimations.values();
        for (int i = 0; i < additions.length; i++) {
            actions.add(create(additions[i].name(), ordinal + i));
        }
        $VALUES = actions.toArray(new UseAnim[0]);
    }
}