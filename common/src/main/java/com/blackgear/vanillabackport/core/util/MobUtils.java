package com.blackgear.vanillabackport.core.util;

import com.blackgear.vanillabackport.common.level.items.AnimalArmorItem;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MobUtils {
    private static final Map<TagKey<Item>, String> entityArmorMap = new HashMap();

    public static boolean isPanicking(PathfinderMob mob) {
        if (mob.getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING)) {
            return mob.getBrain().getMemory(MemoryModuleType.IS_PANICKING).isPresent();
        } else {
            for (WrappedGoal goal : mob.goalSelector.getAvailableGoals()) {
                if (goal.isRunning() && goal.getGoal() instanceof PanicGoal) {
                    return true;
                }
            }

            return false;
        }
    }

    public static void addArmorType(TagKey<Item> key, String value) {
        entityArmorMap.put(key, value);
    }

    public static String getArmorType(ItemStack stack) {
        return (String)entityArmorMap.entrySet().stream().filter((entry) -> {
            return stack.is((TagKey)entry.getKey());
        }).map(Map.Entry::getValue).findFirst().orElse("");
    }

    public static void renderArmor(ResourceLocation location, Model model, ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int i) {
        if (!stack.is(ModItemTags.WOLF_ARMOR)) {
            String type = getArmorType(stack);
            if (!type.isBlank()) {
                VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(location.withPath((path) -> {
                    return "textures/entity/equipment/" + type + "/" + path + ".png";
                })));
                model.renderToBuffer(poseStack, consumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    public static boolean canWearArmor(LivingEntity entity) {
        return Arrays.stream(AnimalArmorItem.BodyType.values()).flatMap((bodyType) -> {
            return bodyType.allowedEntities.stream();
        }).anyMatch((holder) -> {
            return holder.value() == entity.getType();
        });
    }

    public static boolean canFeed(LivingEntity entity) {
        return canWearArmor(entity);
    }

    static {
        addArmorType(ModItemTags.WOLF_ARMOR, "wolf_body");
    }
}