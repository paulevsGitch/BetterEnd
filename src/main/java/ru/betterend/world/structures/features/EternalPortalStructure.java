package ru.betterend.world.structures.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import ru.bclib.util.MHelper;
import ru.bclib.util.StructureHelper;
import ru.betterend.BetterEnd;
import ru.betterend.world.structures.piece.NBTPiece;

public class EternalPortalStructure extends FeatureBaseStructure {
	private static final ResourceLocation STRUCTURE_ID = BetterEnd.makeID("portal/eternal_portal");
	private static final StructureTemplate STRUCTURE = StructureHelper.readStructure(STRUCTURE_ID);
	
	@Override
	protected boolean isFeatureChunk(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed, WorldgenRandom chunkRandom, ChunkPos pos, Biome biome, ChunkPos chunkPos, NoneFeatureConfiguration featureConfig, LevelHeightAccessor levelHeightAccessor) {
		long x = (long) chunkPos.x * (long) chunkPos.x;
		long z = (long) chunkPos.z * (long) chunkPos.z;
		if (x + z < 1024L) {
			return false;
		}
		if (chunkGenerator.getBaseHeight(pos.getBlockX(8), pos.getBlockX(8), Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor) < 5) {
			return false;
		}
		return super.isFeatureChunk(chunkGenerator, biomeSource, worldSeed, chunkRandom, pos, biome, chunkPos, featureConfig, levelHeightAccessor);
	}
	
	@Override
	public StructureFeature.StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
		return PortalStructureStart::new;
	}
	
	public static class PortalStructureStart extends StructureStart<NoneFeatureConfiguration> {
		public PortalStructureStart(StructureFeature<NoneFeatureConfiguration> feature, ChunkPos pos, int references, long seed) {
			super(feature, pos, references, seed);
		}
		
		@Override
		public void generatePieces(RegistryAccess registryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, NoneFeatureConfiguration featureConfiguration, LevelHeightAccessor levelHeightAccessor) {
			int x = chunkPos.getBlockX(8);
			int z = chunkPos.getBlockZ(8);
			int y = chunkGenerator.getBaseHeight(x, z, Types.WORLD_SURFACE_WG, levelHeightAccessor);
			this.pieces.add(new NBTPiece(STRUCTURE_ID, STRUCTURE, new BlockPos(x, y - 4, z), random.nextInt(5), true, random));
		}
	}
}
