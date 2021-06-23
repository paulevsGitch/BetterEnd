package ru.betterend.world.biome.cave;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.ShufflingList;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import ru.bclib.api.BiomeAPI;
import ru.bclib.world.biomes.BCLBiomeDef;
import ru.bclib.world.features.BCLFeature;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndSounds;
import ru.betterend.world.biome.EndBiome;
import ru.betterend.world.features.terrain.caves.CaveChunkPopulatorFeature;

public class EndCaveBiome extends EndBiome {
	private ShufflingList<Feature<?>> floorFeatures = new ShufflingList<Feature<?>>();
	private ShufflingList<Feature<?>> ceilFeatures = new ShufflingList<Feature<?>>();
	
	public EndCaveBiome(BCLBiomeDef definition) {
		super(makeDef(definition));
	}
	
	private static BCLBiomeDef makeDef(BCLBiomeDef definition) {
		BCLFeature feature = BCLFeature.makeChunkFeature(
			BetterEnd.makeID(definition.getID().getPath() + "_cave_populator"),
			new CaveChunkPopulatorFeature(() -> (EndCaveBiome) BiomeAPI.getBiome(definition.getID()))
		);
		definition.setCategory(BiomeCategory.NONE).addFeature(feature);
		definition.setMusic(EndSounds.MUSIC_CAVES);
		definition.setLoop(EndSounds.AMBIENT_CAVES);
		return definition;
	}
	
	public void addFloorFeature(Feature<?> feature, int weight) {
		floorFeatures.add(feature, weight);
	}
	
	public void addCeilFeature(Feature<?> feature, int weight) {
		ceilFeatures.add(feature, weight);
	}
	
	public Feature<?> getFloorFeature(Random random) {
		return floorFeatures.isEmpty() ? null : floorFeatures.getOne(random);
	}
	
	public Feature<?> getCeilFeature(Random random) {
		return ceilFeatures.isEmpty() ? null : ceilFeatures.getOne(random);
	}
	
	public float getFloorDensity() {
		return 0;
	}
	
	public float getCeilDensity() {
		return 0;
	}
	
	public BlockState getCeil(BlockPos pos) {
		return null;
	}
	
	public BlockState getWall(BlockPos pos) {
		return null;
	}
}
