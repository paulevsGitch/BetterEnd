package ru.betterend.world.features.terrain;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.TagAPI;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFRotation;
import ru.bclib.sdf.operator.SDFSubtraction;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.primitive.SDFCappedCone;
import ru.bclib.sdf.primitive.SDFFlatland;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;

import java.util.Random;

public class ObsidianPillarBasementFeature extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		pos = getPosOnSurface(
			world,
			new BlockPos(pos.getX() + random.nextInt(16), pos.getY(), pos.getZ() + random.nextInt(16))
		);
		if (!world.getBlockState(pos.below(5)).is(TagAPI.BLOCK_GEN_TERRAIN)) {
			return false;
		}
		
		float height = MHelper.randRange(10F, 35F, random);
		float radius = MHelper.randRange(2F, 5F, random);
		SDF pillar = new SDFCappedCone().setRadius1(radius)
										.setRadius2(radius)
										.setHeight(height * 0.5F)
										.setBlock(Blocks.OBSIDIAN);
		pillar = new SDFTranslate().setTranslate(0, height * 0.5F - 3, 0).setSource(pillar);
		SDF cut = new SDFFlatland().setBlock(Blocks.OBSIDIAN);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		cut = new SDFDisplacement().setFunction((vec) -> {
			return (float) (noise.eval(vec.x() * 0.2, vec.z() * 0.2) * 3);
		}).setSource(cut);
		Vector3f vec = MHelper.randomHorizontal(random);
		float angle = random.nextFloat() * 0.5F + (float) Math.PI;
		cut = new SDFRotation().setRotation(vec, angle).setSource(cut);
		cut = new SDFTranslate().setTranslate(0, height * 0.7F - 3, 0).setSource(cut);
		pillar = new SDFSubtraction().setSourceA(pillar).setSourceB(cut);
		vec = MHelper.randomHorizontal(random);
		angle = random.nextFloat() * 0.2F;
		pillar = new SDFRotation().setRotation(vec, angle).setSource(pillar);
		BlockState mossy = EndBlocks.MOSSY_OBSIDIAN.defaultBlockState();
		pillar.addPostProcess((info) -> {
			if (info.getStateUp().isAir() && random.nextFloat() > 0.1F) {
				return mossy;
			}
			return info.getState();
		}).setReplaceFunction((state) -> {
			return state.getMaterial().isReplaceable() || state.is(TagAPI.BLOCK_GEN_TERRAIN) || state.getMaterial()
																							   .equals(Material.PLANT);
		}).fillRecursive(world, pos);
		
		return true;
	}
}
