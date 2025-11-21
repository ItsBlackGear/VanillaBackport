<!-- Top banner -->
<p style="text-align:center; margin: 0 0 8px;">
  <img src="https://i.imgur.com/GZwoar7.png" alt="Header Image">
</p>

<!-- Titles -->
<h1 style="text-align:center; margin: 4px 0 12px;">
  <strong>Vanilla Backport: Bundle o' Life</strong>
</h1>

<!-- Divider -->
<p style="text-align:center; margin: 4px 0 4px;">
  <img src="https://i.imgur.com/6H8YGoX.png" alt="Divider">
</p>

It's finally time! releasing the biggest update of Vanilla Backport to the date and the result of 4 months of development, finally introducing... **Bundle o' Life**!

This update brings features of not one, but multiple updates!
- Spring to Life
- Bundles of Bravery
- Armored Paws
- Bats & Pots

## FEATURES
- World generation & ambience:
    - added **leaf litter**:
        - toggleable via config.
        - generates in biomes that use the biome tag `#minecraft:spawns_leaf_litter`
        - they generate under trees that use the block tag `#minecraft:allows_leaf_litter`
        - sparse leaf litter patches generate on biomes that use the tag `#minecraft:spawns_leaf_litter_patches`
    - added **wild flowers**:
        - toggleable via config.
        - generates in biomes that use the biome tag `#minecraft:spawns_wildflowers`
        - noise-based wild flowers generate in biomes that use the biome tag `#minecraft:spawns_noise_based_wildflowers`
    - added **bushes**:
        - toggleable via config.
        - generates in biomes that use the biome tag `#minecraft:spawns_bushes`
    - added **firefly bushes**:
        - toggleable via config.
        - generates in biomes that use the biome tag `#minecraft:spawns_firefly_bushes`
        - they may generate in land in biomes that use the biome tag `#minecraft:spawns_firefly_bushes_swamp`
    - added **cactus flower**.
    - added **short and tall dry grass**:
        - toggleable via config.
        - generates in biomes that use the biome tags `#minecraft:spawns_dry_grass` and `#minecraft:spawns_dry_grass_rarely`
    - added **farm animal variants**.
    - added **falling leaves**.
    - added **fallen trees**:
        - toggleable via config.
        - generates in the biomes that use the following biome tags:
            - `#minecraft:spawns_fallen_oak_trees`
            - `#minecraft:spawns_fallen_birch_trees`
            - `#minecraft:spawns_fallen_birch_trees_rarely`
            - `#minecraft:spawns_fallen_super_birch_trees`
            - `#minecraft:spawns_fallen_jungle_trees`
            - `#minecraft:spawns_fallen_spruce_trees`
            - `#minecraft:spawns_fallen_spruce_trees_rarely`
    - added desert ambient sounds:
        - Dead Bushes, Dry Grass, Sand and Red Sand now have a chance to play ambient sounds.
    - updated lodestone recipe.
    - updated Bundle UI and controls.
        - toggleable via config.
        - bundles now change their model depending on whether you are using legacy or modern bundles.
    - added **dyable bundles**.
    - reworked creative tabs
    - added support to conventional tags (`Forge` / `Fabric` / `Create`).
    - added background music by **Amos Roddy**:
        - *"Below and Above"*
        - *"Broken Clocks"*
        - *"Fireflies"*
        - *"Lilypad"*
        - *"O's Piano"*
    - sheep now have color priorities based on their spawning biome.
    - updated camel spawning:
        - toggleable via config.
        - they can generate in biomes that use the biome tag `#minecraft:spawns_camels`
    - added **wolf variants**
    - added **armadillos**
        - toggleable via config.
        - they spawn in biomes that use the biome tag `#minecraft:spawns_armadillos`
        - spiders are now scared of armadillos.
    - decorative pots can now store items. (thanks to **Echo2Craft**)

