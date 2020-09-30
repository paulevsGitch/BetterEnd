package ru.betterend.util.sdf.primitive;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import ru.betterend.util.sdf.SDF;

public abstract class SDFPrimitive extends SDF {
	protected Function<BlockPos, BlockState> placerFunction;
	
	public SDFPrimitive setBlock(Function<BlockPos, BlockState> placerFunction) {
		this.placerFunction = placerFunction;
		return this;
	}
	
	public SDFPrimitive setBlock(BlockState state) {
		this.placerFunction = (pos) -> {
			return state;
		};
		return this;
	}
	
	public SDFPrimitive setBlock(Block block) {
		this.placerFunction = (pos) -> {
			return block.getDefaultState();
		};
		return this;
	}
	
	public BlockState getBlockState(BlockPos pos) {
		return placerFunction.apply(pos);
	}
}
