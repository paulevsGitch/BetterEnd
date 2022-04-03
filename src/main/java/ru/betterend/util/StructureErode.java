package ru.betterend.util;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.tag.CommonBlockTags;
import ru.bclib.api.tag.TagAPI;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;

import java.util.Random;
import java.util.Set;

public class StructureErode {
	private static final Direction[] DIR = BlocksHelper.makeHorizontal();
	
	public static void erode(WorldGenLevel world, BoundingBox bounds, int iterations, Random random) {
		MutableBlockPos mut = new MutableBlockPos();
		boolean canDestruct = true;
		for (int i = 0; i < iterations; i++) {
			for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
				mut.setX(x);
				for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
					mut.setZ(z);
					for (int y = bounds.maxY(); y >= bounds.minY(); y--) {
						mut.setY(y);
						BlockState state = world.getBlockState(mut);
						boolean ignore = ignore(state, world, mut);
						if (canDestruct && BlocksHelper.isInvulnerable(
							state,
							world,
							mut
						) && random.nextInt(8) == 0 && world.isEmptyBlock(mut.below(2))) {
							int r = MHelper.randRange(1, 4, random);
							int cx = mut.getX();
							int cy = mut.getY();
							int cz = mut.getZ();
							int x1 = cx - r;
							int y1 = cy - r;
							int z1 = cz - r;
							int x2 = cx + r;
							int y2 = cy + r;
							int z2 = cz + r;
							for (int px = x1; px <= x2; px++) {
								int dx = px - cx;
								dx *= dx;
								mut.setX(px);
								for (int py = y1; py <= y2; py++) {
									int dy = py - cy;
									dy *= dy;
									mut.setY(py);
									for (int pz = z1; pz <= z2; pz++) {
										int dz = pz - cz;
										dz *= dz;
										mut.setZ(pz);
										if (dx + dy + dz <= r && BlocksHelper.isInvulnerable(
											world.getBlockState(mut),
											world,
											mut
										)) {
											BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
										}
									}
								}
							}
							mut.setX(cx);
							mut.setY(cy);
							mut.setZ(cz);
							canDestruct = false;
							continue;
						}
						else if (ignore) {
							continue;
						}
						if (!state.isAir() && random.nextBoolean()) {
							MHelper.shuffle(DIR, random);
							for (Direction dir : DIR) {
								if (world.isEmptyBlock(mut.relative(dir)) && world.isEmptyBlock(mut.below()
																								   .relative(dir))) {
									BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
									mut.move(dir).move(Direction.DOWN);
									for (int py = mut.getY(); y >= bounds.minY() - 10; y--) {
										mut.setY(py - 1);
										if (!world.isEmptyBlock(mut)) {
											mut.setY(py);
											BlocksHelper.setWithoutUpdate(world, mut, state);
											break;
										}
									}
								}
							}
							break;
						}
						else if (random.nextInt(8) == 0 && !BlocksHelper.isInvulnerable(
							world.getBlockState(mut.above()),
							world,
							mut
						)) {
							BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
						}
					}
				}
			}
		}
		for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
			mut.setX(x);
			for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
				mut.setZ(z);
				for (int y = bounds.maxY(); y >= bounds.minY(); y--) {
					mut.setY(y);
					BlockState state = world.getBlockState(mut);
					if (!ignore(state, world, mut) && world.isEmptyBlock(mut.below())) {
						BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
						for (int py = mut.getY(); py >= bounds.minY() - 10; py--) {
							mut.setY(py - 1);
							if (!world.isEmptyBlock(mut)) {
								mut.setY(py);
								BlocksHelper.setWithoutUpdate(world, mut, state);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	public static void erodeIntense(WorldGenLevel world, BoundingBox bounds, Random random) {
		MutableBlockPos mut = new MutableBlockPos();
		MutableBlockPos mut2 = new MutableBlockPos();
		int minY = bounds.minY() - 10;
		for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
			mut.setX(x);
			for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
				mut.setZ(z);
				for (int y = bounds.maxY(); y >= bounds.minY(); y--) {
					mut.setY(y);
					BlockState state = world.getBlockState(mut);
					if (!ignore(state, world, mut)) {
						if (random.nextInt(6) == 0) {
							BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
							if (random.nextBoolean()) {
								int px = MHelper.floor(random.nextGaussian() * 2 + x + 0.5);
								int pz = MHelper.floor(random.nextGaussian() * 2 + z + 0.5);
								mut2.set(px, y, pz);
								while (world.getBlockState(mut2).getMaterial().isReplaceable() && mut2.getY() > minY) {
									mut2.setY(mut2.getY() - 1);
								}
								if (!world.getBlockState(mut2).isAir() && state.canSurvive(world, mut2)) {
									mut2.setY(mut2.getY() + 1);
									BlocksHelper.setWithoutUpdate(world, mut2, state);
								}
							}
						}
						else if (random.nextInt(8) == 0) {
							BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
						}
					}
				}
			}
		}
		
		drop(world, bounds);
	}
	
	private static void drop(WorldGenLevel world, BoundingBox bounds) {
		MutableBlockPos mut = new MutableBlockPos();
		
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> edge = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		
		for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
			mut.setX(x);
			for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
				mut.setZ(z);
				for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
					mut.setY(y);
					BlockState state = world.getBlockState(mut);
					if (!ignore(state, world, mut) && isTerrainNear(world, mut)) {
						edge.add(mut.immutable());
					}
				}
			}
		}
		
		if (edge.isEmpty()) {
			return;
		}
		
		while (!edge.isEmpty()) {
			for (BlockPos center : edge) {
				for (Direction dir : BlocksHelper.DIRECTIONS) {
					BlockState state = world.getBlockState(center);
					if (state.isCollisionShapeFullBlock(world, center)) {
						mut.set(center).move(dir);
						if (bounds.isInside(mut)) {
							state = world.getBlockState(mut);
							if (!ignore(state, world, mut) && !blocks.contains(mut)) {
								add.add(mut.immutable());
							}
						}
					}
				}
			}
			
			blocks.addAll(edge);
			edge.clear();
			edge.addAll(add);
			add.clear();
		}
		
		int minY = bounds.minY() - 10;
		for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
			mut.setX(x);
			for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
				mut.setZ(z);
				for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
					mut.setY(y);
					BlockState state = world.getBlockState(mut);
					if (!ignore(state, world, mut) && !blocks.contains(mut)) {
						BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
						while (world.getBlockState(mut).getMaterial().isReplaceable() && mut.getY() > minY) {
							mut.setY(mut.getY() - 1);
						}
						if (mut.getY() > minY) {
							mut.setY(mut.getY() + 1);
							BlocksHelper.setWithoutUpdate(world, mut, state);
						}
					}
				}
			}
		}
	}
	
	private static boolean ignore(BlockState state, WorldGenLevel world, BlockPos pos) {
		if (state.is(CommonBlockTags.GEN_END_STONES) || state.is(BlockTags.NYLIUM)) {
			return true;
		}
		return !state.getMaterial().equals(Material.STONE) || BlocksHelper.isInvulnerable(state, world, pos);
	}
	
	private static boolean isTerrainNear(WorldGenLevel world, BlockPos pos) {
		for (Direction dir : BlocksHelper.DIRECTIONS) {
			if (world.getBlockState(pos.relative(dir)).is(CommonBlockTags.GEN_END_STONES)) {
				return true;
			}
		}
		return false;
	}
	
	public static void cover(WorldGenLevel world, BoundingBox bounds, Random random, BlockState defaultBlock) {
		MutableBlockPos mut = new MutableBlockPos();
		for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
			mut.setX(x);
			for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
				mut.setZ(z);
				BlockState top = BiomeAPI.findTopMaterial(world.getBiome(mut)).orElse(defaultBlock);
				for (int y = bounds.maxY(); y >= bounds.minY(); y--) {
					mut.setY(y);
					BlockState state = world.getBlockState(mut);
					if (state.is(CommonBlockTags.END_STONES) && !world.getBlockState(mut.above()).getMaterial().isSolidBlocking()) {
						BlocksHelper.setWithoutUpdate(world, mut, top);
					}
				}
			}
		}
	}
}
