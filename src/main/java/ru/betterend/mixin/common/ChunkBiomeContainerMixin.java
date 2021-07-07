package ru.betterend.mixin.common;

import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.core.BlockPos;
import net.minecraft.util.BitStorage;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import ru.betterend.BetterEnd;
import ru.betterend.integration.Integrations;
import ru.betterend.interfaces.IBiomeArray;

@Mixin(ChunkBiomeContainer.class)
public class ChunkBiomeContainerMixin implements IBiomeArray {
	@Final
	@Shadow
	private Biome[] biomes;

	@Final
	@Shadow
	private static int WIDTH_BITS;

	@Final
	@Shadow
	private static int HORIZONTAL_MASK;

	@Override
	public void be_setBiome(Biome biome, BlockPos pos) {
		int biomeX = pos.getX() >> 2;
		int biomeY = pos.getY() >> 2;
		int biomeZ = pos.getZ() >> 2;
		int index = be_getArrayIndex(biomeX, biomeY, biomeZ);
		
		if (Integrations.hasHydrogen()) {
			try {
				ChunkBiomeContainer self = (ChunkBiomeContainer) (Object) this;
				BitStorage storage = be_getHydrogenStorage(self);
				Biome[] palette = be_getHydrogenPalette(self);
				int paletteIndex = be_getHydrogenPaletteIndex(biome, palette);
				if (paletteIndex == -1) {
					Biome[] newPalette = new Biome[palette.length + 1];
					System.arraycopy(palette, 0, newPalette, 0, palette.length);
					paletteIndex = palette.length;
					palette = newPalette;
					palette[paletteIndex] = biome;
					be_setHydrogenPalette(self, palette);
				}
				try {
					storage.set(index, paletteIndex);
				}
				catch (Exception e) {
					int size = storage.getSize();
					int bits = Mth.ceillog2(palette.length);
					BitStorage newStorage = new BitStorage(bits, size);
					for (int i = 0; i < size; i++) {
						newStorage.set(i, storage.get(i));
					}
					storage = newStorage;
					storage.set(index, paletteIndex);
					be_setHydrogenStorage(self, storage);
				}
			}
			catch (Exception e) {
				BetterEnd.LOGGER.warning(e.getLocalizedMessage());
			}
			return;
		}
		
		biomes[index] = biome;
	}

	@Shadow @Final private int quartMinY;
	@Shadow @Final private int quartHeight;

	private int be_getArrayIndex(int biomeX, int biomeY, int biomeZ) {
		int i = biomeX & HORIZONTAL_MASK;
		int j = Mth.clamp(biomeY - this.quartMinY, 0, this.quartHeight);
		int k = biomeZ & HORIZONTAL_MASK;
		return j << WIDTH_BITS + WIDTH_BITS | k << WIDTH_BITS | i;
	}
	
	private Field be_getField(String name) throws Exception {
		Field field = ChunkBiomeContainer.class.getDeclaredField(name);
		field.setAccessible(true);
		return field;
	}
	
	private BitStorage be_getHydrogenStorage(ChunkBiomeContainer container) throws Exception {
		return (BitStorage) be_getField("intArray").get(container);
	}
	
	private Biome[] be_getHydrogenPalette(ChunkBiomeContainer container) throws Exception {
		return (Biome[]) be_getField("palette").get(container);
	}
	
	private int be_getHydrogenPaletteIndex(Biome biome, Biome[] palette) {
		int index = -1;
		for (int i = 0; i < palette.length; i++) {
			if (palette[i] == biome) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	private void be_setHydrogenPalette(ChunkBiomeContainer container, Biome[] palette) throws Exception {
		be_getField("palette").set(container, palette);
	}
	
	private void be_setHydrogenStorage(ChunkBiomeContainer container, BitStorage storage) throws Exception {
		be_getField("intArray").set(container, storage);
	}
}
