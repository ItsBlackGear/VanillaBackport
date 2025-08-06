package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.CreakingHeartDecorator;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.PaleMossDecorator;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.access.TreeDecoratorTypeAccessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.function.Supplier;

public class ModTreeDecorators {
    public static final CoreRegistry<TreeDecoratorType<?>> DECORATORS = CoreRegistry.create(Registries.TREE_DECORATOR_TYPE, VanillaBackport.NAMESPACE);

    public static final Supplier<TreeDecoratorType<PaleMossDecorator>> PALE_MOSS = DECORATORS.register(
        "pale_moss",
        () -> TreeDecoratorTypeAccessor.createTreeDecorator(PaleMossDecorator.CODEC)
    );
    public static final Supplier<TreeDecoratorType<CreakingHeartDecorator>> CREAKING_HEART = DECORATORS.register(
        "creaking_heart",
        () -> TreeDecoratorTypeAccessor.createTreeDecorator(CreakingHeartDecorator.CODEC)
    );
}