package ru.betterend.blocks.basis;

import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.LeveledAnvilBlock;
import ru.betterend.complexmaterials.MetalMaterial;

public class EndAnvilBlock extends LeveledAnvilBlock {
	protected MetalMaterial metalMaterial;
	
	public EndAnvilBlock(MaterialColor color, int level) {
		super(color, level);
	}
	
	public EndAnvilBlock(MetalMaterial metalMaterial, MaterialColor color, int level) {
		super(color, level);
		this.metalMaterial = metalMaterial;
	}
	
	public int getCraftingLevel() {
		return level;
	}
}
