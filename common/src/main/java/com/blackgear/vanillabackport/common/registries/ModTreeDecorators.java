package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.AttachedToLogsDecorator;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.CreakingHeartDecorator;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.PaleMossDecorator;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.PlaceOnGroundDecorator;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.access.TreeDecoratorTypeAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.function.Supplier;

public class ModTreeDecorators {
    public static final CoreRegistry<TreeDecoratorType<?>> DECORATORS = CoreRegistry.create(Registries.TREE_DECORATOR_TYPE, VanillaBackport.NAMESPACE);

    public static final Supplier<TreeDecoratorType<PaleMossDecorator>> PALE_MOSS = register("pale_moss", PaleMossDecorator.CODEC);
    public static final Supplier<TreeDecoratorType<CreakingHeartDecorator>> CREAKING_HEART = register("creaking_heart", CreakingHeartDecorator.CODEC);
    public static final Supplier<TreeDecoratorType<AttachedToLogsDecorator>> ATTACHED_TO_LOGS = register("attached_to_logs", AttachedToLogsDecorator.CODEC);
    public static final Supplier<TreeDecoratorType<PlaceOnGroundDecorator>> PLACE_ON_GROUND = register("place_on_ground", PlaceOnGroundDecorator.CODEC);

    private static <P extends TreeDecorator> Supplier<TreeDecoratorType<P>> register(String name, Codec<P> codec) {
        return DECORATORS.register(name, () -> TreeDecoratorTypeAccessor.createTreeDecorator(codec));
    }
}