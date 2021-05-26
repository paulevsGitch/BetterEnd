package ru.betterend.world.structures.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import ru.bclib.util.MHelper;
import ru.betterend.registry.EndBlocks;
import ru.betterend.world.structures.piece.PaintedMountainPiece;

public class PaintedMountainStructure extends FeatureBaseStructure {
	private static final BlockState[] VARIANTS;
	
	@Override
	public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
		return SDFStructureStart::new;
	}
	
	public static class SDFStructureStart extends StructureStart<NoneFeatureConfiguration> {
		public SDFStructureStart(StructureFeature<NoneFeatureConfiguration> feature, int chunkX, int chunkZ, BoundingBox box, int references, long seed) {
			super(feature, chunkX, chunkZ, box, references, seed);
		}

		@Override
		public void generatePieces(RegistryAccess registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, NoneFeatureConfiguration config) {
			int x = (chunkX << 4) | MHelper.randRange(4, 12, random);
			int z = (chunkZ << 4) | MHelper.randRange(4, 12, random);
			int y = chunkGenerator.getBaseHeight(x, z, Types.WORLD_SURFACE_WG);
			if (y > 50) {
				float radius = MHelper.randRange(50, 100, random);
				float height = radius * MHelper.randRange(0.4F, 0.6F, random);
				int count = MHelper.floor(height * MHelper.randRange(0.1F, 0.35F, random) + 1);
				BlockState[] slises = new BlockState[count];
				for (int i = 0; i < count; i++) {
					slises[i] = VARIANTS[random.nextInt(VARIANTS.length)];
				}
				this.pieces.add(new PaintedMountainPiece(new BlockPos(x, y, z), radius, height, random, biome, slises ));
			}
			this.calculateBoundingBox();
		}
	}
	
	static {
		VARIANTS = new BlockState[] {
				Blocks.END_STONE.defaultBlockState(),
				EndBlocks.FLAVOLITE.stone.defaultBlockState(),
				EndBlocks.VIOLECITE.stone.defaultBlockState(),
		};
	}
}
