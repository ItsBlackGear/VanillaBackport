package com.blackgear.vanillabackport.core.mixin.client.spear_behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.ServerSpearHandler;
import com.blackgear.vanillabackport.common.api.extensions.entity.arms.PlayerActions;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.common.level.components.PiercingWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin implements ServerSpearHandler {
    @Shadow protected abstract void ensureHasSentCarriedItem();
    @Shadow @Final private ClientPacketListener connection;
    @Shadow @Final private Minecraft minecraft;
    
    @Override
    public void piercingAttack(PiercingWeapon piercingWeapon) {
        this.ensureHasSentCarriedItem();
        this.connection.send(new ServerboundPlayerActionPacket(PlayerActions.STAB.get(), BlockPos.ZERO, Direction.DOWN));
        ((MobSpearHandler) this.minecraft.player).vb$onAttack();
        ((MobSpearHandler) this.minecraft.player).vb$postPiercingAttack();
        piercingWeapon.makeSound(this.minecraft.player);
    }
}