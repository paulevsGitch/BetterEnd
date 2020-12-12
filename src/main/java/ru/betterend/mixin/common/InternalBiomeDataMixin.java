package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.impl.biome.InternalBiomeData;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndBiomes;

@Mixin(InternalBiomeData.class)
public class InternalBiomeDataMixin {
	@Inject(method = "addEndBiomeReplacement", at = @At("TAIL"))
	private static void beAddIslandsBiomeFromFabric(RegistryKey<Biome> replaced, RegistryKey<Biome> variant, double weight, CallbackInfo info) {
		if (replaced.equals(BiomeKeys.SMALL_END_ISLANDS)) {
			Biome biome = BuiltinRegistries.BIOME.get(variant);
			Identifier id = BuiltinRegistries.BIOME.getId(biome);
			if (!id.getNamespace().equals(BetterEnd.MOD_ID)) {
				EndBiomes.FABRIC_VOID.add(id);
				if (BetterEnd.isDevEnvironment()) {
					System.out.println("Added " + id + " from Fabric small islands biome map");
				}
			}
		}
	}
}
