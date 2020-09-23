package ru.betterend.world.biome;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;

public class EndBiome {
	protected List<Subbiome> subbiomes = Lists.newArrayList();

	protected final Biome biome;
	protected final Identifier mcID;
	protected EndBiome edge;
	protected int edgeSize;

	protected EndBiome biomeParent;
	protected float maxSubBiomeChance = 1;
	protected float genChance = 1;

	private final float fogDensity;

	public EndBiome(BiomeDefinition definition) {
		biome = definition.build();
		mcID = definition.getID();
		fogDensity = definition.getFodDensity();
	}

	public EndBiome(Biome biome) {
		this.biome = biome;
		mcID = BuiltinRegistries.BIOME.getId(biome);
		fogDensity = 1;
	}

	public void genSurfColumn(WorldAccess world, BlockPos pos, Random random) {
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

	public void addSubBiome(EndBiome biome, float chance) {
		maxSubBiomeChance += chance;
		biome.biomeParent = this;
		subbiomes.add(new Subbiome(biome, maxSubBiomeChance));
	}

	public EndBiome getSubBiome(Random random) {
		float chance = random.nextFloat() * maxSubBiomeChance;
		for (Subbiome biome : subbiomes)
			if (biome.canGenerate(chance))
				return biome.biome;
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

	protected final class Subbiome {
		EndBiome biome;
		float chance;

		Subbiome(EndBiome biome, float chance) {
			this.biome = biome;
			this.chance = chance;
		}

		public boolean canGenerate(float chance) {
			return chance < this.chance;
		}
	}

	public boolean canGenerate(float chance) {
		return chance <= this.genChance;
	}

	public float setGenChance(float chance) {
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
}
