package com.blackgear.vanillabackport.core.forge.mixin.common.registries;

import com.blackgear.vanillabackport.core.mixin.common.access.ReferenceAccessor;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Mixin(value = MappedRegistry.class, priority = 500)
public abstract class MappedRegistryMixin<T> {
    @Shadow @Final private Map<ResourceLocation, Holder.Reference<T>> byLocation;
    @Shadow @Final private Map<ResourceKey<T>, Holder.Reference<T>> byKey;
    @Shadow @Final private Map<T, Holder.Reference<T>> byValue;
    @Shadow @Final private Object2IntMap<T> toId;
    @Shadow @Final private Map<T, Lifecycle> lifecycles;
    @Shadow @Nullable protected Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
    @Shadow private Lifecycle registryLifecycle;
    @Shadow private boolean frozen;
    @Shadow @Nullable private List<Holder.Reference<T>> holdersInOrder;
    
    @Inject(
        method = "registerMapping(ILnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/core/Holder$Reference;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$replaceDuplicate(int idParam, ResourceKey<T> key, T value, Lifecycle lifecycle, CallbackInfoReturnable<Holder.Reference<T>> cir) {
        if (!"minecraft".equals(key.location().getNamespace()) || this.frozen) {
            return;
        }
        
        Holder.Reference<T> existing = this.byLocation.get(key.location());
        if (existing == null) return;
        
        if (this.byValue.get(value) == existing) {
            cir.setReturnValue(existing);
            return;
        }
        
        if (this.byValue.containsKey(value)) return;
        
        T previous = this.byValue.entrySet().stream()
            .filter(entry -> entry.getValue() == existing)
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
        
        if (previous == null) return;
        
        int id = this.toId.getInt(previous);
        
        this.byValue.remove(previous);
        this.lifecycles.remove(previous);
        
        this.byValue.put(value, existing);
        this.lifecycles.put(value, lifecycle);
        
        if (id >= 0) {
            this.toId.put(value, id);
        }
        
        ((ReferenceAccessor<T>) existing).callBindValue(value);
        
        this.byLocation.put(key.location(), existing);
        this.byKey.put(key, existing);
        
        if (this.unregisteredIntrusiveHolders != null) {
            Holder.Reference<T> intrusive = this.unregisteredIntrusiveHolders.remove(value);
            if (intrusive != null) {
                ((ReferenceAccessor<T>) intrusive).callBindKey(key);
            }
        }
        
        this.registryLifecycle = this.registryLifecycle.add(lifecycle);
        this.holdersInOrder = null;
        
        cir.setReturnValue(existing);
    }
}