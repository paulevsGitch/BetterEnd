package ru.betterend.world.features.trees;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.blocks.BaseAttachedBlock;
import ru.bclib.sdf.PosInfo;
import ru.bclib.sdf.SDF;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.util.SplineHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.registry.EndBlocks;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class GiganticAmaranitaFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Function<BlockState, Boolean> IGNORE;
	private static final Function<PosInfo, BlockState> POST;
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		final BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		if (!world.getBlockState(pos.below()).is(BlockTags.NYLIUM)) return false;
		
		float size = MHelper.randRange(5, 10, random);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, size, 0, 5);
		SplineHelper.offsetParts(spline, random, 0.7F, 0, 0.7F);
		
		if (!SplineHelper.canGenerate(spline, pos, world, REPLACE)) {
			return false;
		}
		BlocksHelper.setWithoutUpdate(world, pos, AIR);
		
		float radius = size * 0.17F;// MHelper.randRange(0.8F, 1.2F, random);
		SDF function = SplineHelper.buildSDF(
			spline,
			radius,
			0.2F,
			(bpos) -> EndBlocks.AMARANITA_STEM.defaultBlockState()
		);
		
		Vector3f capPos = spline.get(spline.size() - 1);
		makeHead(world, pos.offset(capPos.x() + 0.5F, capPos.y() + 1.5F, capPos.z() + 0.5F), Mth.floor(size / 1.6F));
		
		function.setReplaceFunction(REPLACE);
		function.addPostProcess(POST);
		function.fillRecursiveIgnore(world, pos, IGNORE);
		
		for (int i = 0; i < 3; i++) {
			List<Vector3f> copy = SplineHelper.copySpline(spline);
			SplineHelper.offsetParts(copy, random, 0.2F, 0, 0.2F);
			SplineHelper.fillSplineForce(copy, world, EndBlocks.AMARANITA_HYPHAE.defaultBlockState(), pos, REPLACE);
		}
		
		return true;
	}
	
	private void makeHead(WorldGenLevel world, BlockPos pos, int radius) {
		MutableBlockPos mut = new MutableBlockPos();
		if (radius < 2) {
			for (int i = -1; i < 2; i++) {
				mut.set(pos).move(Direction.NORTH, 2).move(Direction.EAST, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.set(pos).move(Direction.SOUTH, 2).move(Direction.EAST, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.set(pos).move(Direction.EAST, 2).move(Direction.NORTH, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.set(pos).move(Direction.WEST, 2).move(Direction.NORTH, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
			}
			for (int x = -1; x < 2; x++) {
				for (int z = -1; z < 2; z++) {
					mut.set(pos).move(x, 0, z);
					if (world.getBlockState(mut).getMaterial().isReplaceable()) {
						BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_LANTERN);
						mut.move(Direction.DOWN);
						BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_LANTERN);
						mut.move(Direction.DOWN);
						if (world.getBlockState(mut).getMaterial().isReplaceable()) {
							BlocksHelper.setWithoutUpdate(
								world,
								mut,
								EndBlocks.AMARANITA_FUR.defaultBlockState()
													   .setValue(BaseAttachedBlock.FACING, Direction.DOWN)
							);
						}
					}
				}
			}
			
			int h = radius + 1;
			for (int y = 0; y < h; y++) {
				mut.setY(pos.getY() + y + 1);
				for (int x = -1; x < 2; x++) {
					mut.setX(pos.getX() + x);
					for (int z = -1; z < 2; z++) {
						mut.setZ(pos.getZ() + z);
						if (world.getBlockState(mut).getMaterial().isReplaceable()) {
							BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_CAP);
						}
					}
				}
			}
			
			mut.setY(pos.getY() + h + 1);
			for (int x = -1; x < 2; x++) {
				mut.setX(pos.getX() + x);
				for (int z = -1; z < 2; z++) {
					mut.setZ(pos.getZ() + z);
					if ((x == 0 || z == 0) && world.getBlockState(mut).getMaterial().isReplaceable()) {
						BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_CAP);
					}
				}
			}
		}
		else if (radius < 4) {
			pos = pos.offset(-1, 0, -1);
			for (int i = -2; i < 2; i++) {
				mut.set(pos).move(Direction.NORTH, 2).move(Direction.WEST, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.set(pos).move(Direction.SOUTH, 3).move(Direction.WEST, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.set(pos).move(Direction.EAST, 3).move(Direction.NORTH, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.set(pos).move(Direction.WEST, 2).move(Direction.NORTH, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
			}
			for (int x = -1; x < 3; x++) {
				for (int z = -1; z < 3; z++) {
					mut.set(pos).move(x, 0, z);
					if (world.getBlockState(mut).getMaterial().isReplaceable()) {
						BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_LANTERN);
						mut.move(Direction.DOWN);
						if ((x >> 1) == 0 || (z >> 1) == 0) {
							BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_LANTERN);
							Axis axis = x < 0 || x > 1 ? Axis.X : Axis.Z;
							int distance = axis == Axis.X ? x < 0 ? -1 : 1 : z < 0 ? -1 : 1;
							BlockPos offseted = mut.relative(axis, distance);
							if (world.getBlockState(offseted).getMaterial().isReplaceable()) {
								Direction dir = Direction.fromAxisAndDirection(
									axis,
									distance < 0 ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE
								);
								BlocksHelper.setWithoutUpdate(
									world,
									offseted,
									EndBlocks.AMARANITA_FUR.defaultBlockState().setValue(BaseAttachedBlock.FACING, dir)
								);
							}
							mut.move(Direction.DOWN);
						}
						if (world.getBlockState(mut).getMaterial().isReplaceable()) {
							BlocksHelper.setWithoutUpdate(
								world,
								mut,
								EndBlocks.AMARANITA_FUR.defaultBlockState()
													   .setValue(BaseAttachedBlock.FACING, Direction.DOWN)
							);
						}
					}
				}
			}
			
			int h = radius - 1;
			for (int y = 0; y < h; y++) {
				mut.setY(pos.getY() + y + 1);
				for (int x = -1; x < 3; x++) {
					mut.setX(pos.getX() + x);
					for (int z = -1; z < 3; z++) {
						mut.setZ(pos.getZ() + z);
						if (world.getBlockState(mut).getMaterial().isReplaceable()) {
							BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_CAP);
						}
					}
				}
			}
			
			mut.setY(pos.getY() + h + 1);
			for (int x = -1; x < 3; x++) {
				mut.setX(pos.getX() + x);
				for (int z = -1; z < 3; z++) {
					mut.setZ(pos.getZ() + z);
					if (((x >> 1) == 0 || (z >> 1) == 0) && world.getBlockState(mut).getMaterial().isReplaceable()) {
						BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_CAP);
					}
				}
			}
		}
		else {
			for (int i = -2; i < 3; i++) {
				mut.set(pos).move(Direction.NORTH, 3).move(Direction.EAST, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.move(Direction.UP);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.move(Direction.NORTH);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				
				mut.set(pos).move(Direction.SOUTH, 3).move(Direction.EAST, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.move(Direction.UP);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.move(Direction.SOUTH);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				
				mut.set(pos).move(Direction.EAST, 3).move(Direction.NORTH, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.move(Direction.UP);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.move(Direction.EAST);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				
				mut.set(pos).move(Direction.WEST, 3).move(Direction.NORTH, i);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.move(Direction.UP);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
				mut.move(Direction.WEST);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
			}
			
			for (int i = 0; i < 4; i++) {
				mut.set(pos)
				   .move(Direction.UP)
				   .move(BlocksHelper.HORIZONTAL[i], 3)
				   .move(BlocksHelper.HORIZONTAL[(i + 1) & 3], 3);
				if (world.getBlockState(mut).getMaterial().isReplaceable()) {
					BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_HYMENOPHORE);
				}
			}
			
			for (int x = -2; x < 3; x++) {
				for (int z = -2; z < 3; z++) {
					mut.set(pos).move(x, 0, z);
					if (world.getBlockState(mut).getMaterial().isReplaceable()) {
						BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_LANTERN);
						mut.move(Direction.DOWN);
						if ((x / 2) == 0 || (z / 2) == 0) {
							BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_LANTERN);
							Axis axis = x < 0 || x > 1 ? Axis.X : Axis.Z;
							int distance = axis == Axis.X ? x < 0 ? -1 : 1 : z < 0 ? -1 : 1;
							BlockPos offseted = mut.relative(axis, distance);
							if (world.getBlockState(offseted).getMaterial().isReplaceable()) {
								Direction dir = Direction.fromAxisAndDirection(
									axis,
									distance < 0 ? AxisDirection.NEGATIVE : AxisDirection.POSITIVE
								);
								BlocksHelper.setWithoutUpdate(
									world,
									offseted,
									EndBlocks.AMARANITA_FUR.defaultBlockState().setValue(BaseAttachedBlock.FACING, dir)
								);
							}
							mut.move(Direction.DOWN);
						}
						if (world.getBlockState(mut).getMaterial().isReplaceable()) {
							BlocksHelper.setWithoutUpdate(
								world,
								mut,
								EndBlocks.AMARANITA_FUR.defaultBlockState()
													   .setValue(BaseAttachedBlock.FACING, Direction.DOWN)
							);
						}
					}
				}
			}
			
			for (int y = 0; y < 3; y++) {
				mut.setY(pos.getY() + y + 1);
				for (int x = -2; x < 3; x++) {
					mut.setX(pos.getX() + x);
					for (int z = -2; z < 3; z++) {
						mut.setZ(pos.getZ() + z);
						if (world.getBlockState(mut).getMaterial().isReplaceable()) {
							BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_CAP);
						}
					}
				}
			}
			
			int h = radius + 1;
			for (int y = 4; y < h; y++) {
				mut.setY(pos.getY() + y);
				for (int x = -2; x < 3; x++) {
					mut.setX(pos.getX() + x);
					for (int z = -2; z < 3; z++) {
						mut.setZ(pos.getZ() + z);
						if (y < 6) {
							if (((x / 2) == 0 || (z / 2) == 0) && world.getBlockState(mut)
																	   .getMaterial()
																	   .isReplaceable()) {
								BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_CAP);
							}
						}
						else {
							if ((x == 0 || z == 0) && (Math.abs(x) < 2 && Math.abs(z) < 2) && world.getBlockState(mut)
																								   .getMaterial()
																								   .isReplaceable()) {
								BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.AMARANITA_CAP);
							}
						}
					}
				}
			}
		}
	}
	
	static {
		REPLACE = (state) -> {
			if (/*state.is(TagAPI.BLOCK_END_GROUND) || */state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		IGNORE = EndBlocks.DRAGON_TREE::isTreeLog;
		
		POST = (info) -> {
			if (!info.getStateUp().is(EndBlocks.AMARANITA_STEM) || !info.getStateDown().is(EndBlocks.AMARANITA_STEM)) {
				return EndBlocks.AMARANITA_HYPHAE.defaultBlockState();
			}
			return info.getState();
		};
	}
}
