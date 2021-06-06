package ru.betterend.world.features.terrain;

import java.util.Random;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.TagAPI;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFRotation;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.primitive.SDFCappedCone;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;

public class FallenPillarFeature extends DefaultFeature {
	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoneFeatureConfiguration config) {
		pos = getPosOnSurface(world,
				new BlockPos(pos.getX() + random.nextInt(16), pos.getY(), pos.getZ() + random.nextInt(16)));
		if (!world.getBlockState(pos.below(5)).is(TagAPI.GEN_TERRAIN)) {
			return false;
		}

		float height = MHelper.randRange(20F, 40F, random);
		float radius = MHelper.randRange(2F, 4F, random);
		SDF pillar = new SDFCappedCone().setRadius1(radius).setRadius2(radius).setHeight(height * 0.5F)
				.setBlock(Blocks.OBSIDIAN);
		pillar = new SDFTranslate().setTranslate(0, radius * 0.5F - 2, 0).setSource(pillar);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		pillar = new SDFDisplacement().setFunction((vec) -> {
			return (float) (noise.eval(vec.x() * 0.3, vec.y() * 0.3, vec.z() * 0.3) * 0.5F);
		}).setSource(pillar);
		Vector3f vec = MHelper.randomHorizontal(random);
		float angle = (float) random.nextGaussian() * 0.05F + (float) Math.PI;
		pillar = new SDFRotation().setRotation(vec, angle).setSource(pillar);

		BlockState mossy = EndBlocks.MOSSY_OBSIDIAN.defaultBlockState();
		pillar.addPostProcess((info) -> {
			if (info.getStateUp().isAir() && random.nextFloat() > 0.1F) {
				return mossy;
			}
			return info.getState();
		}).setReplaceFunction((state) -> {
			return state.getMaterial().isReplaceable() || state.is(TagAPI.GEN_TERRAIN)
					|| state.getMaterial().equals(Material.PLANT);
		}).fillRecursive(world, pos);

		return true;
	}
}
