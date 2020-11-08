package ru.betterend.world.features.bushes;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class DragonTreeBushFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (world.getBlockState(pos.down()).getBlock() != EndBlocks.SHADOW_GRASS) return false;
		
		BlockState leaves = EndBlocks.DRAGON_TREE_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 1);
		float radius = MHelper.randRange(1.8F, 3.5F, random);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(EndBlocks.DRAGON_TREE_LEAVES.getDefaultState().with(LeavesBlock.DISTANCE, 1));
		sphere = new SDFScale3D().setScale(1, 0.5F, 1).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return (float) noise.eval(vec.getX() * 0.2, vec.getY() * 0.2, vec.getZ() * 0.2) * 3; }).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return MHelper.randRange(-2F, 2F, random); }).setSource(sphere);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(new SDFTranslate().setTranslate(0, -radius, 0).setSource(sphere));
		sphere.setReplaceFunction(REPLACE);
		sphere.setPostProcess((info) -> {
			if (info.getState().getBlock() instanceof LeavesBlock) {
				int distance = info.getPos().getManhattanDistance(pos);
				if (distance < 7) {
					return info.getState().with(LeavesBlock.DISTANCE, distance);
				}
				else {
					return AIR;
				}
			}
			return info.getState();
		});
		sphere.fillRecursive(world, pos);
		BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.DRAGON_TREE.bark);
		for (Direction d: Direction.values()) {
			BlockPos p = pos.offset(d);
			if (world.isAir(p)) {
				BlocksHelper.setWithoutUpdate(world, p, leaves);
			}
		}
		
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
