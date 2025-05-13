package com.blackgear.vanillabackport.core.mixin.access;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TreeDecoratorType.class)
public interface TreeDecoratorTypeAccessor {
    @Invoker("<init>")
    static <T extends TreeDecorator> TreeDecoratorType<T> createTreeDecorator(Codec<T> codec) {
        throw new AssertionError();
    }

}