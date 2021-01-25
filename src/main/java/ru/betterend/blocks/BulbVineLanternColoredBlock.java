package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import ru.betterend.interfaces.IColorProvider;

public class BulbVineLanternColoredBlock extends BulbVineLanternBlock implements IColorProvider {
	public BulbVineLanternColoredBlock(FabricBlockSettings settings) {
		super(settings);
	}

	@Override
	public BlockColorProvider getProvider() {
		return (state, world, pos, tintIndex) -> {
			return this.getDefaultMaterialColor().color;
		};
	}

	@Override
	public ItemColorProvider getItemProvider() {
		return (stack, tintIndex) -> {
			return this.getDefaultMaterialColor().color;
		};
	}
	
	@Override
	protected String getGlowTexture() {
		return "bulb_vine_lantern_overlay";
	}
}
