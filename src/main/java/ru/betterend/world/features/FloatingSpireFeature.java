package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.terrain.SpireFeature;

public class FloatingSpireFeature extends SpireFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		int minY = getYOnSurface(world, pos.getX(), pos.getZ());
		int y = minY > 57 ? MHelper.floor(MHelper.randRange(minY, minY * 2, random) * 0.5F + 32) : MHelper.randRange(64, 192, random);
		pos = new BlockPos(pos.getX(), y, pos.getZ());
		
		SDF sdf = new SDFSphere().setRadius(MHelper.randRange(2, 3, random)).setBlock(Blocks.END_STONE);
		int count = MHelper.randRange(3, 5, random);
		
		for (int i = 0; i < count; i++) {
			float rMin = (i * 1.3F) + 2.5F;
			sdf = addSegment(sdf, MHelper.randRange(rMin, rMin + 1.5F, random), random);
		}
		for (int i = count - 1; i > 0; i--) {
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
}
