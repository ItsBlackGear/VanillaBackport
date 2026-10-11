package com.blackgear.vanillabackport.common.api.modules.waypoints;

import com.blackgear.vanillabackport.core.util.codec.AdditionalCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public interface Waypoint {
    class Icon {
        public static final Codec<Icon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(WaypointStyleAssets.ROOT_ID).fieldOf("style").forGetter(icon -> icon.style),
            AdditionalCodecs.RGB_COLOR_CODEC.optionalFieldOf("color").forGetter(icon -> icon.color)
        ).apply(instance, Icon::new));
        
        public static final Icon NULL = new Icon();
        public ResourceKey<WaypointStyleAsset> style = WaypointStyleAssets.DEFAULT;
        public Optional<Integer> color = Optional.empty();
        
        public Icon() { /* NO-OP */ }
        
        public Icon(ResourceKey<WaypointStyleAsset> style, Optional<Integer> color) {
            this.style = style;
            this.color = color;
        }
        
        public boolean hasData() {
            return this.style != WaypointStyleAssets.DEFAULT || this.color.isPresent();
        }
        
        public Icon cloneAndAssignStyle(LivingEntity entity) {
            ResourceKey<WaypointStyleAsset> overrideStyle = this.getOverrideStyle();
            Optional<Integer> colorOverride = this.color.or(() -> Optional.ofNullable(entity.getTeam()).map(team -> team.getColor().getColor()).map(color -> color == 0 ? -13619152 : color));
            return overrideStyle == this.style && colorOverride.isEmpty() ? this : new Icon(overrideStyle, colorOverride);
        }
        
        private ResourceKey<WaypointStyleAsset> getOverrideStyle() {
            return this.style != WaypointStyleAssets.DEFAULT ? this.style : WaypointStyleAssets.DEFAULT;
        }
    }
}