## CHANGES
- namespace update
- creative tab rework
- updated configs:
    - updated bat model is now toggleable.
    - removed creaking heart on pale oak sapling config.
    - resin config now controls its generation on woodland mansions loot.
    - dried ghast config now controls whether dried ghasts are bartered by piglins.
    - music disc drops from ghasts and chicken jockeys are now toggleable.
    - villager's and wandering trader's trades are now toggleable.
    - happy ghast movement speed can now be modified.
    - music fading for pale garden is now toggleable.
    - creaking trail particle colors are now configurable.
- eyeblossoms and firefly bushes are now emissive by default.


## BUGFIXES
- Happy ghasts no longer consume saddles.
- Creakings no longer die from colliding with players for too long.
- Creakings wouldn't play their attack sound.
- Creakings wouldn't spawn properly.
- Parrot sounds were incorrectly referenced in crash logs for Sinytra Connector.
- Configs were not properly generating.


## NAMESPACE UPDATE
- migrated all features from `vanillabackport` namespace to `minecraft`.
    - Allows safe port-forwarding to vanilla versions and the usage of resourcepacks
- features data conversion:
    - this system may be removed in a future update.
    - allows to safely migrate worlds that used the old `vanillabackport` namespace.
    - includes support for `Every Compat` features


## CREATIVE TAB REWORK (BUNDLED TABS)
- merged previous tab from vanilla backport onto a single creative tab.
- added a vertical menu to switch between contents of versions.
- titles are fully translatable.


## FALLING LEAVES
- fully client sided and configurable.
    - toggleable via config.
    - can modify their frequency via config.
- block-tag based:
    - `spawn_falling_leaves` allows to control which leaves spawn falling particles.
    - `spawn_falling_needles` allows to control which leaves spawn coniferous falling particles.
- resource-pack API:
    - resource packs can override leaf colors via JSON files under `assets/<your-id>/leaf_colors/`
    - ```json
      {
        "block": "minecraft:azalea_leaves",
        "properties": {
          "color": -9399763,
          "priority": 0
        }
      }
      ```
    - **block**: ID of a block that is an instance of `LeavesBlock`.
    - **properties**:
        - **color**: ARGB color value for the particle.
        - **priority** (optional): highest priority override wins (default: `0`).


## FARM ANIMAL VARIANTS
- toggleable via config.
- provided new variants for cows, pigs and chickens:
    - `temperate` (default)
    - `cold`
    - `warm`
- they spawn in biomes that use the following biome tags:
    - `#minecraft:spawn_cold_variant_farm_animals`
    - `#minecraft:spawn_warm_variant_farm_animals`
- these variants do not actually spawn the mob, they only determine the variant from the biome it is spawning in.
- added colored eggs for chicken variants:
    - **Blue Eggs**: dropped by cold chickens.
    - **Brown Eggs**: dropped by warm chickens.
- disabled by default when using `mixed litter` (for safer compatibility).
- data-driven:
    - resource packs can add more variants via JSON files under `data/<your-id>/<mob>_variant/`
    - example: **data/minecraft/chicken_variant/cold.json**
    - ```json
      {
        "asset_id": "minecraft:entity/chicken/cold_chicken",
        "model": "cold",
        "spawn_conditions": [
          {
            "condition": {
              "type": "biome",
              "biomes": "#minecraft:spawn_cold_variant_farm_animals"
            },
            "priority": 0
          }
        ]
      }
      ```
    - **asset_id**: represents the location of the entity texture.
    - **model**: variant model.
    - **spawn conditions**
        - **condition** : defines the condition to spawn this variant.
            - **type**: the type of condition to be used
                - **biome**: takes a biome or biome tag to check if the mob is spawning in it.
                    - requires `biome` field.
                - **structure**: takes a structure ID to check if the mob is spawning inside it.
                    - requires `structures` field.
                - **moon_brightness**: takes a range of moon brightness values to check if the mob is spawning during that moon phase.
                    - requires `range` array with `min` and `max` values.
    - for more context, check minecraft official documentation.

## WOLF SOUND VARIANTS
- toggleable via config.
- provided new variants for wolf personalities:
    - `classic` (default)
    - `puglin`
    - `sad`
    - `angry`
    - `grumpy`
    - `big`
    - `cute`
