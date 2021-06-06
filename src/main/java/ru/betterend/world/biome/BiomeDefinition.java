package ru.betterend.world.biome;

import net.minecraft.world.level.biome.Biome.BiomeCategory;
import ru.bclib.world.biomes.BCLBiomeDef;
import ru.betterend.BetterEnd;
import ru.betterend.world.structures.EndStructureFeature;

public class BiomeDefinition extends BCLBiomeDef {
	private boolean hasCaves = true;
	
	public BiomeDefinition(String name) {
		super(BetterEnd.makeID(name));
		this.endBiome();
	}
	
	public BiomeDefinition setCaveBiome() {
		this.setCategory(BiomeCategory.NONE);
		return this;
	}
	
	public BiomeDefinition setCaves(boolean hasCaves) {
		this.hasCaves = hasCaves;
		return this;
	}

	public boolean hasCaves() {
		return hasCaves;
	}

	public BCLBiomeDef addStructureFeature(EndStructureFeature structure) {
		return addStructureFeature(structure.getFeatureConfigured());
	}
}