package ru.betterend.world.features.trees;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.MossyGlowshroomCapBlock;
import ru.betterend.blocks.basis.FurBlock;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFBinary;
import ru.betterend.util.sdf.operator.SDFCoordModify;
import ru.betterend.util.sdf.operator.SDFFlatWave;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFCappedCone;
import ru.betterend.util.sdf.primitive.SDFPrimitive;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

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
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig) {
		BlockState down = world.getBlockState(blockPos.down());
		if (!down.isOf(EndBlocks.END_MYCELIUM) && !down.isOf(EndBlocks.END_MOSS)) return false;
		
		CONE1.setBlock(EndBlocks.MOSSY_GLOWSHROOM_CAP);
		CONE2.setBlock(EndBlocks.MOSSY_GLOWSHROOM_CAP);
		CONE_GLOW.setBlock(EndBlocks.MOSSY_GLOWSHROOM_HYMENOPHORE);
		ROOTS.setBlock(EndBlocks.MOSSY_GLOWSHROOM.bark);
		
		float height = MHelper.randRange(10F, 25F, random);
		int count = MHelper.floor(height / 4);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, height, 0, count);
		SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
		SDF sdf = SplineHelper.buildSDF(spline, 2.1F, 1.5F, (pos) -> {
			return EndBlocks.MOSSY_GLOWSHROOM.log.getDefaultState();
		});
		Vector3f pos = spline.get(spline.size() - 1);
		float scale = MHelper.randRange(0.75F, 1.1F, random);
		
		if (!SplineHelper.canGenerate(spline, scale, blockPos, world, REPLACE)) {
			return false;
		}
		BlocksHelper.setWithoutUpdate(world, blockPos, AIR);
		
		CENTER.set(blockPos.getX(), 0, blockPos.getZ());
		HEAD_POS.setTranslate(pos.getX(), pos.getY(), pos.getZ());
		ROOTS_ROT.setAngle(random.nextFloat() * MHelper.PI2);
		FUNCTION.setSourceA(sdf);
		
		new SDFScale().setScale(scale)
				.setSource(FUNCTION)
				.setReplaceFunction(REPLACE)
				.addPostProcess((info) -> {
					if (EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getState())) {
						if (random.nextBoolean() && info.getStateUp().getBlock() == EndBlocks.MOSSY_GLOWSHROOM_CAP) {
							info.setState(EndBlocks.MOSSY_GLOWSHROOM_CAP.getDefaultState().with(MossyGlowshroomCapBlock.TRANSITION, true));
							return info.getState();
						}
						else if (!EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getStateUp()) || !EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getStateDown())) {
							info.setState(EndBlocks.MOSSY_GLOWSHROOM.bark.getDefaultState());
							return info.getState();
						}
					}
					else if (info.getState().getBlock() == EndBlocks.MOSSY_GLOWSHROOM_CAP) {
						if (EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(info.getStateDown().getBlock())) {
							info.setState(EndBlocks.MOSSY_GLOWSHROOM_CAP.getDefaultState().with(MossyGlowshroomCapBlock.TRANSITION, true));
							return info.getState();
						}
						
						info.setState(EndBlocks.MOSSY_GLOWSHROOM_CAP.getDefaultState());
						return info.getState();
					}
					else if (info.getState().getBlock() == EndBlocks.MOSSY_GLOWSHROOM_HYMENOPHORE) {
						for (Direction dir: BlocksHelper.HORIZONTAL) {
							if (info.getState(dir) == AIR) {
								info.setBlockPos(info.getPos().offset(dir), EndBlocks.MOSSY_GLOWSHROOM_FUR.getDefaultState().with(FurBlock.FACING, dir));
							}
						}
						
						if (info.getStateDown().getBlock() != EndBlocks.MOSSY_GLOWSHROOM_HYMENOPHORE) {
							info.setBlockPos(info.getPos().down(), EndBlocks.MOSSY_GLOWSHROOM_FUR.getDefaultState().with(FurBlock.FACING, Direction.DOWN));
						}
					}
					return info.getState();
				})
				.fillRecursive(world, blockPos);
		
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
			float dist = MHelper.length(pos.getX(), pos.getZ());
			float y = pos.getY() + (float) noise.eval(pos.getX() * 0.1 + CENTER.getX(), pos.getZ() * 0.1 + CENTER.getZ()) * dist * 0.3F - dist * 0.15F;
			pos.set(pos.getX(), y, pos.getZ());
		}).setSource(cones);
		
		HEAD_POS = (SDFTranslate) new SDFTranslate().setSource(new SDFTranslate().setTranslate(0, 2.5F, 0).setSource(cones));
		
		SDF roots = new SDFSphere().setRadius(4F);
		ROOTS = (SDFPrimitive) roots;
		roots = new SDFScale3D().setScale(1, 0.7F, 1).setSource(roots);
		ROOTS_ROT = (SDFFlatWave) new SDFFlatWave().setRaysCount(5).setIntensity(1.5F).setSource(roots);
		
		FUNCTION = new SDFSmoothUnion().setRadius(4).setSourceB(new SDFUnion().setSourceA(HEAD_POS).setSourceB(ROOTS_ROT));
		
		REPLACE = (state) -> {
			if (state.isIn(EndTags.END_GROUND)) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}
}
