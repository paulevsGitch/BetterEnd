package ru.betterend.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.WorldAccess;
import ru.betterend.blocks.BlockBlueVine;
import ru.betterend.blocks.basis.BlockDoublePlant;
import ru.betterend.blocks.basis.BlockGlowingFur;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;

public class BlocksHelper {
	public static final BooleanProperty ROOTS = BooleanProperty.of("roots");
	
	public static final int FLAG_UPDATE_BLOCK = 1;
	public static final int FLAG_SEND_CLIENT_CHANGES = 2;
	public static final int FLAG_NO_RERENDER = 4;
	public static final int FORSE_RERENDER = 8;
	public static final int FLAG_IGNORE_OBSERVERS = 16;

	public static final int SET_SILENT = FLAG_UPDATE_BLOCK | FLAG_IGNORE_OBSERVERS | FLAG_SEND_CLIENT_CHANGES;
	public static final Direction[] HORIZONTAL = makeHorizontal();
	
	private static final Mutable POS = new Mutable();
	protected static final BlockState AIR = Blocks.AIR.getDefaultState();

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

	public static void setWithoutUpdate(WorldAccess world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state, SET_SILENT);
	}
	
	public static void setWithoutUpdate(WorldAccess world, BlockPos pos, Block block) {
		world.setBlockState(pos, block.getDefaultState(), SET_SILENT);
	}

	public static int upRay(WorldAccess world, BlockPos pos, int maxDist) {
		int length = 0;
		for (int j = 1; j < maxDist && (world.isAir(pos.up(j))); j++)
			length++;
		return length;
	}

	public static int downRay(WorldAccess world, BlockPos pos, int maxDist) {
		int length = 0;
		for (int j = 1; j < maxDist && (world.isAir(pos.down(j))); j++)
			length++;
		return length;
	}
	
	public static int downRayRep(WorldAccess world, BlockPos pos, int maxDist) {
		POS.set(pos);
		for (int j = 1; j < maxDist && (world.getBlockState(POS)).getMaterial().isReplaceable(); j++)
		{
			POS.setY(POS.getY() - 1);
		}
		return pos.getY() - POS.getY();
	}

	public static BlockState rotateHorizontal(BlockState state, BlockRotation rotation, Property<Direction> facing) {
		return (BlockState) state.with(facing, rotation.rotate((Direction) state.get(facing)));
	}

	public static BlockState mirrorHorizontal(BlockState state, BlockMirror mirror, Property<Direction> facing) {
		return state.rotate(mirror.getRotation((Direction) state.get(facing)));
	}

	public static int getLengthDown(WorldAccess world, BlockPos pos, Block block) {
		int count = 1;
		while (world.getBlockState(pos.down(count)).getBlock() == block)
			count++;
		return count;
	}

	public static void cover(WorldAccess world, BlockPos center, Block ground, BlockState cover, int radius, Random random) {
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
							BlockPos pos2 = pos.add(offset);
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
	
	public static void fixBlocks(WorldAccess world, BlockPos start, BlockPos end) {
		BlockState state;
		Set<BlockPos> doubleCheck = Sets.newHashSet();
		for (int x = start.getX(); x <= end.getX(); x++) {
			POS.setX(x);
			for (int z = start.getZ(); z <= end.getZ(); z++) {
				POS.setZ(z);
				for (int y = start.getY(); y <= end.getY(); y++) {
					POS.setY(y);
					state = world.getBlockState(POS);
					
					if (state.getBlock() instanceof BlockGlowingFur) {
						doubleCheck.add(POS.toImmutable());
					}
					
					// Chorus
					if (state.isOf(Blocks.CHORUS_PLANT)) {
						Set<BlockPos> ends = Sets.newHashSet();
						Set<BlockPos> add = Sets.newHashSet();
						ends.add(POS.toImmutable());
						
						for (int i = 0; i < 64 && !ends.isEmpty(); i++) {
							ends.forEach((pos) -> {
								setWithoutUpdate(world, pos, AIR);
								for (Direction dir: HORIZONTAL) {
									BlockPos p = pos.offset(dir);
									BlockState st = world.getBlockState(p);
									if ((st.isOf(Blocks.CHORUS_PLANT) || st.isOf(Blocks.CHORUS_FLOWER)) && !st.canPlaceAt(world, p)) {
										add.add(p);
									}
								}
								BlockPos p = pos.up();
								BlockState st = world.getBlockState(p);
								if ((st.isOf(Blocks.CHORUS_PLANT) || st.isOf(Blocks.CHORUS_FLOWER)) && !st.canPlaceAt(world, p)) {
									add.add(p);
								}
							});
							ends.clear();
							ends.addAll(add);
							add.clear();
						}
					}
					// Liquids
					else if (!state.getFluidState().isEmpty()) {
						POS.setY(y - 1);
						if (world.isAir(POS)) {
							POS.setY(y);
							while (!world.getFluidState(POS).isEmpty()) {
								setWithoutUpdate(world, POS, AIR);
								POS.setY(POS.getY() + 1);
							}
						}
						else for (Direction dir: HORIZONTAL) {
							if (world.getBlockState(POS.offset(dir)).getMaterial().isReplaceable()) {
								world.getFluidTickScheduler().schedule(POS, state.getFluidState().getFluid(), 0);
							}
						}
					}
					// Falling blocks
					else if (state.getBlock() instanceof FallingBlock) {
						BlockState falling = state;
						
						POS.setY(POS.getY() - 1);
						state = world.getBlockState(POS);
						
						int ray = downRayRep(world, POS.toImmutable(), 64);
						if (ray > 32) {
							BlocksHelper.setWithoutUpdate(world, POS, Blocks.END_STONE.getDefaultState());
							if (world.getRandom().nextBoolean()) {
								POS.setY(POS.getY() - 1);
								state = world.getBlockState(POS);
								BlocksHelper.setWithoutUpdate(world, POS, Blocks.END_STONE.getDefaultState());
							}
						}
						else {
							POS.setY(y);
							boolean place = true;
							for (Direction dir: HORIZONTAL) {
								state = world.getBlockState(POS.offset(dir));
								if (!state.getFluidState().isEmpty()) {
									BlocksHelper.setWithoutUpdate(world, POS, state);
									place = false;
									break;
								}
							}
							if (place) {
								BlocksHelper.setWithoutUpdate(world, POS, AIR);
							}
							
							POS.setY(y - ray);
							BlocksHelper.setWithoutUpdate(world, POS, falling);
						}
					}
					// Blocks without support
					else if (!state.canPlaceAt(world, POS)) {
						// Blue Vine
						if (state.getBlock() instanceof BlockBlueVine) {
							while (state.isOf(BlockRegistry.BLUE_VINE) || state.isOf(BlockRegistry.BLUE_VINE_LANTERN) || state.isOf(BlockRegistry.BLUE_VINE_FUR)) {
								BlocksHelper.setWithoutUpdate(world, POS, AIR);
								for (Direction dir : HORIZONTAL) {
									BlockPos p = POS.offset(dir);
									state = world.getBlockState(p);
									if (state.getBlock() instanceof BlockGlowingFur) {
										BlocksHelper.setWithoutUpdate(world, p, AIR);
									}
									world.getBlockTickScheduler().schedule(p, world.getBlockState(p).getBlock(), 0);
								}
								POS.setY(POS.getY() + 1);
								state = world.getBlockState(POS);
							}
						}
						// Double plants
						if (state.getBlock() instanceof BlockDoublePlant) {
							BlocksHelper.setWithoutUpdate(world, POS, AIR);
							POS.setY(POS.getY() + 1);
							BlocksHelper.setWithoutUpdate(world, POS, AIR);
						}
						// Other blocks
						else {
							BlocksHelper.setWithoutUpdate(world, POS, AIR);
						}
					}
				}
			}
		}
		
		doubleCheck.forEach((pos) -> {
			if (!world.getBlockState(pos).canPlaceAt(world, pos)) {
				BlocksHelper.setWithoutUpdate(world, pos, AIR);
			}
		});
	}
	
	public static boolean isEndNylium(Block block) {
		return block.isIn(BlockTags.NYLIUM) && block.isIn(BlockTagRegistry.END_GROUND);
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
}
