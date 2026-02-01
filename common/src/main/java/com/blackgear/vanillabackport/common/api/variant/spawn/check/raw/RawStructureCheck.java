package com.blackgear.vanillabackport.common.api.variant.spawn.check.raw;

import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.List;
import java.util.function.Predicate;

public record RawStructureCheck(TagKey<Structure> requiredStructures) implements SpawnCondition {
    public static final Codec<RawStructureCheck> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.codec(Registries.STRUCTURE).fieldOf("structures").forGetter(RawStructureCheck::requiredStructures)
    ).apply(instance, RawStructureCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return this.getStructureWithPieceAt(context, holder -> holder.is(this.requiredStructures)).isValid();
    }

    private StructureStart getStructureWithPieceAt(SpawnContext context, Predicate<Holder<Structure>> predicate) {
        StructureManager manager = context.level().getLevel().structureManager();
        BlockPos pos = context.pos();
        Registry<Structure> registry = manager.registryAccess().registryOrThrow(Registries.STRUCTURE);

        List<StructureStart> starts = manager.startsForStructure(new ChunkPos(pos), structure -> registry.getHolder(registry.getId(structure)).map(predicate::test).orElse(false));

        for (StructureStart start : starts) {
            if (manager.structureHasPieceAt(pos, start)) {
                return start;
            }
        }

        return StructureStart.INVALID_START;
    }

    @Override
    public Codec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}
