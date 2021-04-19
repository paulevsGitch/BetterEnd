package ru.betterend.util.sdf.primitive;

import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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
			return block.defaultBlockState();
		};
		return this;
	}
	
	public BlockState getBlockState(BlockPos pos) {
		return placerFunction.apply(pos);
	}
	
	/*public abstract CompoundTag toNBT(CompoundTag root) {
		
	}*/
}
