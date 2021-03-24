package ru.betterend.blocks.complex;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;
import ru.betterend.recipe.builders.GridRecipe;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class ColoredMaterial {
	private static final Map<Integer, ItemConvertible> DYES = Maps.newHashMap();
	private static final Map<Integer, String> COLORS = Maps.newHashMap();
	private final Map<Integer, Block> colors = Maps.newHashMap();
	
	public ColoredMaterial(Function<FabricBlockSettings, Block> constructor, Block source, boolean craftEight) {
		this(constructor, source, COLORS, DYES, craftEight);
	}
	
	public ColoredMaterial(Function<FabricBlockSettings, Block> constructor, Block source, Map<Integer, String> colors, Map<Integer, ItemConvertible> dyes, boolean craftEight) {
		String id = Registry.BLOCK.getId(source).getPath();
		colors.forEach((color, name) -> {
			String blockName = id + "_" + name;
			Block block = constructor.apply(FabricBlockSettings.copyOf(source).materialColor(MaterialColor.BLACK));
			EndBlocks.registerBlock(blockName, block);
			if (craftEight) {
				GridRecipe.make(blockName, block).setOutputCount(8).setShape("###", "#D#", "###").addMaterial('#', source).addMaterial('D', dyes.get(color)).build();
			}
			else {
				GridRecipe.make(blockName, block).setList("#D").addMaterial('#', source).addMaterial('D', dyes.get(color)).build();
			}
			this.colors.put(color, block);
			BlocksHelper.addBlockColor(block, color);
		});
	}
	
	public Block getByColor(DyeColor color) {
		return colors.get(color.getMaterialColor().color);
	}
	
	public Block getByColor(int color) {
		return colors.get(color);
	}
	
	static {
		for (DyeColor color: DyeColor.values()) {
			int colorRGB = color.getMaterialColor().color;
			COLORS.put(colorRGB, color.getName());
			DYES.put(colorRGB, DyeItem.byColor(color));
		}
	}
}
