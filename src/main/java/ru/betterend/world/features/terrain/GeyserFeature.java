package ru.betterend.world.features.terrain;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFIntersection;
import ru.betterend.util.sdf.operator.SDFRotation;
import ru.betterend.util.sdf.operator.SDFScale;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFCapedCone;
import ru.betterend.util.sdf.primitive.SDFFlatland;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class GeyserFeature extends DefaultFeature {
	protected static final Function<BlockState, Boolean> REPLACE;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		pos = getPosOnSurfaceWG(world, pos);
		
		if (pos.getY() > 57) {
			int halfHeight = MHelper.randRange(10, 20, random);
			float radius1 = halfHeight * 0.5F;
			float radius2 = halfHeight * 0.1F + 0.5F;
			SDF sdf = new SDFCapedCone().setHeight(halfHeight).setRadius1(radius1).setRadius2(radius2).setBlock(EndBlocks.SULFURIC_ROCK.stone);
			sdf = new SDFTranslate().setTranslate(0, halfHeight - 3, 0).setSource(sdf);
			
			SDF bowl = new SDFSphere().setRadius(10).setBlock(EndBlocks.SULFURIC_ROCK.stone);
			bowl = new SDFSubtraction().setSourceA(bowl).setSourceB(new SDFScale().setScale(0.7F).setSource(bowl));
			bowl = new SDFIntersection().setSourceA(bowl).setSourceB(new SDFFlatland().setBlock(EndBlocks.SULFURIC_ROCK.stone));
			bowl = new SDFScale3D().setScale(1, 0.4F, 1).setSource(bowl);
			bowl = new SDFTranslate().setTranslate(5, 0, 0).setSource(bowl);
			int count = halfHeight >> 1;
			for (int i = 0; i < count; i++) {
				int py = i << 2;
				float delta = (float) i / (float) (count - 1);
				float offset = MathHelper.lerp(delta, radius1, radius2);
				float scale = (1 - delta * 0.5F) * halfHeight / 20F;
				
				SDF bowlPositioned = new SDFScale().setScale(scale).setSource(bowl);
				bowlPositioned = new SDFTranslate().setTranslate(offset, py, 0).setSource(bowlPositioned);
				bowlPositioned = new SDFRotation().setRotation(Vector3f.POSITIVE_Y, i * 1.5F).setSource(bowlPositioned);
				sdf = new SDFSmoothUnion().setRadius(10).setSourceA(bowlPositioned).setSourceB(sdf);
				//sdf = new SDFUnion().setSourceA(sdf).setSourceB(bowlPositioned);
			}
			sdf.setReplaceFunction(REPLACE).fillRecursive(world, pos);
			
			return true;
		}
		
		return false;
	}
	
	static {
		REPLACE = (state) -> {
			if (state.isIn(EndTags.END_GROUND)) {
				return true;
			}
			if (state.getBlock() instanceof LeavesBlock) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
	}
}
