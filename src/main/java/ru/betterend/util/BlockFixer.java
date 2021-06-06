package ru.betterend.util;

import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.util.BlocksHelper;
import ru.betterend.blocks.BlueVineBlock;
import ru.betterend.blocks.basis.DoublePlantBlock;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.blocks.basis.StalactiteBlock;
import ru.betterend.blocks.basis.VineBlock;
import ru.betterend.registry.EndBlocks;

public class BlockFixer {
	private static final MutableBlockPos POS = new MutableBlockPos();
	private static final BlockState AIR = Blocks.AIR.defaultBlockState();
	private static final BlockState WATER = Blocks.WATER.defaultBlockState();
	
	public static void fixBlocks(LevelAccessor world, BlockPos start, BlockPos end) {
		BlockState state;
		Set<BlockPos> doubleCheck = Sets.newHashSet();
		for (int x = start.getX(); x <= end.getX(); x++) {
			POS.setX(x);
			for (int z = start.getZ(); z <= end.getZ(); z++) {
				POS.setZ(z);
				for (int y = start.getY(); y <= end.getY(); y++) {
					POS.setY(y);
					state = world.getBlockState(POS);
					
					if (state.getBlock() instanceof FurBlock) {
						doubleCheck.add(POS.immutable());
					}
					// Liquids
					else if (!state.getFluidState().isEmpty()) {
						if (!state.canSurvive(world, POS)) {
							BlocksHelper.setWithoutUpdate(world, POS, WATER);
							POS.setY(POS.getY() - 1);
							state = world.getBlockState(POS);
							while (!state.canSurvive(world, POS)) {
								state = state.getFluidState().isEmpty() ? AIR : WATER;
								BlocksHelper.setWithoutUpdate(world, POS, state);
								POS.setY(POS.getY() - 1);
								state = world.getBlockState(POS);
							}
						}
						POS.setY(y - 1);
						if (world.isEmptyBlock(POS)) {
							POS.setY(y);
							while (!world.getFluidState(POS).isEmpty()) {
								BlocksHelper.setWithoutUpdate(world, POS, AIR);
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
								BlocksHelper.setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() + 1);
								state = world.getBlockState(POS);
							}
						}
					}
					else if (state.getBlock() instanceof StalactiteBlock) {
						if (!state.canSurvive(world, POS)) {
							if (world.getBlockState(POS.above()).getBlock() instanceof StalactiteBlock) {
								while (state.getBlock() instanceof StalactiteBlock) {
									BlocksHelper.setWithoutUpdate(world, POS, AIR);
									POS.setY(POS.getY() + 1);
									state = world.getBlockState(POS);
								}
							}
							else {
								while (state.getBlock() instanceof StalactiteBlock) {
									BlocksHelper.setWithoutUpdate(world, POS, AIR);
									POS.setY(POS.getY() - 1);
									state = world.getBlockState(POS);
								}
							}
						}
					}
					else if (state.is(EndBlocks.CAVE_PUMPKIN)) {
						if (!world.getBlockState(POS.above()).is(EndBlocks.CAVE_PUMPKIN_SEED)) {
							BlocksHelper.setWithoutUpdate(world, POS, AIR);
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
									BlocksHelper.setWithoutUpdate(world, pos, AIR);
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
						else if (state.getBlock() instanceof VineBlock) {
							while (world.getBlockState(POS).getBlock() instanceof VineBlock) {
								BlocksHelper.setWithoutUpdate(world, POS, AIR);
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
								BlocksHelper.setWithoutUpdate(world, POS, Blocks.END_STONE.defaultBlockState());
								if (world.getRandom().nextBoolean()) {
									POS.setY(POS.getY() - 1);
									state = world.getBlockState(POS);
									BlocksHelper.setWithoutUpdate(world, POS, Blocks.END_STONE.defaultBlockState());
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
								BlocksHelper.setWithoutUpdate(world, POS, replacement);
								POS.setY(y - ray);
								BlocksHelper.setWithoutUpdate(world, POS, falling);
							}
						}
						// Blocks without support
						else {
							// Blue Vine
							if (state.getBlock() instanceof BlueVineBlock) {
								while (state.is(EndBlocks.BLUE_VINE) || state.is(EndBlocks.BLUE_VINE_LANTERN) || state.is(EndBlocks.BLUE_VINE_FUR)) {
									BlocksHelper.setWithoutUpdate(world, POS, AIR);
									POS.setY(POS.getY() + 1);
									state = world.getBlockState(POS);
								}
							}
							// Double plants
							if (state.getBlock() instanceof DoublePlantBlock) {
								BlocksHelper.setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() + 1);
								BlocksHelper.setWithoutUpdate(world, POS, AIR);
							}
							// Other blocks
							else {
								BlocksHelper.setWithoutUpdate(world, POS, getAirOrFluid(state));
							}
						}
					}
				}
			}
		}
		
		doubleCheck.forEach((pos) -> {
			if (!world.getBlockState(pos).canSurvive(world, pos)) {
				BlocksHelper.setWithoutUpdate(world, pos, AIR);
			}
		});
	}
	
	private static BlockState getAirOrFluid(BlockState state) {
		return state.getFluidState().isEmpty() ? AIR : state.getFluidState().createLegacyBlock();
	}
}
