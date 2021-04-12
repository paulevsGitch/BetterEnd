package ru.betterend.world.structures.features;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import ru.betterend.util.MHelper;
import ru.betterend.world.structures.piece.CrystalMountainPiece;

public class MountainStructure extends FeatureBaseStructure {
	@Override
	public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStructureStartFactory() {
		return SDFStructureStart::new;
	}

	public static class SDFStructureStart extends StructureStart<NoneFeatureConfiguration> {
		public SDFStructureStart(StructureFeature<NoneFeatureConfiguration> feature, int chunkX, int chunkZ,
				BlockBox box, int references, long seed) {
			super(feature, chunkX, chunkZ, box, references, seed);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator,
				StructureManager manager, int chunkX, int chunkZ, Biome biome, NoneFeatureConfiguration config) {
			int x = (chunkX << 4) | MHelper.randRange(4, 12, random);
			int z = (chunkZ << 4) | MHelper.randRange(4, 12, random);
			int y = chunkGenerator.getHeight(x, z, Type.WORLD_SURFACE_WG);
			if (y > 5) {
				float radius = MHelper.randRange(50, 100, random);
				float height = radius * MHelper.randRange(0.8F, 1.2F, random);
				CrystalMountainPiece piece = new CrystalMountainPiece(new BlockPos(x, y, z), radius, height, random,
						biome);
				this.children.add(piece);
			}
			this.setBoundingBoxFromChildren();
		}
	}
}
