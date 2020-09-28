package ru.betterend.util.sdf.primitive;

import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ru.betterend.util.MHelper;

public class SDFCapsule extends SDFPrimitive {
	private float radius;
	private float height;
	
	public SDFCapsule(Function<BlockPos, BlockState> placerFunction) {
		super(placerFunction);
	}
	
	public SDFCapsule(BlockState state) {
		super(state);
	}
	
	public SDFCapsule setRadius(float radius) {
		this.radius = radius;
		return this;
	}
	
	public SDFCapsule setHeight(float height) {
		this.height = height;
		return this;
	}
	
	@Override
	public float getDistance(float x, float y, float z) {
		return MHelper.length(x, y - MathHelper.clamp(y, 0, height), z) - radius;
	}
}
