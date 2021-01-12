package ru.betterend.world.features.trees;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class TenaneaFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Function<BlockState, Boolean> IGNORE;
	private static final List<Vector3f> SPLINE;
	private static final Direction[] DIRECTIONS = Direction.values();
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).getBlock().isIn(EndTags.END_GROUND)) return false;
		
		float size = MHelper.randRange(7, 10, random);
		int count = (int) (size * 0.45F);
		float var = MHelper.PI2 /  (float) (count * 3);
		float start = MHelper.randRange(0, MHelper.PI2, random);
		for (int i = 0; i < count; i++) {
			float angle = (float) i / (float) count * MHelper.PI2 + MHelper.randRange(0, var, random) + start;
			List<Vector3f> spline = SplineHelper.copySpline(SPLINE);
			SplineHelper.rotateSpline(spline, angle);
			SplineHelper.scale(spline, size + MHelper.randRange(0, size * 0.5F, random));
			SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
			SplineHelper.fillSpline(spline, world, EndBlocks.TENANEA.bark.getDefaultState(), pos, REPLACE);
			Vector3f last = spline.get(spline.size() - 1);
			float leavesRadius = (size * 0.3F + MHelper.randRange(0.8F, 1.5F, random)) * 1.4F;
			OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
			leavesBall(world, pos.add(last.getX(), last.getY(), last.getZ()), leavesRadius, random, noise);
		}
		
		return true;
	}
	
	private void leavesBall(StructureWorldAccess world, BlockPos pos, float radius, Random random, OpenSimplexNoise noise) {
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(EndBlocks.TENANEA_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 6));
		SDF sub = new SDFScale().setScale(5).setSource(sphere);
		sub = new SDFTranslate().setTranslate(0, -radius * 5, 0).setSource(sub);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(sub);
		sphere = new SDFScale3D().setScale(1, 0.75F, 1).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return (float) noise.eval(vec.getX() * 0.2, vec.getY() * 0.2, vec.getZ() * 0.2) * 2F; }).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return MHelper.randRange(-1.5F, 1.5F, random); }).setSource(sphere);
		
		Mutable mut = new Mutable();
		for (Direction d1: BlocksHelper.HORIZONTAL) {
			BlockPos p = mut.set(pos).move(Direction.UP).move(d1).toImmutable();
			BlocksHelper.setWithoutUpdate(world, p, EndBlocks.TENANEA.bark.getDefaultState());
			for (Direction d2: BlocksHelper.HORIZONTAL) {
				mut.set(p).move(Direction.UP).move(d2);
				BlocksHelper.setWithoutUpdate(world, p, EndBlocks.TENANEA.bark.getDefaultState());
			}
		}
		
		BlockState top = EndBlocks.TENANEA_FLOWERS.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP);
		BlockState middle = EndBlocks.TENANEA_FLOWERS.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE);
		BlockState bottom = EndBlocks.TENANEA_FLOWERS.getDefaultState().with(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM);
		BlockState outer = EndBlocks.TENANEA_OUTER_LEAVES.getDefaultState();
		
		List<BlockPos> support = Lists.newArrayList();
		sphere.addPostProcess((info) -> {
			if (random.nextInt(6) == 0 && info.getStateDown().isAir()) {
				BlockPos d = info.getPos().down();
				support.add(d);
			}
			if (random.nextInt(5) == 0) {
				for (Direction dir: Direction.values()) {
					BlockState state = info.getState(dir, 2);
					if (state.isAir()) {
						return info.getState();
					}
				}
				info.setState(EndBlocks.TENANEA.bark.getDefaultState());
			}
			
			MHelper.shuffle(DIRECTIONS, random);
			for (Direction d: DIRECTIONS) {
				if (info.getState(d).isAir()) {
					info.setBlockPos(info.getPos().offset(d), outer.with(FurBlock.FACING, d));
				}
			}
			
			if (EndBlocks.TENANEA.isTreeLog(info.getState())) {
				for (int x = -6; x < 7; x++) {
					int ax = Math.abs(x);
					mut.setX(x + info.getPos().getX());
					for (int z = -6; z < 7; z++) {
						int az = Math.abs(z);
						mut.setZ(z + info.getPos().getZ());
						for (int y = -6; y < 7; y++) {
							int ay = Math.abs(y);
							int d = ax + ay + az;
							if (d < 7) {
								mut.setY(y + info.getPos().getY());
								BlockState state = info.getState(mut);
								if (state.getBlock() instanceof LeavesBlock) {
									int distance = state.get(LeavesBlock.DISTANCE);
									if (d < distance) {
										info.setState(mut, state.with(LeavesBlock.DISTANCE, d));
									}
								}
							}
						}
					}
				}
			}
			return info.getState();
		});
		sphere.fillRecursiveIgnore(world, pos, IGNORE);
		BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.TENANEA.bark);
		
		support.forEach((bpos) -> {
			BlockState state = world.getBlockState(bpos);
			if (state.isAir() || state.isOf(EndBlocks.TENANEA_OUTER_LEAVES)) {
				int count = MHelper.randRange(3, 8, random);
				mut.set(bpos);
				if (world.getBlockState(mut.up()).isOf(EndBlocks.TENANEA_LEAVES)) {
					BlocksHelper.setWithoutUpdate(world, mut, top);
					for (int i = 1; i < count; i++) {
						mut.setY(mut.getY() - 1);
						if (world.isAir(mut.down())) {
							BlocksHelper.setWithoutUpdate(world, mut, middle);
						}
						else {
							break;
						}
					}
					BlocksHelper.setWithoutUpdate(world, mut, bottom);
				}
			}
		});
	}
	
	static {
		REPLACE = (state) -> {
			if (state.isIn(EndTags.END_GROUND)) {
				return true;
			}
			if (state.getBlock() == EndBlocks.TENANEA_LEAVES) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		IGNORE = (state) -> {
			return EndBlocks.TENANEA.isTreeLog(state);
		};
		
		SPLINE = Lists.newArrayList(
			new Vector3f(0.00F, 0.00F, 0.00F),
			new Vector3f(0.10F, 0.35F, 0.00F),
			new Vector3f(0.20F, 0.50F, 0.00F),
			new Vector3f(0.30F, 0.55F, 0.00F),
			new Vector3f(0.42F, 0.70F, 0.00F),
			new Vector3f(0.50F, 1.00F, 0.00F)
		);
	}
}
