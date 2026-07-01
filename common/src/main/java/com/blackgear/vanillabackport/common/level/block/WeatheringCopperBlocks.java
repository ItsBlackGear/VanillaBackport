package com.blackgear.vanillabackport.common.level.block;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record WeatheringCopperBlocks(
    Supplier<Block> unaffected,
    Supplier<Block> exposed,
    Supplier<Block> weathered,
    Supplier<Block> oxidized,
    Supplier<Block> waxed,
    Supplier<Block> waxedExposed,
    Supplier<Block> waxedWeathered,
    Supplier<Block> waxedOxidized
) {
    public static <WaxedBlock extends Block, WeatheringBlock extends Block & WeatheringCopper> WeatheringCopperBlocks create(
        String id,
        TriFunction<String, Function<BlockBehaviour.Properties, Block>, BlockBehaviour.Properties, Supplier<Block>> register,
        Function<BlockBehaviour.Properties, WaxedBlock> waxedFactory,
        BiFunction<WeatherState, BlockBehaviour.Properties, WeatheringBlock> weatheringFactory,
        Function<WeatherState, BlockBehaviour.Properties> properties
    ) {
        return new WeatheringCopperBlocks(
            register.apply(id, p -> weatheringFactory.apply(WeatherState.UNAFFECTED, p), properties.apply(WeatherState.UNAFFECTED)),
            register.apply("exposed_" + id, p -> weatheringFactory.apply(WeatherState.EXPOSED, p), properties.apply(WeatherState.EXPOSED)),
            register.apply("weathered_" + id, p -> weatheringFactory.apply(WeatherState.WEATHERED, p), properties.apply(WeatherState.WEATHERED)),
            register.apply("oxidized_" + id, p -> weatheringFactory.apply(WeatherState.OXIDIZED, p), properties.apply(WeatherState.OXIDIZED)),
            register.apply("waxed_" + id, waxedFactory::apply, properties.apply(WeatherState.UNAFFECTED)),
            register.apply("waxed_exposed_" + id, waxedFactory::apply, properties.apply(WeatherState.EXPOSED)),
            register.apply("waxed_weathered_" + id, waxedFactory::apply, properties.apply(WeatherState.WEATHERED)),
            register.apply("waxed_oxidized_" + id, waxedFactory::apply, properties.apply(WeatherState.OXIDIZED))
        );
    }

    public ImmutableBiMap<Supplier<Block>, Supplier<Block>> weatheringMapping() {
        return ImmutableBiMap.of(this.unaffected, this.exposed, this.exposed, this.weathered, this.weathered, this.oxidized);
    }

    public ImmutableBiMap<Supplier<Block>, Supplier<Block>> waxedMapping() {
        return ImmutableBiMap.of(this.unaffected, this.waxed, this.exposed, this.waxedExposed, this.weathered, this.waxedWeathered, this.oxidized, this.waxedOxidized);
    }

    public ImmutableList<Supplier<Block>> asList() {
        return ImmutableList.of(this.unaffected, this.exposed, this.weathered, this.oxidized, this.waxed, this.waxedExposed, this.waxedWeathered, this.waxedOxidized);
    }

    public void forEach(Consumer<Supplier<Block>> consumer) {
        consumer.accept(this.unaffected);
        consumer.accept(this.exposed);
        consumer.accept(this.weathered);
        consumer.accept(this.oxidized);
        consumer.accept(this.waxed);
        consumer.accept(this.waxedExposed);
        consumer.accept(this.waxedWeathered);
        consumer.accept(this.waxedOxidized);
    }
}