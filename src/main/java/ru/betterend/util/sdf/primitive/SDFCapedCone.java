package ru.betterend.util.sdf.primitive;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ru.betterend.util.MHelper;

public class SDFCapedCone extends SDFPrimitive {
	private float radius1;
	private float radius2;
	private float height;
	
	public SDFCapedCone(Function<BlockPos, BlockState> placerFunction) {
		super(placerFunction);
	}
	
	public SDFCapedCone(BlockState state) {
		super(state);
	}
	
	public SDFCapedCone(Block block) {
		super(block);
	}
	
	public SDFCapedCone setRadius1(float radius) {
		this.radius1 = radius;
		return this;
	}
	
	public SDFCapedCone setRadius2(float radius) {
		this.radius2 = radius;
		return this;
	}
	
	public SDFCapedCone setHeight(float height) {
		this.height = height;
		return this;
	}

	@Override
	public float getDistance(float x, float y, float z) {
		float qx = MHelper.length(x, z);
		float k2x = radius2 - radius1;
		float k2y = 2 * height;
		float cax = qx - MHelper.min(qx, (y < 0F) ? radius1 : radius2);
		float cay = Math.abs(y) - height;
		float mlt = MathHelper.clamp(MHelper.dot(radius2 - qx, height - y, k2x, k2y) / MHelper.dot(k2x, k2y, k2x, k2y), 0F, 1F);
		float cbx = qx - radius2 + k2x * mlt;
		float cby = y - height + k2y * mlt;
		float s = (cbx < 0F && cay < 0F) ? -1F : 1F;
		return s * (float) Math.sqrt(MHelper.min(MHelper.dot(cax, cay, cax, cay), MHelper.dot(cbx, cby, cbx, cby)));
	}
}
