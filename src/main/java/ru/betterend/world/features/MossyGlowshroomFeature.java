package ru.betterend.world.features;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.sdf.ISDF;
import ru.betterend.util.sdf.operator.SDFFlatWave;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFLine;
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
		FUNCTION.fillRecursive(world, getTopPos(world, blockPos), REPLACE, 20, 50, 20);
		return true;
	}
	
	static {
		SDFLine stem = new SDFLine(Blocks.GOLD_BLOCK.getDefaultState()).setRadius(2F).setEnd(0, 15, 0);
		
		SDFSphere outerSphere = new SDFSphere(Blocks.REDSTONE_BLOCK.getDefaultState()).setRadius(10);
		SDFSphere innerSphere = new SDFSphere(Blocks.DIAMOND_BLOCK.getDefaultState()).setRadius(10);
		
		SDFTranslate sphereOffset = new SDFTranslate().setTranslate(0, -10F, 0);
		
		SDFFlatWave wave = new SDFFlatWave().setRaysCount(5).setIntensity(1.2F);
		ISDF head = new SDFSubtraction().setSourceA(outerSphere).setSourceB(sphereOffset.setSource(innerSphere));
		head = new SDFScale3D().setScale(1, 0.5F, 1).setSource(head);
		head = wave.setSource(head);
		
		SDFTranslate headOffset = new SDFTranslate().setTranslate(0, 15, 0);
		
		FUNCTION = new SDFSmoothUnion().setRadius(5).setSourceA(stem).setSourceB(headOffset.setSource(head));
		
		REPLACE = (state) -> {
			return state.getMaterial().isReplaceable();
		};
	}
}
