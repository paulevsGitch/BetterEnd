package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFCapedCone;

public class OverworldIslandFeature extends DefaultFeature {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(412);
	private static final Mutable CENTER = new Mutable();
	private static final SDF FUNCTION;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		CENTER.set(pos);
		FUNCTION.fillRecursive(world, pos.down());
		return true;
	}
	
	static {
		SDF cone = new SDFCapedCone().setRadius1(0).setRadius2(4).setHeight(4).setBlock(Blocks.STONE);
		cone = new SDFTranslate().setTranslate(0, -3, 0).setSource(cone);
		cone = new SDFDisplacement().setFunction((pos) -> {
			return (float) NOISE.eval(CENTER.getX() + pos.getX(), CENTER.getY() + pos.getY(), CENTER.getZ() + pos.getZ());
		});
		FUNCTION = cone;
	}
}
