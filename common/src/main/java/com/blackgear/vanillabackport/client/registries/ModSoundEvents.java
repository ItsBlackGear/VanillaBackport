package com.blackgear.vanillabackport.client.registries;

import com.blackgear.platform.core.helper.SoundRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ModSoundEvents {
    public static final SoundRegistry REGISTRIES = SoundRegistry.create(VanillaBackport.NAMESPACE);

    // BLOCKS
    public static final Supplier<SoundEvent> EYEBLOSSOM_OPEN_LONG = REGISTRIES.soundEvent("block.eyeblossom.open_long");
    public static final Supplier<SoundEvent> EYEBLOSSOM_OPEN = REGISTRIES.soundEvent("block.eyeblossom.open");
    public static final Supplier<SoundEvent> EYEBLOSSOM_CLOSE_LONG = REGISTRIES.soundEvent("block.eyeblossom.close_long");
    public static final Supplier<SoundEvent> EYEBLOSSOM_CLOSE = REGISTRIES.soundEvent("block.eyeblossom.close");
    public static final Supplier<SoundEvent> EYEBLOSSOM_IDLE = REGISTRIES.soundEvent("block.eyeblossom.idle");

    public static final Supplier<SoundEvent> PALE_HANGING_MOSS_IDLE = REGISTRIES.soundEvent("block.pale_hanging_moss.idle");

    public static final Supplier<SoundEvent> CREAKING_HEART_BREAK = REGISTRIES.soundEvent("block.creaking_heart.break");
    public static final Supplier<SoundEvent> CREAKING_HEART_FALL = REGISTRIES.soundEvent("block.creaking_heart.fall");
    public static final Supplier<SoundEvent> CREAKING_HEART_HIT = REGISTRIES.soundEvent("block.creaking_heart.hit");
    public static final Supplier<SoundEvent> CREAKING_HEART_HURT = REGISTRIES.soundEvent("block.creaking_heart.hurt");
    public static final Supplier<SoundEvent> CREAKING_HEART_PLACE = REGISTRIES.soundEvent("block.creaking_heart.place");
    public static final Supplier<SoundEvent> CREAKING_HEART_STEP = REGISTRIES.soundEvent("block.creaking_heart.step");
    public static final Supplier<SoundEvent> CREAKING_HEART_IDLE = REGISTRIES.soundEvent("block.creaking_heart.idle");
    public static final Supplier<SoundEvent> CREAKING_HEART_SPAWN = REGISTRIES.soundEvent("block.creaking_heart.spawn");

    public static final Supplier<SoundEvent> RESIN_BREAK = REGISTRIES.soundEvent("block.resin.break");
    public static final Supplier<SoundEvent> RESIN_FALL = REGISTRIES.soundEvent("block.resin.fall");
    public static final Supplier<SoundEvent> RESIN_PLACE = REGISTRIES.soundEvent("block.resin.place");
    public static final Supplier<SoundEvent> RESIN_STEP = REGISTRIES.soundEvent("block.resin.step");

    public static final Supplier<SoundEvent> RESIN_BRICKS_BREAK = REGISTRIES.soundEvent("block.resin_bricks.break");
    public static final Supplier<SoundEvent> RESIN_BRICKS_FALL = REGISTRIES.soundEvent("block.resin_bricks.fall");
    public static final Supplier<SoundEvent> RESIN_BRICKS_HIT = REGISTRIES.soundEvent("block.resin_bricks.hit");
    public static final Supplier<SoundEvent> RESIN_BRICKS_PLACE = REGISTRIES.soundEvent("block.resin_bricks.place");
    public static final Supplier<SoundEvent> RESIN_BRICKS_STEP = REGISTRIES.soundEvent("block.resin_bricks.step");

    public static final Supplier<SoundEvent> DRIED_GHAST_BREAK = REGISTRIES.soundEvent("block.dried_ghast.break");
    public static final Supplier<SoundEvent> DRIED_GHAST_STEP = REGISTRIES.soundEvent("block.dried_ghast.step");
    public static final Supplier<SoundEvent> DRIED_GHAST_FALL = REGISTRIES.soundEvent("block.dried_ghast.fall");
    public static final Supplier<SoundEvent> DRIED_GHAST_AMBIENT = REGISTRIES.soundEvent("block.dried_ghast.ambient");
    public static final Supplier<SoundEvent> DRIED_GHAST_AMBIENT_WATER = REGISTRIES.soundEvent("block.dried_ghast.ambient_water");
    public static final Supplier<SoundEvent> DRIED_GHAST_PLACE = REGISTRIES.soundEvent("block.dried_ghast.place");
    public static final Supplier<SoundEvent> DRIED_GHAST_PLACE_IN_WATER = REGISTRIES.soundEvent("block.dried_ghast.place_in_water");
    public static final Supplier<SoundEvent> DRIED_GHAST_TRANSITION = REGISTRIES.soundEvent("block.dried_ghast.transition");

    public static final Supplier<SoundEvent> LEAF_LITTER_BREAK = REGISTRIES.soundEvent("block.leaf_litter.break");
    public static final Supplier<SoundEvent> LEAF_LITTER_STEP = REGISTRIES.soundEvent("block.leaf_litter.step");
    public static final Supplier<SoundEvent> LEAF_LITTER_PLACE = REGISTRIES.soundEvent("block.leaf_litter.place");
    public static final Supplier<SoundEvent> LEAF_LITTER_HIT = REGISTRIES.soundEvent("block.leaf_litter.hit");
    public static final Supplier<SoundEvent> LEAF_LITTER_FALL = REGISTRIES.soundEvent("block.leaf_litter.fall");

    public static final Supplier<SoundEvent> CACTUS_FLOWER_BREAK = REGISTRIES.soundEvent("block.cactus_flower.break");
    public static final Supplier<SoundEvent> CACTUS_FLOWER_PLACE = REGISTRIES.soundEvent("block.cactus_flower.place");

    public static final Supplier<SoundEvent> FIREFLY_BUSH_IDLE = REGISTRIES.soundEvent("block.firefly_bush.idle");
    public static final Supplier<SoundEvent> SAND_IDLE = REGISTRIES.soundEvent("block.sand.idle");
    public static final Supplier<SoundEvent> DEAD_BUSH_IDLE = REGISTRIES.soundEvent("block.deadbush.idle");
    public static final Supplier<SoundEvent> DRY_GRASS = REGISTRIES.soundEvent("block.dry_grass.ambient");

    public static final Supplier<SoundEvent> DECORATED_POT_INSERT = REGISTRIES.soundEvent("block.decorated_pot.insert");
    public static final Supplier<SoundEvent> DECORATED_POT_INSERT_FAIL = REGISTRIES.soundEvent("block.decorated_pot.insert_fail");

    public static final Supplier<SoundEvent> SULFUR_BREAK = REGISTRIES.soundEvent("block.sulfur.break");
    public static final Supplier<SoundEvent> SULFUR_STEP = REGISTRIES.soundEvent("block.sulfur.step");
    public static final Supplier<SoundEvent> SULFUR_PLACE = REGISTRIES.soundEvent("block.sulfur.place");
    public static final Supplier<SoundEvent> SULFUR_HIT = REGISTRIES.soundEvent("block.sulfur.hit");
    public static final Supplier<SoundEvent> SULFUR_FALL = REGISTRIES.soundEvent("block.sulfur.fall");

    public static final Supplier<SoundEvent> POTENT_SULFUR_BREAK = REGISTRIES.soundEvent("block.potent_sulfur.break");
    public static final Supplier<SoundEvent> POTENT_SULFUR_STEP = REGISTRIES.soundEvent("block.potent_sulfur.step");
    public static final Supplier<SoundEvent> POTENT_SULFUR_PLACE = REGISTRIES.soundEvent("block.potent_sulfur.place");
    public static final Supplier<SoundEvent> POTENT_SULFUR_HIT = REGISTRIES.soundEvent("block.potent_sulfur.hit");
    public static final Supplier<SoundEvent> POTENT_SULFUR_FALL = REGISTRIES.soundEvent("block.potent_sulfur.fall");

    public static final Supplier<SoundEvent> CINNABAR_BREAK = REGISTRIES.soundEvent("block.cinnabar.break");
    public static final Supplier<SoundEvent> CINNABAR_STEP = REGISTRIES.soundEvent("block.cinnabar.step");
    public static final Supplier<SoundEvent> CINNABAR_PLACE = REGISTRIES.soundEvent("block.cinnabar.place");
    public static final Supplier<SoundEvent> CINNABAR_HIT = REGISTRIES.soundEvent("block.cinnabar.hit");
    public static final Supplier<SoundEvent> CINNABAR_FALL = REGISTRIES.soundEvent("block.cinnabar.fall");

    public static final Supplier<SoundEvent> NOXIOUS_GAS = REGISTRIES.soundEvent("block.potent_sulfur.noxious_gas");
    public static final Supplier<SoundEvent> GEYSER_ERUPTION_START = REGISTRIES.soundEvent("block.potent_sulfur.geyser_eruption");
    public static final Supplier<SoundEvent> GEYSER_ERUPTION_ACTIVE = REGISTRIES.soundEvent("block.potent_sulfur.geyser_eruption_active");
    public static final Supplier<SoundEvent> GEYSER_CONTINUOUS_START = REGISTRIES.soundEvent("block.potent_sulfur.geyser_continuous_eruption_start");
    public static final Supplier<SoundEvent> GEYSER_CONTINUOUS_ACTIVE = REGISTRIES.soundEvent("block.potent_sulfur.geyser_continuous_eruption_active");

    // ITEMS
    public static final Supplier<SoundEvent> BUNDLE_INSERT_FAIL = REGISTRIES.soundEvent("item.bundle.insert_fail");
    public static final Supplier<SoundEvent> ARMOR_EQUIP_WOLF = REGISTRIES.soundEvent("item.armor.equip_wolf");
    public static final Supplier<SoundEvent> ARMOR_UNEQUIP_WOLF = REGISTRIES.soundEvent("item.armor.unequip_wolf");
    public static final Supplier<SoundEvent> WOLF_ARMOR_BREAK = REGISTRIES.soundEvent("item.wolf_armor.break");
    public static final Supplier<SoundEvent> WOLF_ARMOR_CRACK = REGISTRIES.soundEvent("item.wolf_armor.crack");
    public static final Supplier<SoundEvent> WOLF_ARMOR_DAMAGE = REGISTRIES.soundEvent("item.wolf_armor.damage");
    public static final Supplier<SoundEvent> WOLF_ARMOR_REPAIR = REGISTRIES.soundEvent("item.wolf_armor.repair");

    public static final Supplier<SoundEvent> OMINOUS_BOTTLE_DISPOSE = REGISTRIES.soundEvent("item.ominous_bottle.dispose");

    public static final Supplier<SoundEvent> BUCKET_EMPTY_SULFUR_CUBE = REGISTRIES.soundEvent("item.bucket.empty_sulfur_cube");
    public static final Supplier<SoundEvent> BUCKET_FILL_SULFUR_CUBE = REGISTRIES.soundEvent("item.bucket.fill_sulfur_cube");

    // ENTITIES
    public static final Supplier<SoundEvent> ARMADILLO_EAT = REGISTRIES.soundEvent("entity.armadillo.eat");
    public static final Supplier<SoundEvent> ARMADILLO_HURT = REGISTRIES.soundEvent("entity.armadillo.hurt");
    public static final Supplier<SoundEvent> ARMADILLO_HURT_REDUCED = REGISTRIES.soundEvent("entity.armadillo.hurt_reduced");
    public static final Supplier<SoundEvent> ARMADILLO_AMBIENT = REGISTRIES.soundEvent("entity.armadillo.ambient");
    public static final Supplier<SoundEvent> ARMADILLO_STEP = REGISTRIES.soundEvent("entity.armadillo.step");
    public static final Supplier<SoundEvent> ARMADILLO_DEATH = REGISTRIES.soundEvent("entity.armadillo.death");
    public static final Supplier<SoundEvent> ARMADILLO_ROLL = REGISTRIES.soundEvent("entity.armadillo.roll");
    public static final Supplier<SoundEvent> ARMADILLO_LAND = REGISTRIES.soundEvent("entity.armadillo.land");
    public static final Supplier<SoundEvent> ARMADILLO_SCUTE_DROP = REGISTRIES.soundEvent("entity.armadillo.scute_drop");
    public static final Supplier<SoundEvent> ARMADILLO_UNROLL_FINISH = REGISTRIES.soundEvent("entity.armadillo.unroll_finish");
    public static final Supplier<SoundEvent> ARMADILLO_PEEK = REGISTRIES.soundEvent("entity.armadillo.peek");
    public static final Supplier<SoundEvent> ARMADILLO_UNROLL_START = REGISTRIES.soundEvent("entity.armadillo.unroll_start");
    public static final Supplier<SoundEvent> ARMADILLO_BRUSH = REGISTRIES.soundEvent("entity.armadillo.brush");

    public static final Supplier<SoundEvent> CREAKING_AMBIENT = REGISTRIES.soundEvent("entity.creaking.ambient");
    public static final Supplier<SoundEvent> CREAKING_ACTIVATE = REGISTRIES.soundEvent("entity.creaking.activate");
    public static final Supplier<SoundEvent> CREAKING_DEACTIVATE = REGISTRIES.soundEvent("entity.creaking.deactivate");
    public static final Supplier<SoundEvent> CREAKING_ATTACK = REGISTRIES.soundEvent("entity.creaking.attack");
    public static final Supplier<SoundEvent> CREAKING_DEATH = REGISTRIES.soundEvent("entity.creaking.death");
    public static final Supplier<SoundEvent> CREAKING_STEP = REGISTRIES.soundEvent("entity.creaking.step");
    public static final Supplier<SoundEvent> CREAKING_FREEZE = REGISTRIES.soundEvent("entity.creaking.freeze");
    public static final Supplier<SoundEvent> CREAKING_UNFREEZE = REGISTRIES.soundEvent("entity.creaking.unfreeze");
    public static final Supplier<SoundEvent> CREAKING_SPAWN = REGISTRIES.soundEvent("entity.creaking.spawn");
    public static final Supplier<SoundEvent> CREAKING_SWAY = REGISTRIES.soundEvent("entity.creaking.sway");
    public static final Supplier<SoundEvent> CREAKING_TWITCH = REGISTRIES.soundEvent("entity.creaking.twitch");

    public static final Supplier<SoundEvent> GHASTLING_AMBIENT = REGISTRIES.soundEvent("entity.ghastling.ambient");
    public static final Supplier<SoundEvent> GHASTLING_DEATH = REGISTRIES.soundEvent("entity.ghastling.death");
    public static final Supplier<SoundEvent> GHASTLING_HURT = REGISTRIES.soundEvent("entity.ghastling.hurt");
    public static final Supplier<SoundEvent> GHASTLING_SPAWN = REGISTRIES.soundEvent("entity.ghastling.spawn");

    public static final Supplier<SoundEvent> HAPPY_GHAST_AMBIENT = REGISTRIES.soundEvent("entity.happy_ghast.ambient");
    public static final Supplier<SoundEvent> HAPPY_GHAST_DEATH = REGISTRIES.soundEvent("entity.happy_ghast.death");
    public static final Supplier<SoundEvent> HAPPY_GHAST_HURT = REGISTRIES.soundEvent("entity.happy_ghast.hurt");
    public static final Supplier<SoundEvent> HAPPY_GHAST_RIDING = REGISTRIES.soundEvent("entity.happy_ghast.riding");

    public static final Supplier<SoundEvent> HARNESS_EQUIP = REGISTRIES.soundEvent("entity.happy_ghast.equip");
    public static final Supplier<SoundEvent> HARNESS_UNEQUIP = REGISTRIES.soundEvent("entity.happy_ghast.unequip");
    public static final Supplier<SoundEvent> HARNESS_GOGGLES_UP = REGISTRIES.soundEvent("entity.happy_ghast.harness_goggles_up");
    public static final Supplier<SoundEvent> HARNESS_GOGGLES_DOWN = REGISTRIES.soundEvent("entity.happy_ghast.harness_goggles_down");

    public static final Supplier<SoundEvent> PARROT_IMITATE_CREAKING = REGISTRIES.soundEvent("entity.parrot.imitate.creaking");

    public static final Supplier<SoundEvent> WOLF_PUGLIN_AMBIENT = REGISTRIES.soundEvent("entity.wolf_puglin.ambient");
    public static final Supplier<SoundEvent> WOLF_PUGLIN_DEATH = REGISTRIES.soundEvent("entity.wolf_puglin.death");
    public static final Supplier<SoundEvent> WOLF_PUGLIN_GROWL = REGISTRIES.soundEvent("entity.wolf_puglin.growl");
    public static final Supplier<SoundEvent> WOLF_PUGLIN_HURT = REGISTRIES.soundEvent("entity.wolf_puglin.hurt");
    public static final Supplier<SoundEvent> WOLF_PUGLIN_PANT = REGISTRIES.soundEvent("entity.wolf_puglin.pant");
    public static final Supplier<SoundEvent> WOLF_PUGLIN_WHINE = REGISTRIES.soundEvent("entity.wolf_puglin.whine");

    public static final Supplier<SoundEvent> WOLF_SAD_AMBIENT = REGISTRIES.soundEvent("entity.wolf_sad.ambient");
    public static final Supplier<SoundEvent> WOLF_SAD_DEATH = REGISTRIES.soundEvent("entity.wolf_sad.death");
    public static final Supplier<SoundEvent> WOLF_SAD_GROWL = REGISTRIES.soundEvent("entity.wolf_sad.growl");
    public static final Supplier<SoundEvent> WOLF_SAD_HURT = REGISTRIES.soundEvent("entity.wolf_sad.hurt");
    public static final Supplier<SoundEvent> WOLF_SAD_PANT = REGISTRIES.soundEvent("entity.wolf_sad.pant");
    public static final Supplier<SoundEvent> WOLF_SAD_WHINE = REGISTRIES.soundEvent("entity.wolf_sad.whine");

    public static final Supplier<SoundEvent> WOLF_ANGRY_AMBIENT = REGISTRIES.soundEvent("entity.wolf_angry.ambient");
    public static final Supplier<SoundEvent> WOLF_ANGRY_DEATH = REGISTRIES.soundEvent("entity.wolf_angry.death");
    public static final Supplier<SoundEvent> WOLF_ANGRY_GROWL = REGISTRIES.soundEvent("entity.wolf_angry.growl");
    public static final Supplier<SoundEvent> WOLF_ANGRY_HURT = REGISTRIES.soundEvent("entity.wolf_angry.hurt");
    public static final Supplier<SoundEvent> WOLF_ANGRY_PANT = REGISTRIES.soundEvent("entity.wolf_angry.pant");
    public static final Supplier<SoundEvent> WOLF_ANGRY_WHINE = REGISTRIES.soundEvent("entity.wolf_angry.whine");

    public static final Supplier<SoundEvent> WOLF_GRUMPY_AMBIENT = REGISTRIES.soundEvent("entity.wolf_grumpy.ambient");
    public static final Supplier<SoundEvent> WOLF_GRUMPY_DEATH = REGISTRIES.soundEvent("entity.wolf_grumpy.death");
    public static final Supplier<SoundEvent> WOLF_GRUMPY_GROWL = REGISTRIES.soundEvent("entity.wolf_grumpy.growl");
    public static final Supplier<SoundEvent> WOLF_GRUMPY_HURT = REGISTRIES.soundEvent("entity.wolf_grumpy.hurt");
    public static final Supplier<SoundEvent> WOLF_GRUMPY_PANT = REGISTRIES.soundEvent("entity.wolf_grumpy.pant");
    public static final Supplier<SoundEvent> WOLF_GRUMPY_WHINE = REGISTRIES.soundEvent("entity.wolf_grumpy.whine");

    public static final Supplier<SoundEvent> WOLF_BIG_AMBIENT = REGISTRIES.soundEvent("entity.wolf_big.ambient");
    public static final Supplier<SoundEvent> WOLF_BIG_DEATH = REGISTRIES.soundEvent("entity.wolf_big.death");
    public static final Supplier<SoundEvent> WOLF_BIG_GROWL = REGISTRIES.soundEvent("entity.wolf_big.growl");
    public static final Supplier<SoundEvent> WOLF_BIG_HURT = REGISTRIES.soundEvent("entity.wolf_big.hurt");
    public static final Supplier<SoundEvent> WOLF_BIG_PANT = REGISTRIES.soundEvent("entity.wolf_big.pant");
    public static final Supplier<SoundEvent> WOLF_BIG_WHINE = REGISTRIES.soundEvent("entity.wolf_big.whine");

    public static final Supplier<SoundEvent> WOLF_CUTE_AMBIENT = REGISTRIES.soundEvent("entity.wolf_cute.ambient");
    public static final Supplier<SoundEvent> WOLF_CUTE_DEATH = REGISTRIES.soundEvent("entity.wolf_cute.death");
    public static final Supplier<SoundEvent> WOLF_CUTE_GROWL = REGISTRIES.soundEvent("entity.wolf_cute.growl");
    public static final Supplier<SoundEvent> WOLF_CUTE_HURT = REGISTRIES.soundEvent("entity.wolf_cute.hurt");
    public static final Supplier<SoundEvent> WOLF_CUTE_PANT = REGISTRIES.soundEvent("entity.wolf_cute.pant");
    public static final Supplier<SoundEvent> WOLF_CUTE_WHINE = REGISTRIES.soundEvent("entity.wolf_cute.whine");

    public static final Supplier<SoundEvent> SULFUR_CUBE_ABSORB = REGISTRIES.soundEvent("entity.sulfur_cube.absorb");
    public static final Supplier<SoundEvent> SULFUR_CUBE_BOUNCE = REGISTRIES.soundEvent("entity.sulfur_cube.bounce");
    public static final Supplier<SoundEvent> SULFUR_CUBE_DEATH = REGISTRIES.soundEvent("entity.sulfur_cube.death");
    public static final Supplier<SoundEvent> SULFUR_CUBE_EJECT = REGISTRIES.soundEvent("entity.sulfur_cube.eject");
    public static final Supplier<SoundEvent> SULFUR_CUBE_HURT = REGISTRIES.soundEvent("entity.sulfur_cube.hurt");
    public static final Supplier<SoundEvent> SULFUR_CUBE_JUMP = REGISTRIES.soundEvent("entity.sulfur_cube.jump");

    public static final Holder<SoundEvent> SULFUR_CUBE_REGULAR_HIT = registerForHolder("entity.sulfur_cube.regular.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_REGULAR_PUSH = registerForHolder("entity.sulfur_cube.regular.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_BOUNCY_HIT = registerForHolder("entity.sulfur_cube.bouncy.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_BOUNCY_PUSH = registerForHolder("entity.sulfur_cube.bouncy.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_BOUNCY_HIT = registerForHolder("entity.sulfur_cube.slow_bouncy.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_BOUNCY_PUSH = registerForHolder("entity.sulfur_cube.slow_bouncy.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_FLAT_HIT = registerForHolder("entity.sulfur_cube.slow_flat.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_FLAT_PUSH = registerForHolder("entity.sulfur_cube.slow_flat.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_FAST_FLAT_HIT = registerForHolder("entity.sulfur_cube.fast_flat.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_FAST_FLAT_PUSH = registerForHolder("entity.sulfur_cube.fast_flat.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_LIGHT_HIT = registerForHolder("entity.sulfur_cube.light.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_LIGHT_PUSH = registerForHolder("entity.sulfur_cube.light.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_FAST_SLIDING_HIT = registerForHolder("entity.sulfur_cube.fast_sliding.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_FAST_SLIDING_PUSH = registerForHolder("entity.sulfur_cube.fast_sliding.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_SLIDING_HIT = registerForHolder("entity.sulfur_cube.slow_sliding.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_SLOW_SLIDING_PUSH = registerForHolder("entity.sulfur_cube.slow_sliding.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_STICKY_HIT = registerForHolder("entity.sulfur_cube.sticky.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_STICKY_PUSH = registerForHolder("entity.sulfur_cube.sticky.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_HIGH_RESISTANCE_HIT = registerForHolder("entity.sulfur_cube.high_resistance.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_HIGH_RESISTANCE_PUSH = registerForHolder("entity.sulfur_cube.high_resistance.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_EXPLOSIVE_HIT = registerForHolder("entity.sulfur_cube.explosive.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_EXPLOSIVE_PUSH = registerForHolder("entity.sulfur_cube.explosive.push");
    public static final Holder<SoundEvent> SULFUR_CUBE_HOT_HIT = registerForHolder("entity.sulfur_cube.hot.hit");
    public static final Holder<SoundEvent> SULFUR_CUBE_HOT_PUSH = registerForHolder("entity.sulfur_cube.hot.push");

    public static final Supplier<SoundEvent> SULFUR_CUBE_SQUISH = REGISTRIES.soundEvent("entity.sulfur_cube.squish");

    public static final Supplier<SoundEvent> SULFUR_CUBE_SMALL_DEATH = REGISTRIES.soundEvent("entity.small_sulfur_cube.death");
    public static final Supplier<SoundEvent> SULFUR_CUBE_SMALL_HURT = REGISTRIES.soundEvent("entity.small_sulfur_cube.hurt");
    public static final Supplier<SoundEvent> SULFUR_CUBE_SMALL_JUMP = REGISTRIES.soundEvent("entity.small_sulfur_cube.jump");
    public static final Supplier<SoundEvent> SULFUR_CUBE_SMALL_SQUISH = REGISTRIES.soundEvent("entity.small_sulfur_cube.squish");
    public static final Supplier<SoundEvent> SULFUR_CUBE_SMALL_EAT = REGISTRIES.soundEvent("entity.small_sulfur_cube.eat");

    public static final Supplier<SoundEvent> MUSIC_BIOME_SULFUR_CAVES = REGISTRIES.soundEvent("music.overworld.sulfur_caves");

    // RECORDS
    public static final Supplier<SoundEvent> MUSIC_DISC_TEARS = REGISTRIES.soundEvent("music_disc.tears");
    public static final Supplier<SoundEvent> MUSIC_DISC_LAVA_CHICKEN = REGISTRIES.soundEvent("music_disc.lava_chicken");
    public static final Supplier<SoundEvent> MUSIC_DISC_BOUNCE = REGISTRIES.soundEvent("music_disc.bounce");

    private static Holder<SoundEvent> registerForHolder(String id) {
        return REGISTRIES.registry().holder(id, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(id)));
    }
}