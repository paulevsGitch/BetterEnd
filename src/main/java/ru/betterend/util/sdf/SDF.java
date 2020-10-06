package ru.betterend.util.sdf;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ServerWorldAccess;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.structures.StructureWorld;

public abstract class SDF {
	private Function<PosInfo, BlockState> postProcess = (info) -> {
		return info.getState();
	};
	private Function<BlockState, Boolean> canReplace = (state) -> {
		return state.getMaterial().isReplaceable();
	};

	public abstract float getDistance(float x, float y, float z);
	
	public abstract BlockState getBlockState(BlockPos pos);
	
	public SDF setPostProcess(Function<PosInfo, BlockState> postProcess) {
		this.postProcess = postProcess;
		return this;
	}
	
	public SDF setReplaceFunction(Function<BlockState, Boolean> canReplace) {
		this.canReplace = canReplace;
		return this;
	}
	
	public Set<BlockPos> fillRecursive(ServerWorldAccess world, BlockPos start, int dx, int dy, int dz) {
		Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> ends = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		ends.add(new BlockPos(0, 0, 0));
		boolean run = true;
		
		while (run) {
			for (BlockPos center: ends) {
				for (Direction dir: Direction.values()) {
					BlockPos pos = center.offset(dir);
					BlockPos wpos = pos.add(start);
					
					run &= Math.abs(pos.getX()) < dx;
					run &= Math.abs(pos.getY()) < dy;
					run &= Math.abs(pos.getZ()) < dz;
					
					if (!blocks.contains(pos) && canReplace.apply(world.getBlockState(wpos))) {
						if (this.getDistance(pos.getX(), pos.getY(), pos.getZ()) < 0) {
							BlockState state = getBlockState(wpos);
							PosInfo.create(mapWorld, wpos).setState(state);
							if (Math.abs(pos.getX()) < dx && Math.abs(pos.getY()) < dy && Math.abs(pos.getZ()) < dz) {
								add.add(pos);
							}
						}
					}
				}
			}
			
			blocks.addAll(ends);
			ends.clear();
			ends.addAll(add);
			add.clear();
			
			run &= !ends.isEmpty();
		}
		
		mapWorld.forEach((pos, info) -> {
			BlockState state = postProcess.apply(info);
			BlocksHelper.setWithoutUpdate(world, pos, state);
		});
		
		return mapWorld.keySet();
	}
	
	public Set<BlockPos> fillRecursive(ServerWorldAccess world, BlockPos start) {
		Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> ends = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		ends.add(new BlockPos(0, 0, 0));
		boolean run = true;
		
		while (run) {
			for (BlockPos center: ends) {
				for (Direction dir: Direction.values()) {
					BlockPos pos = center.offset(dir);
					BlockPos wpos = pos.add(start);
					
					if (!blocks.contains(pos) && canReplace.apply(world.getBlockState(wpos))) {
						if (this.getDistance(pos.getX(), pos.getY(), pos.getZ()) < 0) {
							BlockState state = getBlockState(wpos);
							PosInfo.create(mapWorld, wpos).setState(state);
							add.add(pos);
						}
					}
				}
			}
			
			blocks.addAll(ends);
			ends.clear();
			ends.addAll(add);
			add.clear();
			
			run &= !ends.isEmpty();
		}
		
		mapWorld.forEach((pos, info) -> {
			BlockState state = postProcess.apply(info);
			BlocksHelper.setWithoutUpdate(world, pos, state);
		});
		
		return mapWorld.keySet();
	}
	
	public Set<BlockPos> fillRecursive(StructureWorld world, BlockPos start) {
		Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> ends = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		ends.add(new BlockPos(0, 0, 0));
		boolean run = true;
		
		while (run) {
			for (BlockPos center: ends) {
				for (Direction dir: Direction.values()) {
					BlockPos pos = center.offset(dir);
					BlockPos wpos = pos.add(start);
					
					if (!blocks.contains(pos)) {
						if (this.getDistance(pos.getX(), pos.getY(), pos.getZ()) < 0) {
							BlockState state = getBlockState(wpos);
							PosInfo.create(mapWorld, wpos).setState(state);
							add.add(pos);
						}
					}
				}
			}
			
			blocks.addAll(ends);
			ends.clear();
			ends.addAll(add);
			add.clear();
			
			run &= !ends.isEmpty();
		}
		
		mapWorld.forEach((pos, info) -> {
			BlockState state = postProcess.apply(info);
			world.setBlock(pos, state);
		});
		
		return mapWorld.keySet();
	}
}
