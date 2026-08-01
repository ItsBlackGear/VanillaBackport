package com.blackgear.vanillabackport.common.predicates;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMaps;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.advancements.critereon.GameTypePredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.StatsCounter;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public record PlayerPredicate(
    MinMaxBounds.Ints level,
    FoodPredicate food,
    GameTypePredicate gameType,
    List<StatMatcher<?>> stats,
    Object2BooleanMap<ResourceLocation> recipes,
    Map<ResourceLocation, AdvancementPredicate> advancements,
    Optional<EntityPredicate> lookingAt
) implements EntitySubPredicate {
    public static final MapCodec<PlayerPredicate> CODEC = RecordCodecBuilder.mapCodec(
        instance -> instance.group(
                MinMaxBounds.Ints.CODEC.optionalFieldOf("level", MinMaxBounds.Ints.ANY).forGetter(PlayerPredicate::level),
                FoodPredicate.CODEC.optionalFieldOf("food", FoodPredicate.ANY).forGetter(PlayerPredicate::food),
                GameTypePredicate.CODEC.optionalFieldOf("gamemode", GameTypePredicate.ANY).forGetter(PlayerPredicate::gameType),
                StatMatcher.CODEC.listOf().optionalFieldOf("stats", List.of()).forGetter(PlayerPredicate::stats),
                ExtraCodecs.object2BooleanMap(ResourceLocation.CODEC).optionalFieldOf("recipes", Object2BooleanMaps.emptyMap()).forGetter(PlayerPredicate::recipes),
                Codec.unboundedMap(ResourceLocation.CODEC, AdvancementPredicate.CODEC)
                    .optionalFieldOf("advancements", Map.of())
                    .forGetter(PlayerPredicate::advancements),
                EntityPredicate.CODEC.optionalFieldOf("looking_at").forGetter(PlayerPredicate::lookingAt)
            )
            .apply(instance, PlayerPredicate::new)
    );
    
    @Override
    public MapCodec<? extends EntitySubPredicate> codec() {
        return CODEC;
    }
    
    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        if (!(entity instanceof ServerPlayer player)) {
            return false;
        } else if (!this.level.matches(player.experienceLevel)) {
            return false;
        } else if (!this.food.matches(player.getFoodData())) {
            return false;
        } else if (!this.gameType.matches(player.gameMode.getGameModeForPlayer())) {
            return false;
        } else {
            StatsCounter stats = player.getStats();
            
            for (StatMatcher<?> stat : this.stats) {
                if (!stat.matches(stats)) {
                    return false;
                }
            }
            
            ServerRecipeBook recipes = player.getRecipeBook();
            
            for (var entry : this.recipes.object2BooleanEntrySet()) {
                if (recipes.contains(entry.getKey()) != entry.getBooleanValue()) {
                    return false;
                }
            }
            
            if (!this.advancements.isEmpty()) {
                PlayerAdvancements advancements = player.getAdvancements();
                ServerAdvancementManager serverAdvancements = player.level().getServer().getAdvancements();
                
                for (var entry : this.advancements.entrySet()) {
                    AdvancementHolder advancement = serverAdvancements.get(entry.getKey());
                    if (advancement == null || !entry.getValue().test(advancements.getOrStartProgress(advancement))) {
                        return false;
                    }
                }
            }
            
            if (this.lookingAt.isPresent()) {
                Vec3 from = player.getEyePosition();
                Vec3 viewVec = player.getViewVector(1.0F);
                Vec3 to = from.add(viewVec.x * 100.0, viewVec.y * 100.0, viewVec.z * 100.0);
                EntityHitResult lookingAtResult = ProjectileUtil.getEntityHitResult(player.level(), player, from, to, new AABB(from, to).inflate(1.0), ex -> !ex.isSpectator(), 0.0F);
                if (lookingAtResult == null || lookingAtResult.getType() != HitResult.Type.ENTITY) {
                    return false;
                }
                
                Entity lookingAtEntity = lookingAtResult.getEntity();
                return this.lookingAt.get().matches(player, lookingAtEntity) && player.hasLineOfSight(lookingAtEntity);
            }
            
            return true;
        }
    }
    
    private record AdvancementCriterionsPredicate(Object2BooleanMap<String> criterions) implements AdvancementPredicate {
        public static final Codec<AdvancementCriterionsPredicate> CODEC = ExtraCodecs.object2BooleanMap(Codec.STRING).xmap(AdvancementCriterionsPredicate::new, AdvancementCriterionsPredicate::criterions);
        
        @Override
        public boolean test(AdvancementProgress progress) {
            for (var entry : this.criterions.object2BooleanEntrySet()) {
                CriterionProgress criterion = progress.getCriterion(entry.getKey());
                if (criterion == null || criterion.isDone() != entry.getBooleanValue()) {
                    return false;
                }
            }
            
            return true;
        }
    }
    
    private record AdvancementDonePredicate(boolean state) implements AdvancementPredicate {
        public static final Codec<AdvancementDonePredicate> CODEC = Codec.BOOL.xmap(AdvancementDonePredicate::new, AdvancementDonePredicate::state);
        
        @Override
        public boolean test(AdvancementProgress progress) {
            return progress.isDone() == this.state;
        }
    }
    
    private interface AdvancementPredicate extends Predicate<AdvancementProgress> {
        Codec<AdvancementPredicate> CODEC = Codec.either(AdvancementDonePredicate.CODEC, AdvancementCriterionsPredicate.CODEC)
            .xmap(Either::unwrap, predicate -> {
                if (predicate instanceof AdvancementDonePredicate done) {
                    return Either.left(done);
                } else if (predicate instanceof AdvancementCriterionsPredicate criterions) {
                    return Either.right(criterions);
                } else {
                    throw new UnsupportedOperationException();
                }
            });
    }
    
    public static class Builder {
        private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
        private FoodPredicate food = FoodPredicate.ANY;
        private GameTypePredicate gameType = GameTypePredicate.ANY;
        private final ImmutableList.Builder<StatMatcher<?>> stats = ImmutableList.builder();
        private final Object2BooleanMap<ResourceLocation> recipes = new Object2BooleanOpenHashMap<>();
        private final Map<ResourceLocation, AdvancementPredicate> advancements = Maps.newHashMap();
        private Optional<EntityPredicate> lookingAt = Optional.empty();
        
        public static Builder player() {
            return new Builder();
        }
        
        public Builder setLevel(MinMaxBounds.Ints level) {
            this.level = level;
            return this;
        }
        
        public Builder setFood(FoodPredicate food) {
            this.food = food;
            return this;
        }
        
        public <T> Builder addStat(StatType<T> type, Holder.Reference<T> value, MinMaxBounds.Ints range) {
            this.stats.add(new StatMatcher<>(type, value, range));
            return this;
        }
        
        public Builder addRecipe(ResourceLocation recipe, boolean present) {
            this.recipes.put(recipe, present);
            return this;
        }
        
        public Builder setGameType(GameTypePredicate gameType) {
            this.gameType = gameType;
            return this;
        }
        
        public Builder setLookingAt(EntityPredicate.Builder lookingAt) {
            this.lookingAt = Optional.of(lookingAt.build());
            return this;
        }
        
        public Builder checkAdvancementDone(ResourceLocation advancement, boolean isDone) {
            this.advancements.put(advancement, new AdvancementDonePredicate(isDone));
            return this;
        }
        
        public Builder checkAdvancementCriterions(ResourceLocation advancement, Map<String, Boolean> criterions) {
            this.advancements.put(advancement, new AdvancementCriterionsPredicate(new Object2BooleanOpenHashMap<>(criterions)));
            return this;
        }
        
        public PlayerPredicate build() {
            return new PlayerPredicate(this.level, this.food, this.gameType, this.stats.build(), this.recipes, this.advancements, this.lookingAt);
        }
    }
    
    private record StatMatcher<T>(StatType<T> type, Holder<T> value, MinMaxBounds.Ints range, Supplier<Stat<T>> stat) {
        public static final Codec<StatMatcher<?>> CODEC = BuiltInRegistries.STAT_TYPE.byNameCodec().dispatch(StatMatcher::type, StatMatcher::createTypedCodec);
        
        public StatMatcher(StatType<T> type, Holder<T> value, MinMaxBounds.Ints range) {
            this(type, value, range, Suppliers.memoize(() -> type.get(value.value())));
        }
        
        private static <T> MapCodec<StatMatcher<T>> createTypedCodec(StatType<T> type) {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                type.getRegistry().holderByNameCodec().fieldOf("stat").forGetter(StatMatcher::value),
                MinMaxBounds.Ints.CODEC.optionalFieldOf("value", MinMaxBounds.Ints.ANY).forGetter(StatMatcher::range)
            ).apply(instance, (value, range) -> new StatMatcher<>(type, value, range)));
        }
        
        public boolean matches(StatsCounter counter) {
            return this.range.matches(counter.getValue(this.stat.get()));
        }
    }
}