package ru.betterend.util.sdf.primitive;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import ru.betterend.util.MHelper;

public class SDFSphere extends SDFPrimitive {
	private float radius;
	
	public SDFSphere(Function<BlockPos, BlockState> placerFunction) {
		super(placerFunction);
	}
	
	public SDFSphere(BlockState state) {
		super(state);
	}
	
	public SDFSphere(Block block) {
		super(block);
	}
	
	public SDFSphere setRadius(float radius) {
		this.radius =  radius;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		return MHelper.length(x, y, z) - radius;
	}
}
