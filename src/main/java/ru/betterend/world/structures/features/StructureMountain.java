package ru.betterend.world.structures.features;

import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import ru.betterend.util.MHelper;
import ru.betterend.world.structures.piece.CavePiece;
import ru.betterend.world.structures.piece.MountainPiece;

public class StructureMountain extends StructureFeatureBase {
	@Override
	public StructureFeature.StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
		return SDFStructureStart::new;
	}
	
	public static class SDFStructureStart extends StructureStart<DefaultFeatureConfig> {
		public SDFStructureStart(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed) {
			super(feature, chunkX, chunkZ, box, references, seed);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, DefaultFeatureConfig config) {
			int x = (chunkX << 4) | MHelper.randRange(4, 12, random);
			int z = (chunkZ << 4) | MHelper.randRange(4, 12, random);
			int y = chunkGenerator.getHeight(x, z, Type.WORLD_SURFACE_WG);
			if (y > 5) {
				float radius = MHelper.randRange(50, 100, random);
				float height = radius * MHelper.randRange(0.8F, 1.2F, random);
				MountainPiece piece = new MountainPiece(new BlockPos(x, y, z), radius, height, random.nextInt());
				this.children.add(piece);
				
				int count = (int) (radius / 15);
				for (int i = 0; i < count; i++) {
					double px = random.nextGaussian() * radius * 0.5 + x;
					double pz = random.nextGaussian() * radius * 0.5 + z;
					float rad = MHelper.randRange(10, 30, random);
					
					int posX = MHelper.floor(px);
					int posZ = MHelper.floor(pz);
					
					int minY = chunkGenerator.getHeight(posX, posZ, Type.WORLD_SURFACE_WG);
					if (minY > 56) {
						int maxY = MHelper.floor(piece.getProtoHeight(posX, minY, posZ) - rad);
						if (maxY > 20) {
							int posY = MHelper.randRange(20, maxY, random);
							this.children.add(new CavePiece(new BlockPos(posX, posY, posZ), rad, random.nextInt()));
						}
					}
				}
			}
			this.setBoundingBoxFromChildren();
		}
	}
}
