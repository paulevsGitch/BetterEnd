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
import ru.betterend.util.sdf.primitive.SDFCappedCone;

public class OverworldIslandFeature extends DefaultFeature {
	private static final OpenSimplexNoise NOISE = new OpenSimplexNoise(412);
	private static final Mutable CENTER = new Mutable();
	private static final SDF ISLAND;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		CENTER.set(pos);
		ISLAND.fillRecursive(world, pos.down());
		return true;
	}
	
	static {
		SDF cone = new SDFCappedCone().setRadius1(0).setRadius2(6).setHeight(4).setBlock((pos) -> {
			if (pos.getY() == CENTER.getY()) return Blocks.GRASS_BLOCK.getDefaultState();
			if (pos.getY() == CENTER.getY() - 1) {
				return Blocks.DIRT.getDefaultState();
			} else if (pos.getY() == CENTER.getY() - Math.round(2.0 * Math.random())) {
				return Blocks.DIRT.getDefaultState();
			}
			return Blocks.STONE.getDefaultState();
		});
		cone = new SDFTranslate().setTranslate(0, -3, 0).setSource(cone);
		cone = new SDFDisplacement().setFunction((pos) -> {
			return (float) NOISE.eval(CENTER.getX() + pos.getX(), CENTER.getY() + pos.getY(), CENTER.getZ() + pos.getZ());
		}).setSource(cone).setReplaceFunction(state -> {
			return state.isOf(Blocks.WATER) || state.getMaterial().isReplaceable();
		});
		ISLAND = cone;
	}
}
