package com.blackgear.vanillabackport.client.registries;

import com.blackgear.platform.core.helper.SoundRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ModSoundEvents {
    public static final SoundRegistry SOUNDS = SoundRegistry.create(VanillaBackport.NAMESPACE);

    // BLOCKS
    public static final Supplier<SoundEvent> EYEBLOSSOM_OPEN_LONG = SOUNDS.soundEvent("block.eyeblossom.open_long");
    public static final Supplier<SoundEvent> EYEBLOSSOM_OPEN = SOUNDS.soundEvent("block.eyeblossom.open");
    public static final Supplier<SoundEvent> EYEBLOSSOM_CLOSE_LONG = SOUNDS.soundEvent("block.eyeblossom.close_long");
    public static final Supplier<SoundEvent> EYEBLOSSOM_CLOSE = SOUNDS.soundEvent("block.eyeblossom.close");
    public static final Supplier<SoundEvent> EYEBLOSSOM_IDLE = SOUNDS.soundEvent("block.eyeblossom.idle");

    public static final Supplier<SoundEvent> PALE_HANGING_MOSS_IDLE = SOUNDS.soundEvent("block.pale_hanging_moss.idle");

    public static final Supplier<SoundEvent> CREAKING_HEART_BREAK = SOUNDS.soundEvent("block.creaking_heart.break");
    public static final Supplier<SoundEvent> CREAKING_HEART_FALL = SOUNDS.soundEvent("block.creaking_heart.fall");
    public static final Supplier<SoundEvent> CREAKING_HEART_HIT = SOUNDS.soundEvent("block.creaking_heart.hit");
    public static final Supplier<SoundEvent> CREAKING_HEART_HURT = SOUNDS.soundEvent("block.creaking_heart.hurt");
    public static final Supplier<SoundEvent> CREAKING_HEART_PLACE = SOUNDS.soundEvent("block.creaking_heart.place");
    public static final Supplier<SoundEvent> CREAKING_HEART_STEP = SOUNDS.soundEvent("block.creaking_heart.step");
    public static final Supplier<SoundEvent> CREAKING_HEART_IDLE = SOUNDS.soundEvent("block.creaking_heart.idle");
    public static final Supplier<SoundEvent> CREAKING_HEART_SPAWN = SOUNDS.soundEvent("block.creaking_heart.spawn");

    public static final Supplier<SoundEvent> RESIN_BREAK = SOUNDS.soundEvent("block.resin.break");
    public static final Supplier<SoundEvent> RESIN_FALL = SOUNDS.soundEvent("block.resin.fall");
    public static final Supplier<SoundEvent> RESIN_PLACE = SOUNDS.soundEvent("block.resin.place");
    public static final Supplier<SoundEvent> RESIN_STEP = SOUNDS.soundEvent("block.resin.step");

    public static final Supplier<SoundEvent> RESIN_BRICKS_BREAK = SOUNDS.soundEvent("block.resin_bricks.break");
    public static final Supplier<SoundEvent> RESIN_BRICKS_FALL = SOUNDS.soundEvent("block.resin_bricks.fall");
    public static final Supplier<SoundEvent> RESIN_BRICKS_HIT = SOUNDS.soundEvent("block.resin_bricks.hit");
    public static final Supplier<SoundEvent> RESIN_BRICKS_PLACE = SOUNDS.soundEvent("block.resin_bricks.place");
    public static final Supplier<SoundEvent> RESIN_BRICKS_STEP = SOUNDS.soundEvent("block.resin_bricks.step");

    public static final Supplier<SoundEvent> DRIED_GHAST_BREAK = SOUNDS.soundEvent("block.dried_ghast.break");
    public static final Supplier<SoundEvent> DRIED_GHAST_STEP = SOUNDS.soundEvent("block.dried_ghast.step");
    public static final Supplier<SoundEvent> DRIED_GHAST_FALL = SOUNDS.soundEvent("block.dried_ghast.fall");
    public static final Supplier<SoundEvent> DRIED_GHAST_AMBIENT = SOUNDS.soundEvent("block.dried_ghast.ambient");
    public static final Supplier<SoundEvent> DRIED_GHAST_AMBIENT_WATER = SOUNDS.soundEvent("block.dried_ghast.ambient_water");
    public static final Supplier<SoundEvent> DRIED_GHAST_PLACE = SOUNDS.soundEvent("block.dried_ghast.place");
    public static final Supplier<SoundEvent> DRIED_GHAST_PLACE_IN_WATER = SOUNDS.soundEvent("block.dried_ghast.place_in_water");
    public static final Supplier<SoundEvent> DRIED_GHAST_TRANSITION = SOUNDS.soundEvent("block.dried_ghast.transition");

    public static final Supplier<SoundEvent> LEAF_LITTER_BREAK = SOUNDS.soundEvent("block.leaf_litter.break");
    public static final Supplier<SoundEvent> LEAF_LITTER_STEP = SOUNDS.soundEvent("block.leaf_litter.step");
    public static final Supplier<SoundEvent> LEAF_LITTER_PLACE = SOUNDS.soundEvent("block.leaf_litter.place");
    public static final Supplier<SoundEvent> LEAF_LITTER_HIT = SOUNDS.soundEvent("block.leaf_litter.hit");
    public static final Supplier<SoundEvent> LEAF_LITTER_FALL = SOUNDS.soundEvent("block.leaf_litter.fall");

    public static final Supplier<SoundEvent> CACTUS_FLOWER_BREAK = SOUNDS.soundEvent("block.cactus_flower.break");
    public static final Supplier<SoundEvent> CACTUS_FLOWER_PLACE = SOUNDS.soundEvent("block.cactus_flower.place");

    public static final Supplier<SoundEvent> FIREFLY_BUSH_IDLE = SOUNDS.soundEvent("block.firefly_bush.idle");
    public static final Supplier<SoundEvent> SAND_IDLE = SOUNDS.soundEvent("block.sand.idle");
    public static final Supplier<SoundEvent> DEAD_BUSH_IDLE = SOUNDS.soundEvent("block.deadbush.idle");
    public static final Supplier<SoundEvent> DRY_GRASS = SOUNDS.soundEvent("block.dry_grass.ambient");

    // ITEMS
    public static final Supplier<SoundEvent> BUNDLE_INSERT_FAIL = SOUNDS.soundEvent("item.bundle.insert_fail");

    // ENTITIES
    public static final Supplier<SoundEvent> CREAKING_AMBIENT = SOUNDS.soundEvent("entity.creaking.ambient");
    public static final Supplier<SoundEvent> CREAKING_ACTIVATE = SOUNDS.soundEvent("entity.creaking.activate");
    public static final Supplier<SoundEvent> CREAKING_DEACTIVATE = SOUNDS.soundEvent("entity.creaking.deactivate");
    public static final Supplier<SoundEvent> CREAKING_ATTACK = SOUNDS.soundEvent("entity.creaking.attack");
    public static final Supplier<SoundEvent> CREAKING_DEATH = SOUNDS.soundEvent("entity.creaking.death");
    public static final Supplier<SoundEvent> CREAKING_STEP = SOUNDS.soundEvent("entity.creaking.step");
    public static final Supplier<SoundEvent> CREAKING_FREEZE = SOUNDS.soundEvent("entity.creaking.freeze");
    public static final Supplier<SoundEvent> CREAKING_UNFREEZE = SOUNDS.soundEvent("entity.creaking.unfreeze");
    public static final Supplier<SoundEvent> CREAKING_SPAWN = SOUNDS.soundEvent("entity.creaking.spawn");
    public static final Supplier<SoundEvent> CREAKING_SWAY = SOUNDS.soundEvent("entity.creaking.sway");
    public static final Supplier<SoundEvent> CREAKING_TWITCH = SOUNDS.soundEvent("entity.creaking.twitch");

    public static final Supplier<SoundEvent> GHASTLING_AMBIENT = SOUNDS.soundEvent("entity.ghastling.ambient");
    public static final Supplier<SoundEvent> GHASTLING_DEATH = SOUNDS.soundEvent("entity.ghastling.death");
    public static final Supplier<SoundEvent> GHASTLING_HURT = SOUNDS.soundEvent("entity.ghastling.hurt");
    public static final Supplier<SoundEvent> GHASTLING_SPAWN = SOUNDS.soundEvent("entity.ghastling.spawn");

    public static final Supplier<SoundEvent> HAPPY_GHAST_AMBIENT = SOUNDS.soundEvent("entity.happy_ghast.ambient");
    public static final Supplier<SoundEvent> HAPPY_GHAST_DEATH = SOUNDS.soundEvent("entity.happy_ghast.death");
    public static final Supplier<SoundEvent> HAPPY_GHAST_HURT = SOUNDS.soundEvent("entity.happy_ghast.hurt");
    public static final Supplier<SoundEvent> HAPPY_GHAST_RIDING = SOUNDS.soundEvent("entity.happy_ghast.riding");

    public static final Supplier<SoundEvent> HARNESS_EQUIP = SOUNDS.soundEvent("entity.happy_ghast.equip");
    public static final Supplier<SoundEvent> HARNESS_UNEQUIP = SOUNDS.soundEvent("entity.happy_ghast.unequip");
    public static final Supplier<SoundEvent> HARNESS_GOGGLES_UP = SOUNDS.soundEvent("entity.happy_ghast.harness_goggles_up");
    public static final Supplier<SoundEvent> HARNESS_GOGGLES_DOWN = SOUNDS.soundEvent("entity.happy_ghast.harness_goggles_down");

    public static final Supplier<SoundEvent> PARROT_IMITATE_CREAKING = SOUNDS.soundEvent("entity.parrot.imitate.creaking");

    public static final Supplier<SoundEvent> WOLF_PUGLIN_AMBIENT = SOUNDS.soundEvent("entity.wolf_puglin.ambient");
    public static final Supplier<SoundEvent> WOLF_PUGLIN_DEATH = SOUNDS.soundEvent("entity.wolf_puglin.death");
    public static final Supplier<SoundEvent> WOLF_PUGLIN_GROWL = SOUNDS.soundEvent("entity.wolf_puglin.growl");
    public static final Supplier<SoundEvent> WOLF_PUGLIN_HURT = SOUNDS.soundEvent("entity.wolf_puglin.hurt");
    public static final Supplier<SoundEvent> WOLF_PUGLIN_PANT = SOUNDS.soundEvent("entity.wolf_puglin.pant");
    public static final Supplier<SoundEvent> WOLF_PUGLIN_WHINE = SOUNDS.soundEvent("entity.wolf_puglin.whine");

    public static final Supplier<SoundEvent> WOLF_SAD_AMBIENT = SOUNDS.soundEvent("entity.wolf_sad.ambient");
    public static final Supplier<SoundEvent> WOLF_SAD_DEATH = SOUNDS.soundEvent("entity.wolf_sad.death");
    public static final Supplier<SoundEvent> WOLF_SAD_GROWL = SOUNDS.soundEvent("entity.wolf_sad.growl");
    public static final Supplier<SoundEvent> WOLF_SAD_HURT = SOUNDS.soundEvent("entity.wolf_sad.hurt");
    public static final Supplier<SoundEvent> WOLF_SAD_PANT = SOUNDS.soundEvent("entity.wolf_sad.pant");
    public static final Supplier<SoundEvent> WOLF_SAD_WHINE = SOUNDS.soundEvent("entity.wolf_sad.whine");

    public static final Supplier<SoundEvent> WOLF_ANGRY_AMBIENT = SOUNDS.soundEvent("entity.wolf_angry.ambient");
    public static final Supplier<SoundEvent> WOLF_ANGRY_DEATH = SOUNDS.soundEvent("entity.wolf_angry.death");
    public static final Supplier<SoundEvent> WOLF_ANGRY_GROWL = SOUNDS.soundEvent("entity.wolf_angry.growl");
    public static final Supplier<SoundEvent> WOLF_ANGRY_HURT = SOUNDS.soundEvent("entity.wolf_angry.hurt");
    public static final Supplier<SoundEvent> WOLF_ANGRY_PANT = SOUNDS.soundEvent("entity.wolf_angry.pant");
    public static final Supplier<SoundEvent> WOLF_ANGRY_WHINE = SOUNDS.soundEvent("entity.wolf_angry.whine");

    public static final Supplier<SoundEvent> WOLF_GRUMPY_AMBIENT = SOUNDS.soundEvent("entity.wolf_grumpy.ambient");
    public static final Supplier<SoundEvent> WOLF_GRUMPY_DEATH = SOUNDS.soundEvent("entity.wolf_grumpy.death");
    public static final Supplier<SoundEvent> WOLF_GRUMPY_GROWL = SOUNDS.soundEvent("entity.wolf_grumpy.growl");
    public static final Supplier<SoundEvent> WOLF_GRUMPY_HURT = SOUNDS.soundEvent("entity.wolf_grumpy.hurt");
    public static final Supplier<SoundEvent> WOLF_GRUMPY_PANT = SOUNDS.soundEvent("entity.wolf_grumpy.pant");
    public static final Supplier<SoundEvent> WOLF_GRUMPY_WHINE = SOUNDS.soundEvent("entity.wolf_grumpy.whine");

    public static final Supplier<SoundEvent> WOLF_BIG_AMBIENT = SOUNDS.soundEvent("entity.wolf_big.ambient");
    public static final Supplier<SoundEvent> WOLF_BIG_DEATH = SOUNDS.soundEvent("entity.wolf_big.death");
    public static final Supplier<SoundEvent> WOLF_BIG_GROWL = SOUNDS.soundEvent("entity.wolf_big.growl");
    public static final Supplier<SoundEvent> WOLF_BIG_HURT = SOUNDS.soundEvent("entity.wolf_big.hurt");
    public static final Supplier<SoundEvent> WOLF_BIG_PANT = SOUNDS.soundEvent("entity.wolf_big.pant");
    public static final Supplier<SoundEvent> WOLF_BIG_WHINE = SOUNDS.soundEvent("entity.wolf_big.whine");

    public static final Supplier<SoundEvent> WOLF_CUTE_AMBIENT = SOUNDS.soundEvent("entity.wolf_cute.ambient");
    public static final Supplier<SoundEvent> WOLF_CUTE_DEATH = SOUNDS.soundEvent("entity.wolf_cute.death");
    public static final Supplier<SoundEvent> WOLF_CUTE_GROWL = SOUNDS.soundEvent("entity.wolf_cute.growl");
    public static final Supplier<SoundEvent> WOLF_CUTE_HURT = SOUNDS.soundEvent("entity.wolf_cute.hurt");
    public static final Supplier<SoundEvent> WOLF_CUTE_PANT = SOUNDS.soundEvent("entity.wolf_cute.pant");
    public static final Supplier<SoundEvent> WOLF_CUTE_WHINE = SOUNDS.soundEvent("entity.wolf_cute.whine");

    // RECORDS
    public static final Supplier<SoundEvent> MUSIC_DISC_TEARS = SOUNDS.soundEvent("music_disc.tears");
    public static final Supplier<SoundEvent> MUSIC_DISC_LAVA_CHICKEN = SOUNDS.soundEvent("music_disc.lava_chicken");
}