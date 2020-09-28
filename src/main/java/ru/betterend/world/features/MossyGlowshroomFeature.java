package ru.betterend.world.features;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.sdf.ISDF;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFCapsule;
import ru.betterend.util.sdf.primitive.SDFSphere;

public class MossyGlowshroomFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final ISDF FUNCTION;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig) {
		blockPos = getTopPos(world, blockPos);
		if (blockPos.getY() < 5) {
			return false;
		}
		if (!world.getBlockState(blockPos.down()).isIn(BlockTagRegistry.END_GROUND)) {
			return false;
		}
		FUNCTION.fillRecursive(world, getTopPos(world, blockPos), REPLACE, 10, 20, 10);
		return true;
	}
	
	static {
		SDFCapsule capsule = new SDFCapsule().setRadius(1.7F).setHeight(5);
		
		SDFSphere outerSphere = new SDFSphere().setRadius(10);
		SDFSphere innerSphere = new SDFSphere().setRadius(30);
		
		ISDF scaled1 = new SDFScale3D().setScale(1, 0.3F, 1).setSource(outerSphere);
		ISDF scaled2 = new SDFScale3D().setScale(1, 0.3F, 1).setSource(innerSphere);
		SDFTranslate sphereOffset = new SDFTranslate().setTranslate(0, -10F, 0);
		
		ISDF head = new SDFSubtraction().setSourceA(scaled1).setSourceB(sphereOffset.setSource(scaled2));
		
		SDFTranslate headOffset = new SDFTranslate().setTranslate(0, 10, 0);
		
		//FUNCTION = new SDFSmoothUnion().setRadius(3).setSourceA(capsule).setSourceB(headOffset.setSource(head));
		FUNCTION = headOffset.setSource(head);
		
		REPLACE = (state) -> {
			return state.getMaterial().isReplaceable();
		};
	}
}
