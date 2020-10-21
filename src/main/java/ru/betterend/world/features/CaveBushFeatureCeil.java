package ru.betterend.world.features;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFSphere;

public class CaveBushFeatureCeil extends InvertedScatterFeature {
	public CaveBushFeatureCeil(int radius) {
		super(radius);
	}

	private static final Function<BlockState, Boolean> REPLACE;

	@Override
	public boolean canGenerate(StructureWorldAccess world, Random random, BlockPos center, BlockPos blockPos, float radius) {
		return blockPos.getY() < 64 && world.isAir(blockPos) && world.getBlockState(blockPos.up()).isIn(BlockTagRegistry.GEN_TERRAIN);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		float radius = MHelper.randRange(0.8F, 2.5F, random);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(BlockRegistry.CAVE_BUSH);
		sphere = new SDFScale3D().setScale(MHelper.randRange(0.8F, 1.2F, random), MHelper.randRange(0.8F, 1.2F, random), MHelper.randRange(0.8F, 1.2F, random)).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return (float) noise.eval(vec.getX() * 0.2, vec.getY() * 0.2, vec.getZ() * 0.2) * 3; }).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return random.nextFloat() * 3F - 1.5F; }).setSource(sphere);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(new SDFTranslate().setTranslate(0, -radius, 0).setSource(sphere));
		sphere.setReplaceFunction(REPLACE);
		sphere.fillRecursive(world, blockPos.down());
		BlocksHelper.setWithoutUpdate(world, blockPos.down(), BlockRegistry.CAVE_BUSH);
	}
	
	static {
		REPLACE = (state) -> {
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}
}
