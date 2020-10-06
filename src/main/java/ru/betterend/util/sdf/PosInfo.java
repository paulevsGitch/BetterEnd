package ru.betterend.util.sdf;

import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class PosInfo implements Comparable<PosInfo> {
	private static final BlockState AIR = Blocks.AIR.getDefaultState();
	private final Map<BlockPos, PosInfo> blocks;
	private final Map<BlockPos, PosInfo> add;
	private final BlockPos pos;
	private BlockState state;
	
	public static PosInfo create(Map<BlockPos, PosInfo> blocks, Map<BlockPos, PosInfo> add, BlockPos pos) {
		return new PosInfo(blocks, add, pos);
	}
	
	private PosInfo(Map<BlockPos, PosInfo> blocks, Map<BlockPos, PosInfo> add, BlockPos pos) {
		this.blocks = blocks;
		this.add = add;
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
			info = add.get(pos.offset(dir));
			return info == null ? AIR : info.getState();
		}
		return info.getState();
	}
	
	public BlockState getState(Direction dir, int distance) {
		PosInfo info = blocks.get(pos.offset(dir, distance));
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
	
	@Override
	public int hashCode() {
		return pos.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PosInfo)) {
			return false;
		}
		return pos.equals(((PosInfo) obj).pos);
	}

	@Override
	public int compareTo(PosInfo info) {
		return this.pos.getY() - info.pos.getY();
	}

	public BlockPos getPos() {
		return pos;
	}
	
	public void setBlockPos(BlockPos pos, BlockState state) {
		PosInfo info = new PosInfo(blocks, add, pos);
		info.state = state;
		add.put(pos, info);
	}
}
