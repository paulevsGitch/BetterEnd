package ru.betterend.world.features.terrain;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class SpireFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		pos = getPosOnSurfaceWG(world, pos);
		if (pos.getY() > 57) {
			SDF sdf = new SDFSphere().setRadius(MHelper.randRange(2, 3, random)).setBlock(Blocks.END_STONE);
			int count = MHelper.randRange(3, 7, random);
			for (int i = 0; i < count; i++) {
				float rMin = (i * 1.3F) + 2.5F;
				sdf = addSegment(sdf, MHelper.randRange(rMin, rMin + 1.5F, random), random);
			}
			OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
			sdf = new SDFDisplacement().setFunction((vec) -> {
				return (float) (Math.abs(noise.eval(vec.getX() * 0.1, vec.getY() * 0.1, vec.getZ() * 0.1)) * 3F + Math.abs(noise.eval(vec.getX() * 0.3, vec.getY() * 0.3 + 100, vec.getZ() * 0.3)) * 1.3F);
			}).setSource(sdf);
			final BlockPos center = pos;
			sdf.setReplaceFunction(REPLACE).setPostProcess((info) -> {
				return info.getStateUp().isAir() ? EndBlocks.END_MOSS.getDefaultState() : info.getState();
			});
			sdf.fillRecursive(world, center);
			return true;
		}
		return false;
	}
	
	private SDF addSegment(SDF sdf, float radius, Random random) {
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(Blocks.END_STONE);
		SDF offseted = new SDFTranslate().setTranslate(0, radius + random.nextFloat() * 0.25F * radius, 0).setSource(sdf);
		return new SDFSmoothUnion().setRadius(radius * 0.5F).setSourceA(sphere).setSourceB(offseted);
	}
	
	static {
		REPLACE = (state) -> {
			if (state.isIn(EndTags.END_GROUND)) {
				return true;
			}
			if (state.getBlock() instanceof LeavesBlock) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}
}
