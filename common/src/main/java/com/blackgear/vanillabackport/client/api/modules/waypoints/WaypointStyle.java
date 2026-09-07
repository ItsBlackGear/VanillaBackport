package com.blackgear.vanillabackport.client.api.modules.waypoints;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;

import java.util.List;

@Environment(EnvType.CLIENT)
public record WaypointStyle(int nearDistance, int farDistance, List<ResourceLocation> sprites, List<ResourceLocation> spriteLocations) {
    private static final Codec<Integer> DISTANCE_CODEC = Codec.intRange(0, 60000000);
    public static final Codec<WaypointStyle> CODEC = RecordCodecBuilder.<WaypointStyle>create(instance -> instance.group(
        DISTANCE_CODEC.optionalFieldOf("near_distance", 128).forGetter(WaypointStyle::nearDistance),
        DISTANCE_CODEC.optionalFieldOf("far_distance", 332).forGetter(WaypointStyle::farDistance),
        ExtraCodecs.nonEmptyList(ResourceLocation.CODEC.listOf()).fieldOf("sprites").forGetter(WaypointStyle::sprites)
    ).apply(instance, WaypointStyle::new)).validate(WaypointStyle::validate);
    
    
    public WaypointStyle(int nearDistance, int farDistance, List<ResourceLocation> sprites) {
        this(nearDistance, farDistance, sprites, sprites.stream().map(sprite -> sprite.withPrefix("textures/gui/sprites/hud/locator_bar_dot/").withSuffix(".png")).toList());
    }
    
    public DataResult<WaypointStyle> validate() {
        if (this.sprites.isEmpty()) {
            return DataResult.error(() -> "Must have at least one sprite icon");
        } else if (this.nearDistance <= 0) {
            return DataResult.error(() -> "Near distance (" + this.nearDistance + ") must be greater than zero");
        } else {
            return this.nearDistance >= this.farDistance
                ? DataResult.error(() -> "Far distance (" + this.farDistance + ") cannot be closer or equal to near distance (" + this.nearDistance + ")")
                : DataResult.success(this);
        }
    }
    
    public ResourceLocation sprite(float distance) {
        if (distance < this.nearDistance) {
            return this.spriteLocations.getFirst();
        } else if (distance >= this.farDistance) {
            return this.spriteLocations.getLast();
        } else if (this.spriteLocations.size() == 1) {
            return this.spriteLocations.getFirst();
        } else if (this.spriteLocations.size() == 3) {
            return this.spriteLocations.get(1);
        } else {
            int index = Mth.lerpInt((distance - this.nearDistance) / (this.farDistance - this.nearDistance), 1, this.spriteLocations.size() - 1);
            return this.spriteLocations.get(index);
        }
    }
}
