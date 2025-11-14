package com.blackgear.vanillabackport.data.client;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

// Example Forge Sound Definitions Provider use case. - Echo2craft.
public class SoundDefinitionGenerator extends SoundDefinitionsProvider {
    public static final String BLOCK_GENERIC_PLACE = "subtitles.block.generic.place";
    public static final String BLOCK_GENERIC_BREAK = "subtitles.block.generic.break";
    public static final String BLOCK_GENERIC_FALL = "subtitles.block.generic.fall";
    public static final String BLOCK_GENERIC_HIT = "subtitles.block.generic.hit";
    public static final String BLOCK_GENERIC_FOOTSTEPS = "subtitles.block.generic.footsteps";
    public static final String BLOCK_DOOR_TOGGLE = "subtitles.block.door.toggle";
    public static final String BLOCK_TRAPDOOR_CLOSE = "subtitles.block.trapdoor.close";
    public static final String BLOCK_TRAPDOOR_OPEN = "subtitles.block.trapdoor.open";
    public static final String ITEM_SHEARS_SHEAR = "subtitles.item.shears.shear";
    /**
     * Creates a new instance of this data provider.
     *
     * @param output The {@linkplain PackOutput} instance provided by the data generator.
     * @param modId  The mod ID of the current mod.
     * @param helper The existing file helper provided by the event you are initializing this provider in.
     */
    public SoundDefinitionGenerator(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
    }

    @Override
    public void registerSounds() {
        add(ModSoundEvents.OMINOUS_BOTTLE_DISPOSE,
                definition().subtitle(getSubtitleOf(ModSoundEvents.OMINOUS_BOTTLE_DISPOSE.get()))
                        .with(sound(getSoundId(ModSoundEvents.OMINOUS_BOTTLE_DISPOSE.get())))
        );
        add(ModSoundEvents.APPLY_EFFECT_RAID_OMEN,
                definition().subtitle(getSubtitleOf(ModSoundEvents.APPLY_EFFECT_RAID_OMEN.get()))
                        .with(sound(getSoundId(ModSoundEvents.APPLY_EFFECT_RAID_OMEN.get())))
        );
        add(ModSoundEvents.APPLY_EFFECT_BAD_OMEN,
                definition().subtitle(getSubtitleOf(ModSoundEvents.APPLY_EFFECT_BAD_OMEN.get()))
                        .with(sound(getSoundId(ModSoundEvents.APPLY_EFFECT_BAD_OMEN.get())))
        );
        add(ModSoundEvents.APPLY_EFFECT_TRIAL_OMEN,
                definition().subtitle(getSubtitleOf(ModSoundEvents.APPLY_EFFECT_TRIAL_OMEN.get()))
                        .with(sound(getSoundId(ModSoundEvents.APPLY_EFFECT_TRIAL_OMEN.get())))
        );
    }

    // Little helper for subtitles.
    private String getSubtitleOf(SoundEvent pSound){
        // Either way works.
        // return pSound.getLocation().withPrefix("subtitles.").getPath();
        return "subtitles." + pSound.getLocation().getPath();
    }

    // Old code.
    private String getSoundId(SoundEvent pSoundEvent, String pNamespace){
        return pNamespace + ":" + pSoundEvent.getLocation().getPath().replace(".","/");
    }

    // Little helper for sound file location.
    private String getSoundId(SoundEvent pSoundEvent){
        return pSoundEvent.getLocation().toString().replace(".","/");
    }

}
