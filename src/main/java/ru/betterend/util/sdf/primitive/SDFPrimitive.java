package ru.betterend.util.sdf.primitive;

import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.sdf.ISDF;

public abstract class SDFPrimitive implements ISDF {
	protected final Function<BlockPos, BlockState> placerFunction;
	
	public SDFPrimitive(Function<BlockPos, BlockState> placerFunction) {
		this.placerFunction = placerFunction;
	}
	
	public SDFPrimitive(BlockState state) {
		this.placerFunction = (pos) -> {
			return state;
		};
	}
	
	public void setBlock(ServerWorldAccess world, BlockPos pos) {
		BlocksHelper.setWithoutUpdate(world, pos, placerFunction.apply(pos));
	}
}
