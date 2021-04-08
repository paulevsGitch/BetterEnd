package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class BulbVineLanternColoredBlock extends BulbVineLanternBlock implements IColorProvider {
	public BulbVineLanternColoredBlock(FabricBlockSettings settings) {
		super(settings);
	}

	@Override
	public BlockColor getBlockProvider() {
		return (state, world, pos, tintIndex) -> {
			return getColor();
		};
	}

	@Override
	public ItemColor getItemProvider() {
		return (stack, tintIndex) -> {
			return getColor();
		};
	}

	private int getColor() {
		int color = BlocksHelper.getBlockColor(this);
		int b = (color & 255);
		int g = ((color >> 8) & 255);
		int r = ((color >> 16) & 255);
		float[] hsv = MHelper.fromRGBtoHSB(r, g, b);
		return MHelper.fromHSBtoRGB(hsv[0], hsv[1], hsv[1] > 0.2 ? 1 : hsv[2]);
	}

	@Override
	protected String getGlowTexture() {
		return "bulb_vine_lantern_overlay";
	}
}
