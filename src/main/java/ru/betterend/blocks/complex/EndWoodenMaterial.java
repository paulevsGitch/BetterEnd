package ru.betterend.blocks.complex;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.complexmaterials.WoodenMaterial;
import ru.betterend.BetterEnd;
import ru.betterend.config.Configs;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class EndWoodenMaterial extends WoodenMaterial {
	private Block bark;
	private Block log;
	
	public EndWoodenMaterial(String name, MaterialColor woodColor, MaterialColor planksColor) {
		super(BetterEnd.MOD_ID, name, woodColor, planksColor);
	}
	
	public EndWoodenMaterial init() {
		return (EndWoodenMaterial) super.init(EndBlocks.getBlockRegistry(), EndItems.getItemRegistry(), Configs.RECIPE_CONFIG);
	}
	
	public boolean isTreeLog(Block block) {
		return block == getLog() || block == getBark();
	}
	
	public boolean isTreeLog(BlockState state) {
		return isTreeLog(state.getBlock());
	}
	
	public Block getLog() {
		if (log == null) {
			log = getBlock("log");
		}
		return log;
	}
	
	public Block getBark() {
		if (bark == null) {
			bark = getBlock("bark");
		}
		return bark;
	}
}