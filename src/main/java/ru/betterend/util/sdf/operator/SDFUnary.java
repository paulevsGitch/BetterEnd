package ru.betterend.util.sdf.operator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import ru.betterend.util.sdf.ISDF;

public abstract class SDFUnary implements ISDF {
	protected ISDF source;
	
	public SDFUnary setSource(ISDF source) {
		this.source = source;
		return this;
	}
	
	@Override
	public void setBlock(ServerWorldAccess world, BlockPos pos) {
		source.setBlock(world, pos);
	}
}
