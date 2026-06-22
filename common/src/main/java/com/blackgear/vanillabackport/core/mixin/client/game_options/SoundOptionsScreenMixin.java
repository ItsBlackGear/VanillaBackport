package com.blackgear.vanillabackport.core.mixin.client.game_options;

import com.blackgear.vanillabackport.client.api.modules.options.OptionsAccess;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundOptionsScreen.class)
public abstract class SoundOptionsScreenMixin extends OptionsSubScreen {
    public SoundOptionsScreenMixin(Screen lastScreen, Options options, Component title) {
        super(lastScreen, options, title);
    }
    
    @Inject(method = "addOptions", at = @At("TAIL"))
    private void a(CallbackInfo ci) {
        OptionsAccess access = (OptionsAccess) this.options;
        this.list.addSmall(access.musicFrequency(), access.musicToast());
    }
}