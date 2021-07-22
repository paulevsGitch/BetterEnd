package ru.betterend.world.features.terrain;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import ru.bclib.api.TagAPI;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFDisplacement;
import ru.bclib.sdf.operator.SDFRotation;
import ru.bclib.sdf.primitive.SDFTorus;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.noise.OpenSimplexNoise;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class ArchFeature extends DefaultFeature {
	private Function<BlockPos, BlockState> surfaceFunction;
	private Block block;
	
	public ArchFeature(Block block, Function<BlockPos, BlockState> surfaceFunction) {
		this.surfaceFunction = surfaceFunction;
		this.block = block;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		final WorldGenLevel world = featurePlaceContext.level();
		BlockPos origin = featurePlaceContext.origin();
		Random random = featurePlaceContext.random();
		
		BlockPos pos = getPosOnSurfaceWG(
			world,
			new BlockPos((origin.getX() & 0xFFFFFFF0) | 7, 0, (origin.getZ() & 0xFFFFFFF0) | 7)
		);
		if (!world.getBlockState(pos.below(5)).is(TagAPI.BLOCK_GEN_TERRAIN)) {
			return false;
		}
		
		float bigRadius = MHelper.randRange(10F, 20F, random);
		float smallRadius = MHelper.randRange(3F, 7F, random);
		if (smallRadius + bigRadius > 23) {
			smallRadius = 23 - bigRadius;
		}
		SDF arch = new SDFTorus().setBigRadius(bigRadius).setSmallRadius(smallRadius).setBlock(block);
		arch = new SDFRotation().setRotation(MHelper.randomHorizontal(random), (float) Math.PI * 0.5F).setSource(arch);
		
		final float smallRadiusF = smallRadius;
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
		arch = new SDFDisplacement().setFunction((vec) -> {
			return (float) (Math.abs(noise.eval(vec.x() * 0.1,
				vec.y() * 0.1,
				vec.z() * 0.1
			)) * 3F + Math.abs(noise.eval(
				vec.x() * 0.3,
				vec.y() * 0.3 + 100,
				vec.z() * 0.3
			)) * 1.3F) - smallRadiusF * Math.abs(1 - vec.y() / bigRadius);
		}).setSource(arch);
		
		List<BlockPos> surface = Lists.newArrayList();
		arch.addPostProcess((info) -> {
			if (info.getStateUp().isAir()) {
				return surfaceFunction.apply(info.getPos());
			}
			return info.getState();
		});
		
		float side = (bigRadius + smallRadius + 3F) * 2;
		if (side > 47) {
			side = 47;
		}
		arch.fillArea(world, pos, AABB.ofSize(Vec3.atCenterOf(pos), side, side, side));
		
		return true;
	}
}
