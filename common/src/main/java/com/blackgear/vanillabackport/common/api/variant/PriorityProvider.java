package com.blackgear.vanillabackport.common.api.variant;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.util.RandomSource;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface PriorityProvider<Context, Condition extends PriorityProvider.SelectorCondition<Context>> {
    List<Selector<Context, Condition>> selectors();

    static <C, T> Stream<T> select(Stream<T> stream, Function<T, PriorityProvider<C, ?>> function, C context) {
        List<UnpackedEntry<C, T>> list = new ArrayList<>();
        stream.forEach(object -> {
            PriorityProvider<C, ?> provider = function.apply(object);

            for (Selector<C, ?> selector : provider.selectors()) {
                list.add(
                    new UnpackedEntry<>(
                        object,
                        selector.priority(),
                        DataFixUtils.orElseGet(selector.condition(), SelectorCondition::alwaysTrue)
                    )
                );
            }
        });

        list.sort(UnpackedEntry.HIGHEST_PRIORITY_FIRST);
        Iterator<PriorityProvider.UnpackedEntry<C, T>> iterator = list.iterator();
        int priority = Integer.MIN_VALUE;

        while (iterator.hasNext()) {
            PriorityProvider.UnpackedEntry<C, T> entry = iterator.next();
            if (entry.priority < priority) {
                iterator.remove();
            } else if (entry.condition.test(context)) {
                priority = entry.priority;
            } else {
                iterator.remove();
            }
        }

        return list.stream().map(UnpackedEntry::entry);
    }

    static <C, T> Optional<T> pick(Stream<T> stream, Function<T, PriorityProvider<C, ?>> function, RandomSource random, C context) {
        List<T> list = select(stream, function, context).toList();
        return Util.getRandomSafe(list, random);
    }

    static <Context, Condition extends SelectorCondition<Context>> List<Selector<Context, Condition>> single(Condition condition, int priority) {
        return List.of(new Selector<>(condition, priority));
    }

    static <Context, Condition extends SelectorCondition<Context>> List<Selector<Context, Condition>> alwaysTrue(int priority) {
        return List.of(new Selector<>(priority));
    }

    record Selector<Context, Condition extends SelectorCondition<Context>>(Optional<Condition> condition, int priority) {
        public Selector(Condition condition, int priority) {
            this(Optional.of(condition), priority);
        }

        public Selector(int priority) {
            this(Optional.empty(), priority);
        }

        public static <Context, Condition extends SelectorCondition<Context>> Codec<Selector<Context, Condition>> codec(Codec<Condition> codec) {
            return RecordCodecBuilder.create(instance -> instance.group(
                codec.optionalFieldOf("condition").forGetter(Selector::condition),
                Codec.INT.fieldOf("priority").forGetter(Selector::priority)
            ).apply(instance, Selector::new));
        }
    }

    interface SelectorCondition<C> extends Predicate<C> {
        static <C> SelectorCondition<C> alwaysTrue() {
            return context -> true;
        }
    }

    record UnpackedEntry<C, T>(T entry, int priority, SelectorCondition<C> condition) {
        public static final Comparator<UnpackedEntry<?, ?>> HIGHEST_PRIORITY_FIRST = Comparator.<UnpackedEntry<?, ?>>comparingInt(UnpackedEntry::priority).reversed();
    }
}