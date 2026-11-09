package com.blackgear.vanillabackport.client.api;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.network.handlers.LevelEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

@Environment(EnvType.CLIENT)
public class LevelEventHandler {
    public static final LevelEventHandler INSTANCE = new LevelEventHandler();
    
    public void levelEvent(LevelEvents event, BlockPos pos, int data) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            RandomSource random = level.getRandom();
    
            switch (event) {
                case SOUND_SULFUR_SPIKE_LAND -> level.playLocalSound(pos, SoundEvents.POINTED_DRIPSTONE_LAND, SoundSource.BLOCKS, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
                case SOUND_ICICLE_LAND -> level.playLocalSound(pos, ModSoundEvents.ICICLE_LAND.get(), SoundSource.BLOCKS, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
                case CONVERT_ZOMBIE_TO_FROSTBITE -> level.playLocalSound(pos, ModSoundEvents.ZOMBIE_CONVERTED_TO_FROSTBITE.get(), SoundSource.HOSTILE, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
                case CONVERT_FROSTBITE_TO_ZOMBIE -> level.playLocalSound(pos, ModSoundEvents.FROSTBITE_CONVERTED_TO_ZOMBIE.get(), SoundSource.HOSTILE, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
                case CONVERT_DROWNED_TO_ZOMBIE -> level.playLocalSound(pos, SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                default -> {}
            }
        }
    }
}