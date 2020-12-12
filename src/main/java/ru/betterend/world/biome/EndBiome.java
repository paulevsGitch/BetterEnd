package ru.betterend.world.biome;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.structure.Structure;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import ru.betterend.config.Configs;
import ru.betterend.util.JsonFactory;
import ru.betterend.util.StructureHelper;
import ru.betterend.world.features.EndFeature;
import ru.betterend.world.features.ListFeature;
import ru.betterend.world.features.ListFeature.StructureInfo;
import ru.betterend.world.features.NBTStructureFeature.TerrainMerge;

public class EndBiome {
	protected List<EndBiome> subbiomes = Lists.newArrayList();

	protected final Biome biome;
	protected final Identifier mcID;
	protected EndBiome edge;
	protected int edgeSize;

	protected EndBiome biomeParent;
	protected float maxSubBiomeChance = 1;
	protected final float genChanceUnmutable;
	protected float genChance = 1;

	private final float fogDensity;
	private final boolean hasCaves;
	private EndFeature structuresFeature;
	private Biome actualBiome;

	public EndBiome(BiomeDefinition definition) {
		biome = definition.build();
		mcID = definition.getID();
		fogDensity = Configs.BIOME_CONFIG.getFloat(mcID, "fog_density", definition.getFodDensity());
		genChanceUnmutable = Configs.BIOME_CONFIG.getFloat(mcID, "generation_chance", definition.getGenChance());
		hasCaves = Configs.BIOME_CONFIG.getBoolean(mcID, "has_caves", definition.hasCaves());
		readStructureList();
	}

	public EndBiome(Identifier id, Biome biome, float fogDensity, float genChance, boolean hasCaves) {
		this.biome = biome;
		this.mcID = id;
		this.fogDensity = Configs.BIOME_CONFIG.getFloat(mcID, "fog_density", fogDensity);
		this.genChanceUnmutable = Configs.BIOME_CONFIG.getFloat(mcID, "generation_chance", genChance);
		this.hasCaves = Configs.BIOME_CONFIG.getBoolean(mcID, "has_caves", hasCaves);
		readStructureList();
	}

	public EndBiome getEdge() {
		return edge == null ? this : edge;
	}

	public void setEdge(EndBiome edge) {
		this.edge = edge;
		edge.biomeParent = this;
	}

	public int getEdgeSize() {
		return edgeSize;
	}

	public void setEdgeSize(int size) {
		edgeSize = size;
	}

	public void addSubBiome(EndBiome biome) {
		maxSubBiomeChance += biome.mutateGenChance(maxSubBiomeChance);
		biome.biomeParent = this;
		subbiomes.add(biome);
	}

	public EndBiome getSubBiome(Random random) {
		float chance = random.nextFloat() * maxSubBiomeChance;
		for (EndBiome biome : subbiomes)
			if (biome.canGenerate(chance))
				return biome;
		return this;
	}

	public EndBiome getParentBiome() {
		return this.biomeParent;
	}

	public boolean hasEdge() {
		return edge != null;
	}

	public boolean hasParentBiome() {
		return biomeParent != null;
	}

	public boolean isSame(EndBiome biome) {
		return biome == this || (biome.hasParentBiome() && biome.getParentBiome() == this);
	}

	public boolean canGenerate(float chance) {
		return chance <= this.genChance;
	}

	public float mutateGenChance(float chance) {
		genChance = genChanceUnmutable;
		genChance += chance;
		return genChance;
	}

	public Biome getBiome() {
		return biome;
	}

	@Override
	public String toString() {
		return mcID.toString();
	}

	public Identifier getID() {
		return mcID;
	}

	public float getFogDensity() {
		return fogDensity;
	}
	
	protected void readStructureList() {
		String ns = mcID.getNamespace();
		String nm = mcID.getPath();

		String path = "/data/" + ns + "/structures/biome/" + nm + "/";
		InputStream inputstream = StructureHelper.class.getResourceAsStream(path + "structures.json");
		if (inputstream != null) {
			JsonObject obj = JsonFactory.getJsonObject(inputstream);
			JsonArray enties = obj.getAsJsonArray("structures");
			if (enties != null) {
				List<StructureInfo> list = Lists.newArrayList();
				enties.forEach((entry) -> {
					JsonObject e = entry.getAsJsonObject();
					Structure structure = StructureHelper.readStructure(path + e.get("nbt").getAsString() + ".nbt");
					TerrainMerge terrainMerge = TerrainMerge.getFromString(e.get("terrainMerge").getAsString());
					int offsetY = e.get("offsetY").getAsInt();
					list.add(new StructureInfo(structure, offsetY, terrainMerge));
				});
				if (!list.isEmpty()) {
					structuresFeature = EndFeature.makeChansedFeature(nm + "_structures", new ListFeature(list), 10);
				}
			}
		}
	}
	
	public EndFeature getStructuresFeature() {
		return structuresFeature;
	}
	
	public void setActualBiome(Biome biome) {
		this.actualBiome = biome;
	}
	
	public Biome getActualBiome() {
		return this.actualBiome;
	}

	public float getGenChance() {
		return this.genChance;
	}
	
	public float getGenChanceImmutable() {
		return this.genChanceUnmutable;
	}
	
	public boolean hasCaves() {
		return hasCaves;
	}
}
