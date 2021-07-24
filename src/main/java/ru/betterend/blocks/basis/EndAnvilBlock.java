package ru.betterend.blocks.basis;

import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseAnvilBlock;
import ru.betterend.complexmaterials.MetalMaterial;

public class EndAnvilBlock extends BaseAnvilBlock {
	protected MetalMaterial metalMaterial;
	protected final int level;
	
	public EndAnvilBlock(MaterialColor color, int level) {
		super(color);
		this.level = level;
	}
	
	public EndAnvilBlock(MetalMaterial metalMaterial, MaterialColor color, int level) {
		this(color, level);
		this.metalMaterial = metalMaterial;
	}
	
	public int getCraftingLevel() {
		return level;
	}
}
