package ru.betterend.world.features.terrain;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
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
import ru.betterend.util.sdf.operator.SDFRotation;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFCappedCone;
import ru.betterend.world.features.DefaultFeature;

public class FallenPillarFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		pos = getPosOnSurface(world, new BlockPos(pos.getX() + random.nextInt(16), pos.getY(), pos.getZ() + random.nextInt(16)));
		if (!world.getBlockState(pos.down(5)).isIn(EndTags.GEN_TERRAIN)) {
			return false;
		}
		
		float height = MHelper.randRange(20F, 40F, random);
		float radius = MHelper.randRange(2F, 4F, random);
		SDF pillar = new SDFCappedCone().setRadius1(radius).setRadius2(radius).setHeight(height * 0.5F).setBlock(Blocks.OBSIDIAN);
		pillar = new SDFTranslate().setTranslate(0, radius * 0.5F - 2, 0).setSource(pillar);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		pillar = new SDFDisplacement().setFunction((vec) -> {
			return (float) (noise.eval(vec.getX() * 0.3, vec.getY() * 0.3, vec.getZ() * 0.3) * 0.5F);
		}).setSource(pillar);
		Vector3f vec = MHelper.randomHorizontal(random);
		float angle = (float) random.nextGaussian() * 0.05F + (float) Math.PI;
		pillar = new SDFRotation().setRotation(vec, angle).setSource(pillar);
		
		BlockState mossy = EndBlocks.MOSSY_OBSIDIAN.getDefaultState();
		pillar.addPostProcess((info) -> {
			if (info.getStateUp().isAir() && random.nextFloat() > 0.1F) {
				return mossy;
			}
			return info.getState();
		}).setReplaceFunction((state) -> {
			return state.getMaterial().isReplaceable() || state.isIn(EndTags.GEN_TERRAIN) || state.getMaterial().equals(Material.PLANT);
		}).fillRecursive(world, pos);
		
		return true;
	}
}
