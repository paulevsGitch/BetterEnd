package ru.betterend.util.sdf;

import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PosInfo {
	private static final BlockState AIR = Blocks.AIR.getDefaultState();
	private final Map<BlockPos, PosInfo> blocks;
	private final BlockPos pos;
	private BlockState state;
	
	public static PosInfo create(Map<BlockPos, PosInfo> blocks, BlockPos pos) {
		return new PosInfo(blocks, pos);
	}
	
	private PosInfo(Map<BlockPos, PosInfo> blocks, BlockPos pos) {
		this.blocks = blocks;
		this.pos = pos;
		blocks.put(pos, this);
	}
	
	public BlockState getState() {
		return state;
	}
	
	public void setState(BlockState state) {
		this.state = state;
	}
	
	public BlockState getState(Direction dir) {
		PosInfo info = blocks.get(pos.offset(dir));
		if (info == null) {
			return AIR;
		}
		return info.getState();
	}
	
	public BlockState getStateUp() {
		return getState(Direction.UP);
	}
	
	public BlockState getStateDown() {
		return getState(Direction.DOWN);
	}
	
	public int hashCode() {
		return pos.hashCode();
	}
	
	public boolean equals(Object obj) {
		if (!(obj instanceof PosInfo)) {
			return false;
		}
		return pos.equals(((PosInfo) obj).pos);
	}
}
