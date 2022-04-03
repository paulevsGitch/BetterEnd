package ru.betterend.world.structures.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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

public class MegaLakeStructure extends FeatureBaseStructure {


	public MegaLakeStructure() {
		super(PieceGeneratorSupplier.simple(
				FeatureBaseStructure::checkLocation,
				MegaLakeStructure::generatePieces
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

		if (y > 5) {
			Holder<Biome> biome = chunkGenerator.getNoiseBiome(x >> 2, y >> 2, z >> 2);

			float radius = MHelper.randRange(32, 64, random);
			float depth = MHelper.randRange(7, 15, random);
			LakePiece piece = new LakePiece(new BlockPos(x, y, z), radius, depth, random, biome);
			structurePiecesBuilder.addPiece(piece);
		}

		//this.calculateBoundingBox();
	}

}
