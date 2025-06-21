package com.blackgear.vanillabackport.core.mixin.common.worldgen.structure;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.structures.NetherFossilPieces;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherFossilPieces.NetherFossilPiece.class)
public abstract class NetherFossilPieceMixin extends TemplateStructurePiece {
    public NetherFossilPieceMixin(StructurePieceType type, int genDepth, StructureTemplateManager structureTemplateManager, ResourceLocation location, String templateName, StructurePlaceSettings placeSettings, BlockPos templatePosition) {
        super(type, genDepth, structureTemplateManager, location, templateName, placeSettings, templatePosition);
    }

    @Inject(
        method = "postProcess",
        at = @At("TAIL")
    )
    private void placeDriedGhast(
        WorldGenLevel level,
        StructureManager structureManager,
        ChunkGenerator generator,
        RandomSource worldRandom,
        BoundingBox box,
        ChunkPos chunkPos,
        BlockPos origin,
        CallbackInfo ci
    ) {
        if (VanillaBackport.CONFIG.generateDriedGhasts.get() && level.dimensionType().ultraWarm() && level.dimensionType().piglinSafe()) { // Check for Nether dimension
            BoundingBox template = this.template().getBoundingBox(this.placeSettings(), this.templatePosition());
            RandomSource random = RandomSource.create(level.getSeed()).forkPositional().at(template.getCenter());
            if (random.nextFloat() < 0.5F) {
                int x = template.minX() + random.nextInt(template.getXSpan());
                int y = template.minY();
                int z = template.minZ() + random.nextInt(template.getZSpan());
                BlockPos pos = new BlockPos(x, y, z);
                if (level.getBlockState(pos).isAir() && box.isInside(pos)) {
                    level.setBlock(pos, ModBlocks.DRIED_GHAST.get().defaultBlockState().rotate(Rotation.getRandom(random)), 2);
                }
            }
        }
    }
}