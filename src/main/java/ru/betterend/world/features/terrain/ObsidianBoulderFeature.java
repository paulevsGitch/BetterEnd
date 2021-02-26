package ru.betterend.world.features.terrain;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class ObsidianBoulderFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		pos = getPosOnSurface(world, new BlockPos(pos.getX() + random.nextInt(16), pos.getY(), pos.getZ() + random.nextInt(16)));
		if (!world.getBlockState(pos.down()).isIn(EndTags.END_GROUND)) {
			return false;
		}
		
		int count = MHelper.randRange(1, 5, random);
		for (int i = 0; i < count; i++) {
			BlockPos p = getPosOnSurface(world, new BlockPos(pos.getX() + random.nextInt(16) - 8, pos.getY(), pos.getZ() + random.nextInt(16) - 8));
			makeBoulder(world, p, random);
		}
		
		return true;
	}
	
	private void makeBoulder(StructureWorldAccess world, BlockPos pos, Random random) {
		if (!world.getBlockState(pos.down()).isIn(EndTags.END_GROUND)) {
			return;
		}
		
		float radius = MHelper.randRange(1F, 5F, random);
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(Blocks.OBSIDIAN);
		float sx = MHelper.randRange(0.7F, 1.3F, random);
		float sy = MHelper.randRange(0.7F, 1.3F, random);
		float sz = MHelper.randRange(0.7F, 1.3F, random);
		sphere = new SDFScale3D().setScale(sx, sy, sz).setSource(sphere);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		sphere = new SDFDisplacement().setFunction((vec) -> {
			return (float) (noise.eval(vec.getX() * 0.2, vec.getY() * 0.2, vec.getZ() * 0.2) * 1.5F);
		}).setSource(sphere);
		
		BlockState mossy = EndBlocks.MOSSY_OBSIDIAN.getDefaultState();
		sphere.addPostProcess((info) -> {
			if (info.getStateUp().isAir() && random.nextFloat() > 0.1F) {
				return mossy;
			}
			return info.getState();
		}).setReplaceFunction((state) -> {
			return state.getMaterial().isReplaceable() || state.isIn(EndTags.GEN_TERRAIN) || state.getMaterial().equals(Material.PLANT);
		}).fillRecursive(world, pos);
	}
}
