package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.worldgen.tree_decorators.AttachedToLogsDecorator;
import com.blackgear.vanillabackport.common.level.worldgen.tree_decorators.CreakingHeartDecorator;
import com.blackgear.vanillabackport.common.level.worldgen.tree_decorators.PaleMossDecorator;
import com.blackgear.vanillabackport.common.level.worldgen.tree_decorators.PlaceOnGroundDecorator;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.common.access.TreeDecoratorTypeAccessor;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.function.Supplier;

public class ModTreeDecorators {
    public static final CoreRegistry<TreeDecoratorType<?>> REGISTRIES = CoreRegistry.create(Registries.TREE_DECORATOR_TYPE, VanillaBackport.NAMESPACE);

    public static final Supplier<TreeDecoratorType<PaleMossDecorator>> PALE_MOSS = register("pale_moss", PaleMossDecorator.CODEC);
    public static final Supplier<TreeDecoratorType<CreakingHeartDecorator>> CREAKING_HEART = register("creaking_heart", CreakingHeartDecorator.CODEC);
    public static final Supplier<TreeDecoratorType<AttachedToLogsDecorator>> ATTACHED_TO_LOGS = register("attached_to_logs", AttachedToLogsDecorator.CODEC);
    public static final Supplier<TreeDecoratorType<PlaceOnGroundDecorator>> PLACE_ON_GROUND = register("place_on_ground", PlaceOnGroundDecorator.CODEC);

    private static <P extends TreeDecorator> Supplier<TreeDecoratorType<P>> register(String name, Codec<P> codec) {
        return REGISTRIES.register(name, () -> TreeDecoratorTypeAccessor.createTreeDecorator(codec));
    }
}