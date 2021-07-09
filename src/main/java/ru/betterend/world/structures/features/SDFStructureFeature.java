package ru.betterend.world.structures.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import ru.bclib.sdf.SDF;
import ru.bclib.util.MHelper;
import ru.betterend.world.structures.piece.VoxelPiece;

import java.util.Random;

public abstract class SDFStructureFeature extends FeatureBaseStructure {

	protected abstract SDF getSDF(BlockPos pos, Random random);

	@Override
	public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
		return SDFStructureStart::new;
	}

	public static class SDFStructureStart extends StructureStart<NoneFeatureConfiguration> {
		public SDFStructureStart(StructureFeature<NoneFeatureConfiguration> feature, ChunkPos chunkPos, int references, long seed) {
			super(feature, chunkPos, references, seed);
		}

		@Override
		public void generatePieces(RegistryAccess registryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, NoneFeatureConfiguration featureConfiguration, LevelHeightAccessor levelHeightAccessor) {
			int x = chunkPos.getBlockX(MHelper.randRange(4, 12, random));
			int z = chunkPos.getBlockZ(MHelper.randRange(4, 12, random));
			int y = chunkGenerator.getBaseHeight(x, z, Types.WORLD_SURFACE_WG, levelHeightAccessor);
			if (y > 5) {
				BlockPos start = new BlockPos(x, y, z);
				VoxelPiece piece = new VoxelPiece((world) -> {
					((SDFStructureFeature) this.getFeature()).getSDF(start, this.random).fillRecursive(world, start);
				}, random.nextInt());
				this.pieces.add(piece);
			}

			//this.calculateBoundingBox();
		}
	}
}
