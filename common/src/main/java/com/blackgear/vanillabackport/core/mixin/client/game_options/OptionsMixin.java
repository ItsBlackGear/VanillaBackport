package com.blackgear.vanillabackport.core.mixin.client.game_options;

import com.blackgear.vanillabackport.client.experiment.music_frequency.MusicFrequency;
import com.blackgear.vanillabackport.client.experiment.music_frequency.MusicFrequencyAccess;
import com.blackgear.vanillabackport.client.experiment.music_toast.MusicToastAccess;
import com.blackgear.vanillabackport.client.experiment.music_toast.MusicToastDisplayState;
import com.blackgear.vanillabackport.client.experiment.options.OptionsAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Options.class)
public class OptionsMixin implements OptionsAccess {
    @Shadow protected Minecraft minecraft;
    
    @Unique private OptionInstance<MusicFrequency> vb$musicFrequency;
    @Unique private OptionInstance<MusicToastDisplayState> vb$musicToast;
    
    @Override
    public OptionInstance<MusicFrequency> musicFrequency() {
        if (this.vb$musicFrequency == null) {
            this.vb$musicFrequency = new OptionInstance<>(
                "options.music_frequency",
                OptionInstance.cachedConstantTooltip(Component.translatable("options.music_frequency.tooltip")),
                (caption, value) -> value.caption(),
                new OptionInstance.Enum<>(Arrays.asList(MusicFrequency.values()), MusicFrequency.CODEC),
                MusicFrequency.DEFAULT,
                value -> {
                    if (this.minecraft != null && this.minecraft.getMusicManager() instanceof MusicFrequencyAccess access) {
                        access.setMinutesBetweenSongs(value);
                    }
                }
            );
        }
        return this.vb$musicFrequency;
    }
    
    @Override
    public OptionInstance<MusicToastDisplayState> musicToast() {
        if (this.vb$musicToast == null) {
            this.vb$musicToast = new OptionInstance<>(
                "options.musicToast",
                value -> Tooltip.create(value.tooltip()),
                (caption, value) -> value.text(),
                new OptionInstance.Enum<>(Arrays.asList(MusicToastDisplayState.values()), MusicToastDisplayState.CODEC),
                MusicToastDisplayState.NEVER,
                value -> {
                    if (this.minecraft != null && this.minecraft.getToasts() instanceof MusicToastAccess access) {
                        access.setMusicToastDisplayState(value);
                    }
                }
            );
        }
        return this.vb$musicToast;
    }
    
    @Inject(method = "processOptions", at = @At("TAIL"))
    private void vb$processCustomOptions(Options.FieldAccess access, CallbackInfo ci) {
        access.process("musicToast", this.musicToast());
        access.process("musicFrequency", this.musicFrequency());
    }
}