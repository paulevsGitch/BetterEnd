package ru.betterend.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import ru.betterend.blocks.BlueVineBlock;
import ru.betterend.blocks.basis.DoublePlantBlock;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.blocks.basis.StalactiteBlock;
import ru.betterend.blocks.basis.VineBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;

public class BlocksHelper {
	public static final BooleanProperty ROOTS = BooleanProperty.create("roots");
	private static final Map<Block, Integer> COLOR_BY_BLOCK = Maps.newHashMap();
	
	public static final int FLAG_UPDATE_BLOCK = 1;
	public static final int FLAG_SEND_CLIENT_CHANGES = 2;
	public static final int FLAG_NO_RERENDER = 4;
	public static final int FORSE_RERENDER = 8;
	public static final int FLAG_IGNORE_OBSERVERS = 16;

	public static final int SET_SILENT = FLAG_UPDATE_BLOCK | FLAG_IGNORE_OBSERVERS | FLAG_SEND_CLIENT_CHANGES;
	public static final int SET_OBSERV = FLAG_UPDATE_BLOCK | FLAG_SEND_CLIENT_CHANGES;
	public static final Direction[] HORIZONTAL = makeHorizontal();
	public static final Direction[] DIRECTIONS = Direction.values();
	
	private static final MutableBlockPos POS = new MutableBlockPos();
	protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
	protected static final BlockState WATER = Blocks.WATER.defaultBlockState();

	private static final Vec3i[] OFFSETS = new Vec3i[] {
			new Vec3i(-1, -1, -1), new Vec3i(-1, -1, 0), new Vec3i(-1, -1, 1),
			new Vec3i(-1, 0, -1), new Vec3i(-1, 0, 0), new Vec3i(-1, 0, 1),
			new Vec3i(-1, 1, -1), new Vec3i(-1, 1, 0), new Vec3i(-1, 1, 1),

			new Vec3i(0, -1, -1), new Vec3i(0, -1, 0), new Vec3i(0, -1, 1),
			new Vec3i(0, 0, -1), new Vec3i(0, 0, 0), new Vec3i(0, 0, 1),
			new Vec3i(0, 1, -1), new Vec3i(0, 1, 0), new Vec3i(0, 1, 1),

			new Vec3i(1, -1, -1), new Vec3i(1, -1, 0), new Vec3i(1, -1, 1),
			new Vec3i(1, 0, -1), new Vec3i(1, 0, 0), new Vec3i(1, 0, 1),
			new Vec3i(1, 1, -1), new Vec3i(1, 1, 0), new Vec3i(1, 1, 1)
	};
	
	public static void addBlockColor(Block block, int color) {
		COLOR_BY_BLOCK.put(block, color);
	}
	
	public static int getBlockColor(Block block) {
		return COLOR_BY_BLOCK.getOrDefault(block, 0xFF000000);
	}

	public static void setWithoutUpdate(LevelAccessor world, BlockPos pos, BlockState state) {
		world.setBlock(pos, state, SET_SILENT);
	}
	
	public static void setWithoutUpdate(LevelAccessor world, BlockPos pos, Block block) {
		world.setBlock(pos, block.defaultBlockState(), SET_SILENT);
	}
	
	public static void setWithUpdate(LevelAccessor world, BlockPos pos, BlockState state) {
		world.setBlock(pos, state, SET_OBSERV);
	}
	
	public static void setWithUpdate(LevelAccessor world, BlockPos pos, Block block) {
		world.setBlock(pos, block.defaultBlockState(), SET_OBSERV);
	}

	public static int upRay(LevelAccessor world, BlockPos pos, int maxDist) {
		int length = 0;
		for (int j = 1; j < maxDist && (world.isEmptyBlock(pos.above(j))); j++)
			length++;
		return length;
	}

	public static int downRay(LevelAccessor world, BlockPos pos, int maxDist) {
		int length = 0;
		for (int j = 1; j < maxDist && (world.isEmptyBlock(pos.below(j))); j++)
			length++;
		return length;
	}
	
	public static int downRayRep(LevelAccessor world, BlockPos pos, int maxDist) {
		POS.set(pos);
		for (int j = 1; j < maxDist && (world.getBlockState(POS)).getMaterial().isReplaceable(); j++)
		{
			POS.setY(POS.getY() - 1);
		}
		return pos.getY() - POS.getY();
	}
	
	public static int raycastSqr(LevelAccessor world, BlockPos pos, int dx, int dy, int dz, int maxDist) {
		POS.set(pos);
		for (int j = 1; j < maxDist && (world.getBlockState(POS)).getMaterial().isReplaceable(); j++)
		{
			POS.move(dx, dy, dz);
		}
		return (int) pos.distSqr(POS);
	}

	public static BlockState rotateHorizontal(BlockState state, Rotation rotation, Property<Direction> facing) {
		return state.setValue(facing, rotation.rotate(state.getValue(facing)));
	}

	public static BlockState mirrorHorizontal(BlockState state, Mirror mirror, Property<Direction> facing) {
		return state.rotate(mirror.getRotation(state.getValue(facing)));
	}

	public static int getLengthDown(LevelAccessor world, BlockPos pos, Block block) {
		int count = 1;
		while (world.getBlockState(pos.below(count)).getBlock() == block)
			count++;
		return count;
	}

	public static void cover(LevelAccessor world, BlockPos center, Block ground, BlockState cover, int radius, Random random) {
		HashSet<BlockPos> points = new HashSet<BlockPos>();
		HashSet<BlockPos> points2 = new HashSet<BlockPos>();
		if (world.getBlockState(center).getBlock() == ground) {
			points.add(center);
			points2.add(center);
			for (int i = 0; i < radius; i++) {
				Iterator<BlockPos> iterator = points.iterator();
				while (iterator.hasNext()) {
					BlockPos pos = iterator.next();
					for (Vec3i offset : OFFSETS) {
						if (random.nextBoolean()) {
							BlockPos pos2 = pos.offset(offset);
							if (random.nextBoolean() && world.getBlockState(pos2).getBlock() == ground
									&& !points.contains(pos2))
								points2.add(pos2);
						}
					}
				}
				points.addAll(points2);
				points2.clear();
			}
			Iterator<BlockPos> iterator = points.iterator();
			while (iterator.hasNext()) {
				BlockPos pos = iterator.next();
				BlocksHelper.setWithoutUpdate(world, pos, cover);
			}
		}
	}
	
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
						for (Direction dir : HORIZONTAL) {
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
									for (Direction dir : HORIZONTAL) {
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
								setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() - 1);
							}
						}
						// Falling blocks
						else if (state.getBlock() instanceof FallingBlock) {
							BlockState falling = state;

							POS.setY(POS.getY() - 1);
							state = world.getBlockState(POS);

							int ray = downRayRep(world, POS.immutable(), 64);
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
								for (Direction dir : HORIZONTAL) {
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
	
	public static boolean isEndNylium(Block block) {
		return block.is(BlockTags.NYLIUM) && block.is(EndTags.END_GROUND);
	}
	
	public static boolean isEndNylium(BlockState state) {
		return isEndNylium(state.getBlock());
	}
	
	public static Direction[] makeHorizontal() {
		return new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
	}
	
	public static Direction randomHorizontal(Random random) {
		return HORIZONTAL[random.nextInt(4)];
	}
	
	public static Direction randomDirection(Random random) {
		return DIRECTIONS[random.nextInt(6)];
	}

	public static boolean isFluid(BlockState blockState) {
		return !blockState.getFluidState().isEmpty();
	}
}
