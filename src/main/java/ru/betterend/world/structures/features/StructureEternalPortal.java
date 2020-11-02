package ru.betterend.world.structures.features;

import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import ru.betterend.BetterEnd;
import ru.betterend.util.MHelper;
import ru.betterend.util.StructureHelper;
import ru.betterend.world.structures.piece.NBTPiece;

public class StructureEternalPortal extends StructureFeatureBase {
	private static final Identifier STRUCTURE_ID = BetterEnd.makeID("portal/eternal_portal");
	private static final Structure STRUCTURE = StructureHelper.readStructure(STRUCTURE_ID);
	
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
			if (y > 50) {
				this.children.add(new NBTPiece(STRUCTURE_ID, STRUCTURE, new BlockPos(x, y - 3, z), random.nextInt(5), true, random));
			}
			this.setBoundingBoxFromChildren();
		}
	}
}
