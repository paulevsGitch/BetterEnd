package ru.betterend.util.sdf.operator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import ru.betterend.util.sdf.SDF;

public abstract class SDFUnary extends SDF {
	protected SDF source;
	
	public SDFUnary setSource(SDF source) {
		this.source = source;
		return this;
	}
	
	@Override
	public BlockState getBlockState(BlockPos pos) {
		return source.getBlockState(pos);
	}
}
