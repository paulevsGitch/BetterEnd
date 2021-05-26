package ru.betterend.world.features.bushes;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.TagAPI;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFScale3D;
import ru.bclib.sdf.operator.SDFSubtraction;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.primitive.SDFSphere;
import ru.bclib.util.MHelper;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.world.features.DefaultFeature;

public class TenaneaBushFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Direction[] DIRECTIONS = Direction.values();

	public TenaneaBushFeature() {
	}

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			NoneFeatureConfiguration config) {
		if (!world.getBlockState(pos.below()).getBlock().is(TagAPI.END_GROUND))
			return false;

		float radius = MHelper.randRange(1.8F, 3.5F, random);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		BlockState leaves = EndBlocks.TENANEA_LEAVES.defaultBlockState();
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(leaves);
		sphere = new SDFScale3D().setScale(1, 0.75F, 1).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> {
			return (float) noise.eval(vec.x() * 0.2, vec.y() * 0.2, vec.z() * 0.2) * 3;
		}).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> {
			return MHelper.randRange(-2F, 2F, random);
		}).setSource(sphere);
		sphere = new SDFSubtraction().setSourceA(sphere)
				.setSourceB(new SDFTranslate().setTranslate(0, -radius, 0).setSource(sphere));
		sphere.setReplaceFunction(REPLACE);
		List<BlockPos> support = Lists.newArrayList();
		sphere.addPostProcess((info) -> {
			if (info.getState().getBlock() instanceof LeavesBlock) {
				int distance = info.getPos().distManhattan(pos);
				if (distance < 7) {
					if (random.nextInt(4) == 0 && info.getStateDown().isAir()) {
						BlockPos d = info.getPos().below();
						support.add(d);
					}

					MHelper.shuffle(DIRECTIONS, random);
					for (Direction d : DIRECTIONS) {
						if (info.getState(d).isAir()) {
							info.setBlockPos(info.getPos().relative(d),
									EndBlocks.TENANEA_OUTER_LEAVES.defaultBlockState().setValue(FurBlock.FACING, d));
						}
					}

					return info.getState().setValue(LeavesBlock.DISTANCE, distance);
				} else {
					return AIR;
				}
			}
			return info.getState();
		});
		sphere.fillRecursive(world, pos);
		BlockState stem = EndBlocks.TENANEA.bark.defaultBlockState();
		BlocksHelper.setWithoutUpdate(world, pos, stem);
		for (Direction d : Direction.values()) {
			BlockPos p = pos.relative(d);
			if (world.isEmptyBlock(p)) {
				BlocksHelper.setWithoutUpdate(world, p, leaves.setValue(LeavesBlock.DISTANCE, 1));
			}
		}

		MutableBlockPos mut = new MutableBlockPos();
		BlockState top = EndBlocks.TENANEA_FLOWERS.defaultBlockState().setValue(BlockProperties.TRIPLE_SHAPE,
				TripleShape.TOP);
		BlockState middle = EndBlocks.TENANEA_FLOWERS.defaultBlockState().setValue(BlockProperties.TRIPLE_SHAPE,
				TripleShape.MIDDLE);
		BlockState bottom = EndBlocks.TENANEA_FLOWERS.defaultBlockState().setValue(BlockProperties.TRIPLE_SHAPE,
				TripleShape.BOTTOM);
		support.forEach((bpos) -> {
			BlockState state = world.getBlockState(bpos);
			if (state.isAir() || state.is(EndBlocks.TENANEA_OUTER_LEAVES)) {
				int count = MHelper.randRange(3, 8, random);
				mut.set(bpos);
				if (world.getBlockState(mut.above()).is(EndBlocks.TENANEA_LEAVES)) {
					BlocksHelper.setWithoutUpdate(world, mut, top);
					for (int i = 1; i < count; i++) {
						mut.setY(mut.getY() - 1);
						if (world.isEmptyBlock(mut.below())) {
							BlocksHelper.setWithoutUpdate(world, mut, middle);
						} else {
							break;
						}
					}
					BlocksHelper.setWithoutUpdate(world, mut, bottom);
				}
			}
		});

		return true;
	}

	static {
		REPLACE = (state) -> {
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}
}
