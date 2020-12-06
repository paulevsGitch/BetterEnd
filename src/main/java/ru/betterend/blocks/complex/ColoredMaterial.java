package ru.betterend.blocks.complex;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.DyeItem;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;
import ru.betterend.recipe.builders.GridRecipe;
import ru.betterend.registry.EndBlocks;

public class ColoredMaterial {
	private final Map<DyeColor, Block> colors = Maps.newEnumMap(DyeColor.class);
	
	public ColoredMaterial(Function<FabricBlockSettings, Block> constructor, Block source, boolean craftEight) {
		String id = Registry.BLOCK.getId(source).getPath();
		for (DyeColor color: DyeColor.values()) {
			Block block = constructor.apply(FabricBlockSettings.copyOf(source).materialColor(color));
			String blockName = id + "_" + color.getName();
			EndBlocks.registerBlock(blockName, block);
			if (craftEight) {
				GridRecipe.make(blockName, block).setOutputCount(8).setShape("###", "#D#", "###").addMaterial('#', source).addMaterial('D', DyeItem.byColor(color)).build();
			}
			else {
				GridRecipe.make(blockName, block).setList("#D").addMaterial('#', source).addMaterial('D', DyeItem.byColor(color)).build();
			}
			colors.put(color, block);
		}
	}	
	
	public Block getByColor(DyeColor color) {
		return colors.get(color);
	}
}
