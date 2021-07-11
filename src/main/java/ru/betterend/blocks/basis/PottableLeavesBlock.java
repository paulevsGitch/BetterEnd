package ru.betterend.blocks.basis;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseLeavesBlock;
import ru.betterend.interfaces.PottablePlant;

public class PottableLeavesBlock extends BaseLeavesBlock implements PottablePlant {
	private Block sapling;
	
	public PottableLeavesBlock(Block sapling, MaterialColor color) {
		super(sapling, color);
		this.sapling = sapling;
	}
	
	public PottableLeavesBlock(Block sapling, MaterialColor color, int light) {
		super(sapling, color, light);
		this.sapling = sapling;
	}
	
	@Override
	public boolean canPlantOn(Block block) {
		if (sapling instanceof PottablePlant) {
			return ((PottablePlant) sapling).canPlantOn(block);
		}
		return true;
	}
}
