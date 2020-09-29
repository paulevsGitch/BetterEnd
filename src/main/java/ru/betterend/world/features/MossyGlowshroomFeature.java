package ru.betterend.world.features;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFBinary;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFCapedCone;
import ru.betterend.util.sdf.primitive.SDFSphere;

public class MossyGlowshroomFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final SDFBinary FUNCTION;
	private static final SDFTranslate HEAD_POS;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig) {
		blockPos = getTopPos(world, blockPos);
		if (blockPos.getY() < 5) {
			return false;
		}
		if (!world.getBlockState(blockPos.down()).isIn(BlockTagRegistry.END_GROUND)) {
			return false;
		}
		//FUNCTION.fillRecursive(world, getTopPos(world, blockPos), REPLACE, 20, 50, 20);
		
		float height = MHelper.randRange(10F, 25F, random);
		int count = MHelper.floor(height / 4);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, height, 0, count);
		SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
		SDF sdf = SplineHelper.buildSDF(spline, 2.1F, 1.5F, (pos) -> {
			return BlockRegistry.MOSSY_GLOWSHROOM.log.getDefaultState();
		});
		Vector3f pos = spline.get(spline.size() - 1);
		BlockPos up = blockPos.add(pos.getX(), pos.getY(), pos.getZ());
		HEAD_POS.setTranslate(pos.getX(), pos.getY(), pos.getZ());
		FUNCTION.setSourceA(sdf);
		FUNCTION.fillRecursive(world, blockPos, REPLACE);
		BlocksHelper.setWithoutUpdate(world, up, Blocks.DIAMOND_BLOCK.getDefaultState());
		
		return true;
	}
	
	static {
		/*SDFLine stem = new SDFLine(Blocks.GOLD_BLOCK.getDefaultState()).setRadius(2F).setEnd(0, 15, 0);
		
		SDFSphere outerSphere = new SDFSphere(Blocks.REDSTONE_BLOCK.getDefaultState()).setRadius(10);
		SDFSphere innerSphere = new SDFSphere(Blocks.DIAMOND_BLOCK.getDefaultState()).setRadius(10);
		
		SDFTranslate sphereOffset = new SDFTranslate().setTranslate(0, -10F, 0);
		
		SDFFlatWave wave = new SDFFlatWave().setRaysCount(5).setIntensity(1.2F);
		ISDF head = new SDFSubtraction().setSourceA(outerSphere).setSourceB(sphereOffset.setSource(innerSphere));
		head = new SDFScale3D().setScale(1, 0.5F, 1).setSource(head);
		head = wave.setSource(head);
		
		SDFTranslate headOffset = new SDFTranslate().setTranslate(0, 15, 0);
		
		FUNCTION = new SDFSmoothUnion().setRadius(5).setSourceA(stem).setSourceB(headOffset.setSource(head));*/
		
		SDFCapedCone cone = new SDFCapedCone(Blocks.GOLD_BLOCK.getDefaultState()).setHeight(10).setRadius1(0).setRadius2(10);
		//SDFSphere sphere = new SDFSphere(Blocks.GOLD_BLOCK.getDefaultState()).setRadius(10);
		HEAD_POS = (SDFTranslate) new SDFTranslate().setSource(cone);
		
		FUNCTION = new SDFUnion().setSourceB(HEAD_POS);
		FUNCTION.setPostProcess((info) -> {
			if (BlockRegistry.MOSSY_GLOWSHROOM.isTreeLog(info.getState())) {
				if (!BlockRegistry.MOSSY_GLOWSHROOM.isTreeLog(info.getStateUp())) {
					return BlockRegistry.MOSSY_GLOWSHROOM.bark.getDefaultState();
				}
				else if (!BlockRegistry.MOSSY_GLOWSHROOM.isTreeLog(info.getStateDown())) {
					return BlockRegistry.MOSSY_GLOWSHROOM.bark.getDefaultState();
				}
			}
			return info.getState();
		});
		
		REPLACE = (state) -> {
			return state.getMaterial().isReplaceable();
		};
	}
}