- they are randomly assigned on spawn.
- data-driven:
    - resource packs can add more variants via JSON files under `data/<your-id>/wolf_sound_variant/`
    - example: **data/minecraft/wolf_sound_variant/puglin.json**
    - ```json
      {
        "ambient_sound": "minecraft:entity.wolf_puglin.ambient",
        "death_sound": "minecraft:entity.wolf_puglin.death",
        "growl_sound": "minecraft:entity.wolf_puglin.growl",
        "hurt_sound": "minecraft:entity.wolf_puglin.hurt",
        "pant_sound": "minecraft:entity.wolf_puglin.pant",
        "whine_sound": "minecraft:entity.wolf_puglin.whine"
      }
      ```
    - each field represents the sound id to be used for that specific action.
    - for more context, check minecraft official documentation.

## WOLF VARIANTS
- toggleable via config.
- provided new variants for wolves:
    - `pale` (default)
    - `spotted`
    - `snowy`
    - `black`
    - `ashen`
    - `rusty`
    - `woods`
    - `chestnut`
    - `striped`
- they spawn by default in biomes that use the following biome tags:
    - `#minecraft:has_wolf/spotted`
    - `#minecraft:has_wolf/snowy`
    - `#minecraft:has_wolf/black`
    - `#minecraft:has_wolf/ashen`
    - `#minecraft:has_wolf/rusty`
    - `#minecraft:has_wolf/woods`
    - `#minecraft:has_wolf/chestnut`
    - `#minecraft:has_wolf/striped`
- these variants do not actually spawn the mob, they only determine the variant from the biome it is spawning in.
- disabled by default when using `backported wolves` (for safer compatibility).
- data-driven:
    - resource packs can add more variants via JSON files under `data/<your-id>/wolf_variant/`
    - example: **data/minecraft/chicken_variant/cold.json**
    - ```json
      {
        "assets": {
          "angry": "minecraft:entity/wolf/wolf_ashen_angry",
          "tame": "minecraft:entity/wolf/wolf_ashen_tame",
          "wild": "minecraft:entity/wolf/wolf_ashen"
        },
        "spawn_conditions": [
          {
            "condition": {
              "type": "biome",
              "biomes": "#minecraft:has_wolf/ashen"
            },
            "priority": 0
          }
        ]
      }
      ```
    - **assets**
        - **angry**: represents the location of the angry wolf texture.
        - **tame**: represents the location of the tame wolf texture.
        - **wild**: represents the location of the wild wolf texture.
    - **spawn conditions**
        - **condition** : defines the condition to spawn this variant.
            - **type**: the type of condition to be used
                - **biome**: takes a biome or biome tag to check if the mob is spawning in it.
                    - requires `biome` field.
                - **structure**: takes a structure ID to check if the mob is spawning inside it.
                    - requires `structures` field.
                - **moon_brightness**: takes a range of moon brightness values to check if the mob is spawning during that moon phase.
                    - requires `range` array with `min` and `max` values.
    - for more context, check minecraft official documentation.

<!-- Divider -->
<p style="text-align:center; margin: 12px 0 0;">
  <img src="https://i.imgur.com/6H8YGoX.png" alt="Divider">
</p>

<!-- Disclaimer -->
<h2 style="margin: 12px 0 0;">
  <strong>⚠️ Disclaimer</strong>
</h2>

<div style="text-align:justify; margin: 4px 0 10px;">
  This mod is in no way affiliated with or endorsed by Mojang, Microsoft, or any of their subsidiaries. <br>   
  Minecraft is a trademark of Mojang Studios. All related assets, including textures, names, and game content, are the property of Mojang Studios and Microsoft.
</div>

<br>

<p style="text-align:justify; margin: 4px 0 10px;">
  No commercial use is intended or permitted.
</p>

<!-- Divider -->
<p style="text-align:center; margin: 12px 0 0;">
  <img src="https://i.imgur.com/6H8YGoX.png" alt="Divider">
</p>