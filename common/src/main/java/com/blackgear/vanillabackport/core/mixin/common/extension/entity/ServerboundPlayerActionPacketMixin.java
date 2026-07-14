package com.blackgear.vanillabackport.core.mixin.common.extension.entity;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.PlayerActions;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
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

@Mixin(ServerboundPlayerActionPacket.Action.class)
public class ServerboundPlayerActionPacketMixin {
    @Shadow @Mutable @Final private static ServerboundPlayerActionPacket.Action[] $VALUES;

    @Invoker("<init>")
    public static ServerboundPlayerActionPacket.Action create(String name, int ordinal) {
        throw new AssertionError();
    }

    @Inject(
        method = "<clinit>",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;$VALUES:[Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket$Action;",
            shift = At.Shift.AFTER,
            opcode = Opcodes.PUTSTATIC
        ))
    private static void vb$addCustomAction(CallbackInfo ci) {
        List<ServerboundPlayerActionPacket.Action> actions = new ArrayList<>(Arrays.asList($VALUES));
        int ordinal = actions.get(actions.size() - 1).ordinal() + 1;
        PlayerActions[] additions = PlayerActions.values();
        for (int i = 0; i < additions.length; i++) {
            actions.add(create(additions[i].name(), ordinal + i));
        }
        $VALUES = actions.toArray(new ServerboundPlayerActionPacket.Action[0]);
    }
}