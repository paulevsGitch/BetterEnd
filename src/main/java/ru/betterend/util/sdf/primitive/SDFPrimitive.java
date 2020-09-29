package ru.betterend.util.sdf.primitive;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import ru.betterend.util.sdf.SDF;

public abstract class SDFPrimitive extends SDF {
	protected final Function<BlockPos, BlockState> placerFunction;
	
	public SDFPrimitive(Function<BlockPos, BlockState> placerFunction) {
		this.placerFunction = placerFunction;
	}
	
	public SDFPrimitive(BlockState state) {
		this.placerFunction = (pos) -> {
			return state;
		};
	}
	
	public SDFPrimitive(Block block) {
		this.placerFunction = (pos) -> {
			return block.getDefaultState();
		};
	}
	
	public BlockState getBlockState(BlockPos pos) {
		return placerFunction.apply(pos);
	}
}
