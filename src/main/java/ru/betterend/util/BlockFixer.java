package ru.betterend.util;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.blocks.BaseDoublePlantBlock;
import ru.bclib.blocks.BaseVineBlock;
import ru.bclib.blocks.StalactiteBlock;
import ru.bclib.util.BlocksHelper;
import ru.betterend.blocks.BlueVineBlock;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.registry.EndBlocks;

import java.util.Set;
import java.util.stream.IntStream;

public class BlockFixer {
	private static final BlockState AIR = Blocks.AIR.defaultBlockState();
	private static final BlockState WATER = Blocks.WATER.defaultBlockState();
	
	public static void fixBlocks(LevelAccessor world, BlockPos start, BlockPos end) {
		Set<BlockPos> doubleCheck = Sets.newConcurrentHashSet();
		int dx = end.getX() - start.getX() + 1;
		int dz = end.getZ() - start.getZ() + 1;
		int count = dx * dz;
		IntStream.range(0, count).parallel().forEach(index -> {
			MutableBlockPos POS = new MutableBlockPos();
			POS.setX((index % dx) + start.getX());
			POS.setZ((index / dx) + start.getZ());
			BlockState state;
			for (int y = start.getY(); y <= end.getY(); y++) {
				POS.setY(y);
				state = world.getBlockState(POS);
				
				if (state.getBlock() instanceof FurBlock) {
					doubleCheck.add(POS.immutable());
				}
				// Liquids
				else if (!state.getFluidState().isEmpty()) {
					if (!state.canSurvive(world, POS)) {
						setWithoutUpdate(world, POS, WATER);
						POS.setY(POS.getY() - 1);
						state = world.getBlockState(POS);
						while (!state.canSurvive(world, POS)) {
							state = state.getFluidState().isEmpty() ? AIR : WATER;
							setWithoutUpdate(world, POS, state);
							POS.setY(POS.getY() - 1);
							state = world.getBlockState(POS);
						}
					}
					POS.setY(y - 1);
					if (world.isEmptyBlock(POS)) {
						POS.setY(y);
						while (!world.getFluidState(POS).isEmpty()) {
							setWithoutUpdate(world, POS, AIR);
							POS.setY(POS.getY() + 1);
						}
						continue;
					}
					for (Direction dir : BlocksHelper.HORIZONTAL) {
						if (world.isEmptyBlock(POS.relative(dir))) {
							world.getLiquidTicks().scheduleTick(POS, state.getFluidState().getType(), 0);
							break;
						}
					}
				}
				else if (state.is(EndBlocks.SMARAGDANT_CRYSTAL)) {
					POS.setY(POS.getY() - 1);
					if (world.isEmptyBlock(POS)) {
						POS.setY(POS.getY() + 1);
						while (state.is(EndBlocks.SMARAGDANT_CRYSTAL)) {
							setWithoutUpdate(world, POS, AIR);
							POS.setY(POS.getY() + 1);
							state = world.getBlockState(POS);
						}
					}
				}
				else if (state.getBlock() instanceof StalactiteBlock) {
					if (!state.canSurvive(world, POS)) {
						if (world.getBlockState(POS.above()).getBlock() instanceof StalactiteBlock) {
							while (state.getBlock() instanceof StalactiteBlock) {
								setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() + 1);
								state = world.getBlockState(POS);
							}
						}
						else {
							while (state.getBlock() instanceof StalactiteBlock) {
								setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() - 1);
								state = world.getBlockState(POS);
							}
						}
					}
				}
				else if (state.is(EndBlocks.CAVE_PUMPKIN)) {
					if (!world.getBlockState(POS.above()).is(EndBlocks.CAVE_PUMPKIN_SEED)) {
						setWithoutUpdate(world, POS, AIR);
					}
				}
				else if (!state.canSurvive(world, POS)) {
					// Chorus
					if (state.is(Blocks.CHORUS_PLANT)) {
						Set<BlockPos> ends = Sets.newHashSet();
						Set<BlockPos> add = Sets.newHashSet();
						ends.add(POS.immutable());
						
						for (int i = 0; i < 64 && !ends.isEmpty(); i++) {
							ends.forEach((pos) -> {
								setWithoutUpdate(world, pos, AIR);
								for (Direction dir : BlocksHelper.HORIZONTAL) {
									BlockPos p = pos.relative(dir);
									BlockState st = world.getBlockState(p);
									if ((st.is(Blocks.CHORUS_PLANT) || st.is(Blocks.CHORUS_FLOWER)) && !st.canSurvive(world, p)) {
										add.add(p);
									}
								}
								BlockPos p = pos.above();
								BlockState st = world.getBlockState(p);
								if ((st.is(Blocks.CHORUS_PLANT) || st.is(Blocks.CHORUS_FLOWER)) && !st.canSurvive(world, p)) {
									add.add(p);
								}
							});
							ends.clear();
							ends.addAll(add);
							add.clear();
						}
					}
					// Vines
					else if (state.getBlock() instanceof BaseVineBlock) {
						while (world.getBlockState(POS).getBlock() instanceof BaseVineBlock) {
							setWithoutUpdate(world, POS, AIR);
							POS.setY(POS.getY() - 1);
						}
					}
					// Falling blocks
					else if (state.getBlock() instanceof FallingBlock) {
						BlockState falling = state;
						
						POS.setY(POS.getY() - 1);
						state = world.getBlockState(POS);
						
						int ray = BlocksHelper.downRayRep(world, POS.immutable(), 64);
						if (ray > 32) {
							setWithoutUpdate(world, POS, Blocks.END_STONE.defaultBlockState());
							if (world.getRandom().nextBoolean()) {
								POS.setY(POS.getY() - 1);
								state = world.getBlockState(POS);
								setWithoutUpdate(world, POS, Blocks.END_STONE.defaultBlockState());
							}
						}
						else {
							POS.setY(y);
							BlockState replacement = AIR;
							for (Direction dir : BlocksHelper.HORIZONTAL) {
								state = world.getBlockState(POS.relative(dir));
								if (!state.getFluidState().isEmpty()) {
									replacement = state;
									break;
								}
							}
							setWithoutUpdate(world, POS, replacement);
							POS.setY(y - ray);
							setWithoutUpdate(world, POS, falling);
						}
					}
					// Blocks without support
					else {
						// Blue Vine
						if (state.getBlock() instanceof BlueVineBlock) {
							while (state.is(EndBlocks.BLUE_VINE) || state.is(EndBlocks.BLUE_VINE_LANTERN) || state.is(EndBlocks.BLUE_VINE_FUR)) {
								setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() + 1);
								state = world.getBlockState(POS);
							}
						}
						// Double plants
						if (state.getBlock() instanceof BaseDoublePlantBlock) {
							setWithoutUpdate(world, POS, AIR);
							POS.setY(POS.getY() + 1);
							setWithoutUpdate(world, POS, AIR);
						}
						// Other blocks
						else {
							setWithoutUpdate(world, POS, getAirOrFluid(state));
						}
					}
				}
			}
		});
		
		doubleCheck.forEach((pos) -> {
			if (!world.getBlockState(pos).canSurvive(world, pos)) {
				setWithoutUpdate(world, pos, AIR);
			}
		});
	}
	
	private static BlockState getAirOrFluid(BlockState state) {
		return state.getFluidState().isEmpty() ? AIR : state.getFluidState().createLegacyBlock();
	}
	
	private static void setWithoutUpdate(LevelAccessor world, BlockPos pos, BlockState state) {
		synchronized (world) {
			BlocksHelper.setWithoutUpdate(world, pos, state);
		}
	}
}
