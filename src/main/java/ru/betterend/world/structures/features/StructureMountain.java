package ru.betterend.world.structures.features;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import ru.betterend.noise.VoronoiNoise;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFCapedCone;

public class StructureMountain extends SDFStructureFeature {
	@Override
	protected SDF getSDF(BlockPos center, Random random) {
		SDFCapedCone cone1 = new SDFCapedCone().setHeight(20F).setRadius1(40F).setRadius2(0F);
		SDFCapedCone cone2 = new SDFCapedCone().setHeight(10F).setRadius1(0F).setRadius2(40F);
		cone1.setBlock(Blocks.END_STONE);
		cone2.setBlock(Blocks.END_STONE);
		SDF mountain = new SDFSmoothUnion().setRadius(15)
				.setSourceA(new SDFTranslate().setTranslate(0, 20, 1).setSource(cone1))
				.setSourceB(new SDFTranslate().setTranslate(0, -10, 1).setSource(cone2));
		mountain = new SDFScale().setScale(MHelper.randRange(1F, 2.5F, random)).setSource(mountain);
		VoronoiNoise noise = new VoronoiNoise(random.nextInt(), 20, 0.75);
		mountain = new SDFDisplacement().setFunction((pos) -> {
			return (float) noise.sample(pos.getX(), pos.getY(), pos.getZ()) * 15F;
		}).setSource(mountain);
		return mountain;
	}
}
