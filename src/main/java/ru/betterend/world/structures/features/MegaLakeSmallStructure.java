package ru.betterend.world.structures.features;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import ru.bclib.util.MHelper;
import ru.betterend.world.structures.piece.LakePiece;

import java.util.Random;

public class MegaLakeSmallStructure extends FeatureBaseStructure {
    public MegaLakeSmallStructure() {
        super(PieceGeneratorSupplier.simple(
                FeatureBaseStructure::checkLocation,
                MegaLakeSmallStructure::generatePieces
        ));
    }

    protected static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        final Random random = context.random();
        final ChunkPos chunkPos = context.chunkPos();
        final ChunkGenerator chunkGenerator = context.chunkGenerator();
        final LevelHeightAccessor levelHeightAccessor = context.heightAccessor();


        int x = chunkPos.getBlockX(MHelper.randRange(4, 12, random));
        int z = chunkPos.getBlockZ(MHelper.randRange(4, 12, random));
        int y = chunkGenerator.getBaseHeight(x, z, Types.WORLD_SURFACE_WG, levelHeightAccessor);

        //TODO: 1.18 right way to get biome?
        Biome biome = chunkGenerator.getNoiseBiome(x, y, z);
        if (y > 5) {
            float radius = MHelper.randRange(20, 40, random);
            float depth = MHelper.randRange(5, 10, random);
            LakePiece piece = new LakePiece(new BlockPos(x, y, z), radius, depth, random, biome);
            structurePiecesBuilder.addPiece(piece);
        }

        //this.calculateBoundingBox();
    }
}
