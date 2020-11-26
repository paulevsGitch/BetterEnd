package ru.betterend.world.features.terrain;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndFeatures;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.primitive.SDFSphere;

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
		List<BlockPos> support = Lists.newArrayList();
		sdf.setReplaceFunction(REPLACE).setPostProcess((info) -> {
			if (info.getStateUp().isAir()) {
				if (random.nextInt(16) == 0) {
					support.add(info.getPos().up());
				}
				return world.getBiome(info.getPos()).getGenerationSettings().getSurfaceConfig().getTopMaterial();
			}
			return info.getState();
		});
		sdf.fillRecursive(world, center);
		
		support.forEach((bpos) -> {
			if (EndBiomes.getFromBiome(world.getBiome(bpos)) == EndBiomes.BLOSSOMING_SPIRES) {
				EndFeatures.TENANEA_BUSH.getFeature().generate(world, chunkGenerator, random, bpos, null);
			}
		});
		
		return true;
	}
}
