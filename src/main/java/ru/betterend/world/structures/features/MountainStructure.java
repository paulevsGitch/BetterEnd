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
import ru.betterend.world.structures.piece.CrystalMountainPiece;

import java.util.Random;

public class MountainStructure extends FeatureBaseStructure {

	public MountainStructure() {
		super(PieceGeneratorSupplier.simple(
				FeatureBaseStructure::checkLocation,
				MountainStructure::generatePieces
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
			//TODO: 1.18 right way to get biome?
			Biome biome = chunkGenerator.getNoiseBiome(x, y, z);

			float radius = MHelper.randRange(50, 100, random);
			float height = radius * MHelper.randRange(0.8F, 1.2F, random);
			CrystalMountainPiece piece = new CrystalMountainPiece(
					new BlockPos(x, y, z),
					radius,
					height,
					random,
					biome
			);
			structurePiecesBuilder.addPiece(piece);
		}

		//this.calculateBoundingBox();
	}
}

