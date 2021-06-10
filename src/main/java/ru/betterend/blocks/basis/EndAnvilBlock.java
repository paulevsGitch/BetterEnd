package ru.betterend.blocks.basis;

import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseAnvilBlock;

public class EndAnvilBlock extends BaseAnvilBlock {

	protected final int level;
	
	public EndAnvilBlock(MaterialColor color, int level) {
		super(color);
		this.level = level;
	}
	
	public int getCraftingLevel() {
		return level;
	}
}
