package ru.betterend.util.sdf;

import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

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
	
	public BlockState getStateUp() {
		PosInfo info = blocks.get(pos.up());
		if (info == null) {
			return AIR;
		}
		return info.getState();
	}
	
	public BlockState getStateDown() {
		PosInfo info = blocks.get(pos.down());
		if (info == null) {
			return AIR;
		}
		return info.getState();
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
