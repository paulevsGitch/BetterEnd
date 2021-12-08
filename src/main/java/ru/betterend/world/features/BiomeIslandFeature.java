package ru.betterend.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.primitive.SDFCappedCone;
import ru.bclib.util.BlocksHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.world.biome.EndBiome;

public class BiomeIslandFeature extends DefaultFeature {
	private static final MutableBlockPos CENTER = new MutableBlockPos();
	private static final SDF ISLAND;
	
	private static OpenSimplexNoise simplexNoise = new OpenSimplexNoise(412L);
	private static BlockState topBlock = Blocks.GRASS_BLOCK.defaultBlockState();
	private static BlockState underBlock = Blocks.DIRT.defaultBlockState();

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		Biome biome = world.getBiome(pos);
        int dist = BlocksHelper.downRay(world, pos, 10) + 1;
		BlockPos surfacePos = new BlockPos(pos.getX(), pos.getY()-dist, pos.getZ());
		BlockState topMaterial = EndBiome.findTopMaterial(world, surfacePos);;

		//TODO: 1.18 the block selection should be based on the surface rules of the biome
		if (BlocksHelper.isFluid(topMaterial)) {
			topBlock = Blocks.GRAVEL.defaultBlockState();
			underBlock = Blocks.STONE.defaultBlockState();
		}
		else {
			underBlock = EndBiome.findUnderMaterial(world, surfacePos);
		}

		simplexNoise = new OpenSimplexNoise(world.getSeed());
		CENTER.set(pos);
		ISLAND.fillRecursive(world, pos.below());
		return true;
	}
	
	private static SDF createSDFIsland() {
		SDF sdfCone = new SDFCappedCone().setRadius1(0).setRadius2(6).setHeight(4).setBlock(pos -> {
			if (pos.getY() > CENTER.getY()) return AIR;
			if (pos.getY() == CENTER.getY()) return topBlock;
			return underBlock;
		});
		sdfCone = new SDFTranslate().setTranslate(0, -2, 0).setSource(sdfCone);
		sdfCone = new SDFDisplacement().setFunction(pos -> {
			float deltaX = Math.abs(pos.x());
			float deltaY = Math.abs(pos.y());
			float deltaZ = Math.abs(pos.z());
			if (deltaY < 2.0f && (deltaX < 3.0f || deltaZ < 3.0F)) return 0.0f;
			return (float) simplexNoise.eval(CENTER.getX() + pos.x(), CENTER.getY() + pos.y(), CENTER.getZ() + pos.z());
		})
									   .setSource(sdfCone)
									   .setReplaceFunction(state -> BlocksHelper.isFluid(state) || state.getMaterial()
																										.isReplaceable());
		return sdfCone;
	}
	
	static {
		ISLAND = createSDFIsland();
	}
}
