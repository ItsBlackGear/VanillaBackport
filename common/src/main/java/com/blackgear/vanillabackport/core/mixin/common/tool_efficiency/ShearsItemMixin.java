package com.blackgear.vanillabackport.core.mixin.common.tool_efficiency;

import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.component.Tool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    @ModifyReturnValue(method = "createToolProperties", at = @At("RETURN"))
    private static Tool vb$addShearToolRules(Tool original) {
        List<Tool.Rule> rules = new ArrayList<>(original.rules());

        rules.add(Tool.Rule.overrideSpeed(ModBlockTags.WOOL_STAIRS, 5.0F));
        rules.add(Tool.Rule.overrideSpeed(ModBlockTags.WOOL_SLABS, 5.0F));

        return new Tool(rules, original.defaultMiningSpeed(), original.damagePerBlock());
    }
}