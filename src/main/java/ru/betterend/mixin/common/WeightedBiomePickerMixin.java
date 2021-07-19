package ru.betterend.mixin.common;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.impl.biome.InternalBiomeData;
import net.fabricmc.fabric.impl.biome.WeightedBiomePicker;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.betterend.interfaces.IBiomeList;

import java.util.List;

@Mixin(value = WeightedBiomePicker.class, remap = false)
public class WeightedBiomePickerMixin implements IBiomeList {
	private final List<ResourceKey<Biome>> biomes = Lists.newArrayList();
	
	@Inject(method = "addBiome", at = @At("TAIL"))
	private void be_addBiome(final ResourceKey<Biome> biome, final double weight, CallbackInfo info) {
		if (be_isCorrectPicker(WeightedBiomePicker.class.cast(this))) {
			biomes.add(biome);
		}
	}
	
	@Override
	public List<ResourceKey<Biome>> getBiomes() {
		return biomes;
	}
	
	private boolean be_isCorrectPicker(WeightedBiomePicker picker) {
		return picker == InternalBiomeData.getEndBiomesMap()
										  .get(Biomes.SMALL_END_ISLANDS) || picker == InternalBiomeData.getEndBarrensMap()
																									   .get(Biomes.END_BARRENS);
	}
}
