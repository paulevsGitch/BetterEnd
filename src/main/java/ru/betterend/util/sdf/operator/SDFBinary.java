package ru.betterend.util.sdf.operator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import ru.betterend.util.sdf.ISDF;

public abstract class SDFBinary implements ISDF {
	protected ISDF sourceA;
	protected ISDF sourceB;
	protected boolean firstValue;
	
	public SDFBinary setSourceA(ISDF sourceA) {
		this.sourceA = sourceA;
		return this;
	}
	
	public SDFBinary setSourceB(ISDF sourceB) {
		this.sourceB = sourceB;
		return this;
	}
	
	protected void selectValue(float a, float b) {
		firstValue = a < b;
	}
	
	@Override
	public void setBlock(ServerWorldAccess world, BlockPos pos) {
		if (firstValue) {
			sourceA.setBlock(world, pos);
		} else {
			sourceB.setBlock(world, pos);
		}
	}
}
