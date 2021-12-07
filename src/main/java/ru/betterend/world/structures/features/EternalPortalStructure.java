package ru.betterend.world.structures.features;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import ru.bclib.util.StructureHelper;
import ru.betterend.BetterEnd;
import ru.betterend.world.structures.piece.NBTPiece;

public class EternalPortalStructure extends FeatureBaseStructure {
    private static final ResourceLocation STRUCTURE_ID = BetterEnd.makeID("portal/eternal_portal");
    private static final StructureTemplate STRUCTURE = StructureHelper.readStructure(STRUCTURE_ID);

    public EternalPortalStructure() {
        super(PieceGeneratorSupplier.simple(
                EternalPortalStructure::checkLocation,
                EternalPortalStructure::generatePieces
        ));
    }

    protected static boolean checkLocation(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
        final ChunkPos chunkPos = context.chunkPos();
        final ChunkGenerator chunkGenerator = context.chunkGenerator();
        final LevelHeightAccessor levelHeightAccessor = context.heightAccessor();

        long x = (long) chunkPos.x * (long) chunkPos.x;
        long z = (long) chunkPos.z * (long) chunkPos.z;
        if (x + z < 1024L) {
            return false;
        }
        if (chunkGenerator.getBaseHeight(
                chunkPos.getBlockX(8),
                chunkPos.getBlockZ(8),
                Heightmap.Types.WORLD_SURFACE_WG,
                levelHeightAccessor
        ) < 5) {
            return false;
        }
        return FeatureBaseStructure.checkLocation(context);
    }

    protected static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        final Random random = context.random();
        final ChunkPos chunkPos = context.chunkPos();
        final ChunkGenerator chunkGenerator = context.chunkGenerator();
        final LevelHeightAccessor levelHeightAccessor = context.heightAccessor();
        int x = chunkPos.getBlockX(8);
        int z = chunkPos.getBlockZ(8);
        int y = chunkGenerator.getBaseHeight(x, z, Types.WORLD_SURFACE_WG, levelHeightAccessor);
        structurePiecesBuilder.addPiece(new NBTPiece(
                STRUCTURE_ID,
                STRUCTURE,
                new BlockPos(x, y - 4, z),
                random.nextInt(5),
                true,
                random
        ));
    }
}
