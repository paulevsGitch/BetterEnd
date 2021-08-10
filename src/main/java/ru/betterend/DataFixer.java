package ru.betterend;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import ru.bclib.api.datafixer.DataFixerAPI;
import ru.bclib.api.datafixer.Patch;
import ru.bclib.api.datafixer.PatchFunction;

import java.io.File;

public class DataFixer {
	public static void register() {
		DataFixerAPI.registerPatch(() -> {
			return new BetterEndPatch();
		});
	}
	
	private static final class BetterEndPatch extends Patch {
		protected BetterEndPatch() {
			super(BetterEnd.MOD_ID, "0.11.0");
		}
		
		/*public PatchFunction<CompoundTag, Boolean> getLevelDatPatcher() {
			return (root, profile) -> {
				CompoundTag dimensions = root.getCompound("Data").getCompound("WorldGenSettings").getCompound("dimensions");
				if (dimensions.contains("minecraft:the_end")) {
					CompoundTag biomeSource = dimensions.getCompound("minecraft:the_end").getCompound("generator").getCompound("biome_source");
					if (!biomeSource.getString("type").equals("betterend:better_end_biome_source")) {
						BetterEnd.LOGGER.info("Applying biome source patch");
						biomeSource.putString("type", "betterend:better_end_biome_source");
						return true;
					}
				}
				return false;
			};
		}*/
	}
}
