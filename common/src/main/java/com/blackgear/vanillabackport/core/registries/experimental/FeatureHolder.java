package com.blackgear.vanillabackport.core.registries.experimental;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class FeatureHolder<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private final boolean isPresent;
    
    private FeatureHolder(Supplier<T> supplier, boolean isPresent) {
        this.supplier = supplier;
        this.isPresent = isPresent;
    }
    
    public static <T> FeatureHolder<T> of(Supplier<T> supplier) {
        Objects.requireNonNull(supplier, "Supplier cannot be null");
        return new FeatureHolder<>(supplier, true);
    }
    
    public static <T> FeatureHolder<T> empty() {
        return new FeatureHolder<>(null, false);
    }
    
    @Override
    public T get() {
        if (this.isPresent) return this.supplier.get();
        throw new NoSuchElementException("This feature is disabled");
    }
    
    public T orElse(T fallback) {
        return this.isPresent ? this.get() : fallback;
    }
    
    public <U> FeatureHolder<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        return this.isPresent
            ? FeatureHolder.of(() -> mapper.apply(this.get()))
            : FeatureHolder.empty();
    }
    
    public boolean isPresent() {
        return this.isPresent;
    }
    
    public void ifPresent(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        if (this.isPresent) action.accept(this.get());
    }
    
    public void ifPresentOrElse(Consumer<? super T> action, Runnable ifEmpty) {
        Objects.requireNonNull(action);
        Objects.requireNonNull(ifEmpty);
        if (this.isPresent) {
            action.accept(this.get());
        } else {
            ifEmpty.run();
        }
    }
}