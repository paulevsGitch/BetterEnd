package ru.betterend.world.features.terrain;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFCoordModify;
import ru.bclib.sdf.operator.SDFScale3D;
import ru.bclib.sdf.primitive.SDFSphere;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.noise.OpenSimplexNoise;

public class OreLayerFeature extends DefaultFeature {
	private static final SDFSphere SPHERE;
	private static final SDFCoordModify NOISE;
	private static final SDF FUNCTION;

	private final BlockState state;
	private final float radius;
	private final int minY;
	private final int maxY;
	private OpenSimplexNoise noise;

	public OreLayerFeature(BlockState state, float radius, int minY, int maxY) {
		this.state = state;
		this.radius = radius;
		this.minY = minY;
		this.maxY = maxY;
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featureConfig) {
		final Random random = featureConfig.random();
		final BlockPos pos = featureConfig.origin();
		final WorldGenLevel world = featureConfig.level();
		float radius = this.radius * 0.5F;
		int r = MHelper.floor(radius + 1);
		int posX = MHelper.randRange(Math.max(r - 16, 0), Math.min(31 - r, 15), random) + pos.getX();
		int posZ = MHelper.randRange(Math.max(r - 16, 0), Math.min(31 - r, 15), random) + pos.getZ();
		int posY = MHelper.randRange(minY, maxY, random);

		if (noise == null) {
			noise = new OpenSimplexNoise(world.getSeed());
		}

		SPHERE.setRadius(radius).setBlock(state);
		NOISE.setFunction((vec) -> {
			double x = (vec.x() + pos.getX()) * 0.1;
			double z = (vec.z() + pos.getZ()) * 0.1;
			double offset = noise.eval(x, z);
			vec.set(vec.x(), vec.y() + (float) offset * 8, vec.z());
		});
		FUNCTION.fillRecursive(world, new BlockPos(posX, posY, posZ));
		return true;
	}

	static {
		SPHERE = new SDFSphere();
		NOISE = new SDFCoordModify();

		SDF body = SPHERE;
		body = new SDFScale3D().setScale(1, 0.2F, 1).setSource(body);
		body = NOISE.setSource(body);
		body.setReplaceFunction((state) -> {
			return state.is(Blocks.END_STONE);
		});

		FUNCTION = body;
	}
}
