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
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFCappedCone;
import ru.betterend.util.sdf.primitive.SDFFlatland;
import ru.betterend.world.features.DefaultFeature;

public class ObsidianPillarBasementFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		pos = getPosOnSurface(world, new BlockPos(pos.getX() + random.nextInt(16), pos.getY(), pos.getZ() + random.nextInt(16)));
		if (!world.getBlockState(pos.down(5)).isIn(EndTags.GEN_TERRAIN)) {
			return false;
		}
		
		float height = MHelper.randRange(10F, 35F, random);
		float radius = MHelper.randRange(2F, 5F, random);
		SDF pillar = new SDFCappedCone().setRadius1(radius).setRadius2(radius).setHeight(height * 0.5F).setBlock(Blocks.OBSIDIAN);
		pillar = new SDFTranslate().setTranslate(0, height * 0.5F - 3, 0).setSource(pillar);
		SDF cut = new SDFFlatland().setBlock(Blocks.OBSIDIAN);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		cut = new SDFDisplacement().setFunction((vec) -> {
			return (float) (noise.eval(vec.getX() * 0.2, vec.getZ() * 0.2) * 3);
		}).setSource(cut);
		Vector3f vec = MHelper.randomHorizontal(random);
		float angle = random.nextFloat() * 0.5F + (float) Math.PI;
		cut = new SDFRotation().setRotation(vec, angle).setSource(cut);
		cut = new SDFTranslate().setTranslate(0, height * 0.7F - 3, 0).setSource(cut);
		pillar = new SDFSubtraction().setSourceA(pillar).setSourceB(cut);
		vec = MHelper.randomHorizontal(random);
		angle = random.nextFloat() * 0.2F;
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
