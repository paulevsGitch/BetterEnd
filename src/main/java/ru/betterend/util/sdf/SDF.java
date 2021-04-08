package ru.betterend.util.sdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.core.Direction;
import net.minecraft.world.ServerWorldAccess;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.structures.StructureWorld;

public abstract class SDF {
	private List<Function<PosInfo, BlockState>> postProcesses = Lists.newArrayList();
	private Function<BlockState, Boolean> canReplace = (state) -> {
		return state.getMaterial().isReplaceable();
	};

	public abstract float getDistance(float x, float y, float z);

	public abstract BlockState getBlockState(BlockPos pos);

	public SDF addPostProcess(Function<PosInfo, BlockState> postProcess) {
		this.postProcesses.add(postProcess);
		return this;
	}

	public SDF setReplaceFunction(Function<BlockState, Boolean> canReplace) {
		this.canReplace = canReplace;
		return this;
	}

	public void fillRecursive(ServerWorldAccess world, BlockPos start) {
		Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
		Map<BlockPos, PosInfo> addInfo = Maps.newHashMap();
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> ends = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		ends.add(new BlockPos(0, 0, 0));
		boolean run = true;

		MutableBlockPos bPos = new MutableBlockPos();

		while (run) {
			for (BlockPos center : ends) {
				for (Direction dir : Direction.values()) {
					bPos.set(center).move(dir);
					BlockPos wpos = bPos.add(start);

					if (!blocks.contains(bPos) && canReplace.apply(world.getBlockState(wpos))) {
						if (this.getDistance(bPos.getX(), bPos.getY(), bPos.getZ()) < 0) {
							BlockState state = getBlockState(wpos);
							PosInfo.create(mapWorld, addInfo, wpos).setState(state);
							add.add(bPos.toImmutable());
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

		List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
		if (infos.size() > 0) {
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
			});

			infos.clear();
			infos.addAll(addInfo.values());
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				if (canReplace.apply(world.getBlockState(info.getPos()))) {
					BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
				}
			});
		}
	}

	public void fillArea(ServerWorldAccess world, BlockPos center, Box box) {
		Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
		Map<BlockPos, PosInfo> addInfo = Maps.newHashMap();

		MutableBlockPos mut = new MutableBlockPos();
		for (int y = (int) box.minY; y <= box.maxY; y++) {
			mut.setY(y);
			for (int x = (int) box.minX; x <= box.maxX; x++) {
				mut.setX(x);
				for (int z = (int) box.minZ; z <= box.maxZ; z++) {
					mut.setZ(z);
					if (canReplace.apply(world.getBlockState(mut))) {
						BlockPos fpos = mut.subtract(center);
						if (this.getDistance(fpos.getX(), fpos.getY(), fpos.getZ()) < 0) {
							PosInfo.create(mapWorld, addInfo, mut.toImmutable()).setState(getBlockState(mut));
						}
					}
				}
			}
		}

		List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
		if (infos.size() > 0) {
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
			});

			infos.clear();
			infos.addAll(addInfo.values());
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				if (canReplace.apply(world.getBlockState(info.getPos()))) {
					BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
				}
			});
		}
	}

	public void fillRecursiveIgnore(ServerWorldAccess world, BlockPos start, Function<BlockState, Boolean> ignore) {
		Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
		Map<BlockPos, PosInfo> addInfo = Maps.newHashMap();
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> ends = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		ends.add(new BlockPos(0, 0, 0));
		boolean run = true;

		MutableBlockPos bPos = new MutableBlockPos();

		while (run) {
			for (BlockPos center : ends) {
				for (Direction dir : Direction.values()) {
					bPos.set(center).move(dir);
					BlockPos wpos = bPos.add(start);
					BlockState state = world.getBlockState(wpos);
					boolean ign = ignore.apply(state);
					if (!blocks.contains(bPos) && (ign || canReplace.apply(state))) {
						if (this.getDistance(bPos.getX(), bPos.getY(), bPos.getZ()) < 0) {
							PosInfo.create(mapWorld, addInfo, wpos).setState(ign ? state : getBlockState(bPos));
							add.add(bPos.toImmutable());
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

		List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
		if (infos.size() > 0) {
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
			});

			infos.clear();
			infos.addAll(addInfo.values());
			Collections.sort(infos);
			postProcesses.forEach((postProcess) -> {
				infos.forEach((info) -> {
					info.setState(postProcess.apply(info));
				});
			});
			infos.forEach((info) -> {
				if (canReplace.apply(world.getBlockState(info.getPos()))) {
					BlocksHelper.setWithoutUpdate(world, info.getPos(), info.getState());
				}
			});
		}
	}

	public void fillRecursive(StructureWorld world, BlockPos start) {
		Map<BlockPos, PosInfo> mapWorld = Maps.newHashMap();
		Map<BlockPos, PosInfo> addInfo = Maps.newHashMap();
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> ends = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		ends.add(new BlockPos(0, 0, 0));
		boolean run = true;

		MutableBlockPos bPos = new MutableBlockPos();

		while (run) {
			for (BlockPos center : ends) {
				for (Direction dir : Direction.values()) {
					bPos.set(center).move(dir);
					BlockPos wpos = bPos.add(start);

					if (!blocks.contains(bPos)) {
						if (this.getDistance(bPos.getX(), bPos.getY(), bPos.getZ()) < 0) {
							BlockState state = getBlockState(wpos);
							PosInfo.create(mapWorld, addInfo, wpos).setState(state);
							add.add(bPos.toImmutable());
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

		List<PosInfo> infos = new ArrayList<PosInfo>(mapWorld.values());
		Collections.sort(infos);
		postProcesses.forEach((postProcess) -> {
			infos.forEach((info) -> {
				info.setState(postProcess.apply(info));
			});
		});
		infos.forEach((info) -> {
			world.setBlock(info.getPos(), info.getState());
		});

		infos.clear();
		infos.addAll(addInfo.values());
		Collections.sort(infos);
		postProcesses.forEach((postProcess) -> {
			infos.forEach((info) -> {
				info.setState(postProcess.apply(info));
			});
		});
		infos.forEach((info) -> {
			world.setBlock(info.getPos(), info.getState());
		});
	}

	public Set<BlockPos> getPositions(ServerWorldAccess world, BlockPos start) {
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> ends = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		ends.add(new BlockPos(0, 0, 0));
		boolean run = true;

		MutableBlockPos bPos = new MutableBlockPos();

		while (run) {
			for (BlockPos center : ends) {
				for (Direction dir : Direction.values()) {
					bPos.set(center).move(dir);
					BlockPos wpos = bPos.add(start);
					BlockState state = world.getBlockState(wpos);
					if (!blocks.contains(wpos) && canReplace.apply(state)) {
						if (this.getDistance(bPos.getX(), bPos.getY(), bPos.getZ()) < 0) {
							add.add(bPos.toImmutable());
						}
					}
				}
			}

			ends.forEach((end) -> blocks.add(end.add(start)));
			ends.clear();
			ends.addAll(add);
			add.clear();

			run &= !ends.isEmpty();
		}

		return blocks;
	}
}
