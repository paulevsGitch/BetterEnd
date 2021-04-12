package ru.betterend.integration;

import java.awt.Color;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.world.level.ItemLike;
import ru.betterend.blocks.HydraluxPetalColoredBlock;
import ru.betterend.blocks.complex.ColoredMaterial;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

public class FlamboyantRefabricatedIntegration extends ModIntegration {
	public FlamboyantRefabricatedIntegration() {
		super("flamboyant");
	}

	@Override
	public void register() {
		/*
		 * Class<?> fDyeColor =
		 * getClass("com.github.EltrutCo.flamboyant.items.FDyeColor"); Object[] values =
		 * getStaticFieldValue(fDyeColor, "VALUES");
		 * 
		 * if (values == null) { return; }
		 */

		Map<Integer, String> colors = Maps.newHashMap();
		Map<Integer, ItemLike> dyes = Maps.newHashMap();
		/*
		 * for (Object val: values) { Integer color = (Integer) getFieldValue(fDyeColor,
		 * "signColor", val); String name = (String) getFieldValue(fDyeColor, "name",
		 * val); if (color != null && name != null) { colors.put(color, name);
		 * System.out.println(name + " " + color + " " + new Color(color));
		 * dyes.put(color, getItem(name + "_dye")); } }
		 */

		addColor("fead1d", "amber", colors, dyes);
		addColor("bd9a5f", "beige", colors, dyes);
		addColor("edeada", "cream", colors, dyes);
		addColor("33430e", "dark_green", colors, dyes);
		addColor("639920", "forest_green", colors, dyes);
		addColor("f0618c", "hot_pink", colors, dyes);
		addColor("491c7b", "indigo", colors, dyes);
		addColor("65291b", "maroon", colors, dyes);
		addColor("2c3969", "navy", colors, dyes);
		addColor("827c17", "olive", colors, dyes);
		addColor("7bc618", "pale_green", colors, dyes);
		addColor("f4a4bd", "pale_pink", colors, dyes);
		addColor("f8d45a", "pale_yellow", colors, dyes);
		addColor("6bb1cf", "sky_blue", colors, dyes);
		addColor("6e8c9c", "slate_gray", colors, dyes);
		addColor("b02454", "violet", colors, dyes);

		new ColoredMaterial(HydraluxPetalColoredBlock::new, EndBlocks.HYDRALUX_PETAL_BLOCK, colors, dyes, true);
	}

	private void addColor(String hex, String name, Map<Integer, String> colors, Map<Integer, ItemLike> dyes) {
		int color = MHelper.color(hex);
		colors.put(color, name);
		dyes.put(color, getItem(name + "_dye"));

		System.out.println(name + " " + color + " " + new Color(color));
	}
}
