package ru.betterend.world.biome;

import java.util.function.BiFunction;

import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.biome.Biome;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.interfaces.StructureFeaturesAccessor;
import ru.bclib.world.biomes.BCLBiome;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndSounds;

public class EndBiome extends BCLBiome {
	public abstract static class Config {
		protected static final StructureFeaturesAccessor VANILLA_FEATURES = (StructureFeaturesAccessor)new StructureFeatures();

		public final ResourceLocation ID;

		protected Config(String name) {
			this.ID = BetterEnd.makeID(name);
		}

		protected abstract void addCustomBuildData(BCLBiomeBuilder builder);

		public BiFunction<ResourceLocation, Biome, EndBiome> getSupplier(){
			return EndBiome::new;
		}

		protected boolean hasCaves(){
			return true;
		}
	}

	public EndBiome(ResourceLocation biomeID, Biome biome) {
		super(biomeID, biome);
	}

	public static EndBiome create(Config biomeConfig){
		BCLBiomeBuilder builder = BCLBiomeBuilder
				.start(biomeConfig.ID)
				.category(Biome.BiomeCategory.THEEND)
				.music(SoundEvents.MUSIC_END)
				.waterColor(4159204)
				.waterFogColor(329011)
				.fogColor(0xA080A0)
				.skyColor(0)
				.mood(EndSounds.AMBIENT_DUST_WASTELANDS)
				.temperature(0.5f)
				.wetness(0.5f)
				.precipitation(Biome.Precipitation.NONE);

		biomeConfig.addCustomBuildData(builder);
		EndFeatures.addDefaultFeatures(builder, biomeConfig.hasCaves());

		EndBiome biome = builder.build(biomeConfig.getSupplier());
		biome.addCustomData("has_caves", biomeConfig.hasCaves());

		return biome;
	}
}
