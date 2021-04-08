package ru.betterend.world.structures.features;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;
import ru.betterend.world.structures.piece.PaintedMountainPiece;

public class PaintedMountainStructure extends FeatureBaseStructure {
	private static final BlockState[] VARIANTS;

	@Override
	public StructureFeature.StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
		return SDFStructureStart::new;
	}

	public static class SDFStructureStart extends StructureStart<DefaultFeatureConfig> {
		public SDFStructureStart(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box,
				int references, long seed) {
			super(feature, chunkX, chunkZ, box, references, seed);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator,
				StructureManager manager, int chunkX, int chunkZ, Biome biome, DefaultFeatureConfig config) {
			int x = (chunkX << 4) | MHelper.randRange(4, 12, random);
			int z = (chunkZ << 4) | MHelper.randRange(4, 12, random);
			int y = chunkGenerator.getHeight(x, z, Type.WORLD_SURFACE_WG);
			if (y > 50) {
				float radius = MHelper.randRange(50, 100, random);
				float height = radius * MHelper.randRange(0.4F, 0.6F, random);
				int count = MHelper.floor(height * MHelper.randRange(0.1F, 0.35F, random) + 1);
				BlockState[] slises = new BlockState[count];
				for (int i = 0; i < count; i++) {
					slises[i] = VARIANTS[random.nextInt(VARIANTS.length)];
				}
				this.children
						.add(new PaintedMountainPiece(new BlockPos(x, y, z), radius, height, random, biome, slises));
			}
			this.setBoundingBoxFromChildren();
		}
	}

	static {
		VARIANTS = new BlockState[] { Blocks.END_STONE.defaultBlockState(),
				EndBlocks.FLAVOLITE.stone.defaultBlockState(), EndBlocks.VIOLECITE.stone.defaultBlockState(), };
	}
}
