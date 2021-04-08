package ru.betterend.world.features.terrain;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFCoordModify;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

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
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			DefaultFeatureConfig config) {
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
			double x = (vec.getX() + pos.getX()) * 0.1;
			double z = (vec.getZ() + pos.getZ()) * 0.1;
			double offset = noise.eval(x, z);
			vec.set(vec.getX(), vec.getY() + (float) offset * 8, vec.getZ());
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
