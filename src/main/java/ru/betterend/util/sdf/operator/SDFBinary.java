package ru.betterend.util.sdf.operator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.util.sdf.SDF;

public abstract class SDFBinary extends SDF {
	protected SDF sourceA;
	protected SDF sourceB;
	protected boolean firstValue;
	
	public SDFBinary setSourceA(SDF sourceA) {
		this.sourceA = sourceA;
		return this;
	}
	
	public SDFBinary setSourceB(SDF sourceB) {
		this.sourceB = sourceB;
		return this;
	}
	
	protected void selectValue(float a, float b) {
		firstValue = a < b;
	}
	
	@Override
	public BlockState getBlockState(BlockPos pos) {
		if (firstValue) {
			return sourceA.getBlockState(pos);
		} else {
			return sourceB.getBlockState(pos);
		}
	}
}
