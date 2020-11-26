package ru.betterend.blocks;

import java.io.Reader;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.util.Identifier;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class BlockBulbVineLanternColored extends BlockBulbVineLantern implements IColorProvider, BlockPatterned {
	private final BlockColorProvider blockProvider;
	private final ItemColorProvider itemProvider;
	
	public BlockBulbVineLanternColored(FabricBlockSettings settings) {
		super(settings);
		
		blockProvider = (state, world, pos, tintIndex) -> {
			return this.getDefaultMaterialColor().color;
		};
		
		itemProvider = (stack, tintIndex) -> {
			return this.getDefaultMaterialColor().color;
		};
	}

	@Override
	public BlockColorProvider getProvider() {
		return blockProvider;
	}

	@Override
	public ItemColorProvider getItemProvider() {
		return itemProvider;
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String path = "betterend:block/bulb_lantern_colored";
		return Patterns.createJson(data, path, path);
	}
	
	@Override
	public String getModelPattern(String block) {
		return Patterns.createJson(Patterns.BLOCK_BULB_LANTERN_COLORED, "", "");
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_DIRECT;
	}
}
