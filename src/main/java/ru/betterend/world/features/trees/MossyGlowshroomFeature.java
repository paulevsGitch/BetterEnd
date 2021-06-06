package ru.betterend.world.features.trees;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.api.TagAPI;
import ru.bclib.sdf.SDF;
import ru.bclib.sdf.operator.SDFBinary;
import ru.bclib.sdf.operator.SDFCoordModify;
import ru.bclib.sdf.operator.SDFFlatWave;
import ru.bclib.sdf.operator.SDFScale;
import ru.bclib.sdf.operator.SDFScale3D;
import ru.bclib.sdf.operator.SDFSmoothUnion;
import ru.bclib.sdf.operator.SDFSubtraction;
import ru.bclib.sdf.operator.SDFTranslate;
import ru.bclib.sdf.operator.SDFUnion;
import ru.bclib.sdf.primitive.SDFCappedCone;
import ru.bclib.sdf.primitive.SDFPrimitive;
import ru.bclib.sdf.primitive.SDFSphere;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.util.SplineHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.blocks.MossyGlowshroomCapBlock;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;

public class MossyGlowshroomFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Vector3f CENTER = new Vector3f();
	private static final SDFBinary FUNCTION;
	private static final SDFTranslate HEAD_POS;
	private static final SDFFlatWave ROOTS_ROT;

	private static final SDFPrimitive CONE1;
	private static final SDFPrimitive CONE2;
	private static final SDFPrimitive CONE_GLOW;
	private static final SDFPrimitive ROOTS;

	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos,
			NoneFeatureConfiguration featureConfig) {
		BlockState down = world.getBlockState(blockPos.below());
		if (!down.is(EndBlocks.END_MYCELIUM) && !down.is(EndBlocks.END_MOSS))
			return false;

		CONE1.setBlock(EndBlocks.MOSSY_GLOWSHROOM_CAP);
		CONE2.setBlock(EndBlocks.MOSSY_GLOWSHROOM_CAP);
		CONE_GLOW.setBlock(EndBlocks.MOSSY_GLOWSHROOM_HYMENOPHORE);
		ROOTS.setBlock(EndBlocks.MOSSY_GLOWSHROOM.bark);

		float height = MHelper.randRange(10F, 25F, random);
		int count = MHelper.floor(height / 4);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, height, 0, count);
		SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
		SDF sdf = SplineHelper.buildSDF(spline, 2.1F, 1.5F, (pos) -> {
			return EndBlocks.MOSSY_GLOWSHROOM.log.defaultBlockState();
		});
		Vector3f pos = spline.get(spline.size() - 1);
		float scale = MHelper.randRange(0.75F, 1.1F, random);

		if (!SplineHelper.canGenerate(spline, scale, blockPos, world, REPLACE)) {
			return false;
		}
		BlocksHelper.setWithoutUpdate(world, blockPos, AIR);

		CENTER.set(blockPos.getX(), 0, blockPos.getZ());
		HEAD_POS.setTranslate(pos.x(), pos.y(), pos.z());
		ROOTS_ROT.setAngle(random.nextFloat() * MHelper.PI2);
		FUNCTION.setSourceA(sdf);

		new SDFScale().setScale(scale).setSource(FUNCTION).setReplaceFunction(REPLACE).addPostProcess((info) -> {
			if (EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getState())) {
				if (random.nextBoolean() && info.getStateUp().getBlock() == EndBlocks.MOSSY_GLOWSHROOM_CAP) {
					info.setState(EndBlocks.MOSSY_GLOWSHROOM_CAP.defaultBlockState()
							.setValue(MossyGlowshroomCapBlock.TRANSITION, true));
					return info.getState();
				}
				else if (!EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getStateUp())
						|| !EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getStateDown())) {
					info.setState(EndBlocks.MOSSY_GLOWSHROOM.bark.defaultBlockState());
					return info.getState();
				}
			}
			else if (info.getState().getBlock() == EndBlocks.MOSSY_GLOWSHROOM_CAP) {
				if (EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getStateDown().getBlock())) {
					info.setState(EndBlocks.MOSSY_GLOWSHROOM_CAP.defaultBlockState()
							.setValue(MossyGlowshroomCapBlock.TRANSITION, true));
					return info.getState();
				}

				info.setState(EndBlocks.MOSSY_GLOWSHROOM_CAP.defaultBlockState());
				return info.getState();
			}
			else if (info.getState().getBlock() == EndBlocks.MOSSY_GLOWSHROOM_HYMENOPHORE) {
				for (Direction dir : BlocksHelper.HORIZONTAL) {
					if (info.getState(dir) == AIR) {
						info.setBlockPos(info.getPos().relative(dir),
								EndBlocks.MOSSY_GLOWSHROOM_FUR.defaultBlockState().setValue(FurBlock.FACING, dir));
					}
				}

				if (info.getStateDown().getBlock() != EndBlocks.MOSSY_GLOWSHROOM_HYMENOPHORE) {
					info.setBlockPos(info.getPos().below(), EndBlocks.MOSSY_GLOWSHROOM_FUR.defaultBlockState()
							.setValue(FurBlock.FACING, Direction.DOWN));
				}
			}
			return info.getState();
		}).fillRecursive(world, blockPos);

		return true;
	}

	static {
		SDFCappedCone cone1 = new SDFCappedCone().setHeight(2.5F).setRadius1(1.5F).setRadius2(2.5F);
		SDFCappedCone cone2 = new SDFCappedCone().setHeight(3F).setRadius1(2.5F).setRadius2(13F);
		SDF posedCone2 = new SDFTranslate().setTranslate(0, 5, 0).setSource(cone2);
		SDF posedCone3 = new SDFTranslate().setTranslate(0, 12F, 0).setSource(new SDFScale().setScale(2).setSource(cone2));
		SDF upCone = new SDFSubtraction().setSourceA(posedCone2).setSourceB(posedCone3);
		SDF wave = new SDFFlatWave().setRaysCount(12).setIntensity(1.3F).setSource(upCone);
		SDF cones = new SDFSmoothUnion().setRadius(3).setSourceA(cone1).setSourceB(wave);

		CONE1 = cone1;
		CONE2 = cone2;

		SDF innerCone = new SDFTranslate().setTranslate(0, 1.25F, 0).setSource(upCone);
		innerCone = new SDFScale3D().setScale(1.2F, 1F, 1.2F).setSource(innerCone);
		cones = new SDFUnion().setSourceA(cones).setSourceB(innerCone);

		SDF glowCone = new SDFCappedCone().setHeight(3F).setRadius1(2F).setRadius2(12.5F);
		CONE_GLOW = (SDFPrimitive) glowCone;
		glowCone = new SDFTranslate().setTranslate(0, 4.25F, 0).setSource(glowCone);
		glowCone = new SDFSubtraction().setSourceA(glowCone).setSourceB(posedCone3);

		cones = new SDFUnion().setSourceA(cones).setSourceB(glowCone);

		OpenSimplexNoise noise = new OpenSimplexNoise(1234);
		cones = new SDFCoordModify().setFunction((pos) -> {
			float dist = MHelper.length(pos.x(), pos.z());
			float y = pos.y() + (float) noise.eval(pos.x() * 0.1 + CENTER.x(), pos.z() * 0.1 + CENTER.z()) * dist * 0.3F
					- dist * 0.15F;
			pos.set(pos.x(), y, pos.z());
		}).setSource(cones);

		HEAD_POS = (SDFTranslate) new SDFTranslate()
				.setSource(new SDFTranslate().setTranslate(0, 2.5F, 0).setSource(cones));

		SDF roots = new SDFSphere().setRadius(4F);
		ROOTS = (SDFPrimitive) roots;
		roots = new SDFScale3D().setScale(1, 0.7F, 1).setSource(roots);
		ROOTS_ROT = (SDFFlatWave) new SDFFlatWave().setRaysCount(5).setIntensity(1.5F).setSource(roots);

		FUNCTION = new SDFSmoothUnion().setRadius(4)
				.setSourceB(new SDFUnion().setSourceA(HEAD_POS).setSourceB(ROOTS_ROT));

		REPLACE = (state) -> {
			if (state.is(TagAPI.END_GROUND)) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}
}
