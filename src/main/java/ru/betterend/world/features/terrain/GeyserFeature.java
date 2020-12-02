package ru.betterend.world.features.terrain;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFCoordModify;
import ru.betterend.util.sdf.operator.SDFInvert;
import ru.betterend.util.sdf.operator.SDFRotation;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFCapedCone;
import ru.betterend.util.sdf.primitive.SDFFlatland;
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
			SDF sdf = new SDFCapedCone().setHeight(halfHeight).setRadius1(radius1).setRadius2(radius2).setBlock(EndBlocks.SULPHURIC_ROCK.stone);
			sdf = new SDFTranslate().setTranslate(0, halfHeight - 3, 0).setSource(sdf);
			
			int count = halfHeight;
			for (int i = 0; i < count; i++) {
				int py = i << 1;
				float delta = (float) i / (float) (count - 1);
				float radius = MathHelper.lerp(delta, radius1, radius2) * 1.3F;
				
				/*SDF bowl = new SDFCapedCone().setHeight(radius).setRadius1(0).setRadius2(radius).setBlock(EndBlocks.SULFURIC_ROCK.stone);
				SDF cut = new SDFTranslate().setTranslate(0, 2, 0).setSource(bowl);
				bowl = new SDFSubtraction().setSourceA(bowl).setSourceB(cut);
				
				SDF inner = new SDFCapedCone().setHeight(radius - 2).setRadius1(0).setRadius2(radius - 1).setBlock(EndBlocks.BRIMSTONE);
				cut = new SDFTranslate().setTranslate(0, 4, 0).setSource(inner);
				inner = new SDFSubtraction().setSourceA(inner).setSourceB(cut);
				inner = new SDFTranslate().setTranslate(0, 1.99F, 0).setSource(inner);
				bowl = new SDFUnion().setSourceA(inner).setSourceB(bowl);
				
				SDF water = new SDFCapedCone().setHeight(radius - 4).setRadius1(0).setRadius2(radius - 3).setBlock(Blocks.WATER);
				bowl = new SDFUnion().setSourceA(water).setSourceB(bowl);*/
				
				SDF bowl = new SDFCapedCone().setHeight(radius).setRadius1(0).setRadius2(radius).setBlock(EndBlocks.SULPHURIC_ROCK.stone);
				
				SDF brimstone = new SDFCapedCone().setHeight(radius).setRadius1(0).setRadius2(radius).setBlock(EndBlocks.BRIMSTONE);
				brimstone = new SDFTranslate().setTranslate(0, 2F, 0).setSource(brimstone);
				bowl = new SDFSubtraction().setSourceA(bowl).setSourceB(brimstone);
				bowl = new SDFUnion().setSourceA(brimstone).setSourceB(bowl);
				
				SDF water = new SDFCapedCone().setHeight(radius).setRadius1(0).setRadius2(radius).setBlock(Blocks.WATER);
				water = new SDFTranslate().setTranslate(0, 4, 0).setSource(water);
				bowl = new SDFSubtraction().setSourceA(bowl).setSourceB(water);
				bowl = new SDFUnion().setSourceA(water).setSourceB(bowl);
				
				final OpenSimplexNoise noise1 = new OpenSimplexNoise(random.nextLong());
				final OpenSimplexNoise noise2 = new OpenSimplexNoise(random.nextLong());
				/*bowl = new SDFDisplacement().setFunction((vec) -> {
					return (float) noise.eval(vec.getX() * 0.1, vec.getY() * 0.1, vec.getZ() * 0.1);
				}).setSource(bowl);*/
				bowl = new SDFCoordModify().setFunction((vec) -> {
					float dx = (float) noise1.eval(vec.getX() * 0.1, vec.getY() * 0.1, vec.getZ() * 0.1);
					float dz = (float) noise2.eval(vec.getX() * 0.1, vec.getY() * 0.1, vec.getZ() * 0.1);
					vec.add(dx, 0, dz);
				}).setSource(bowl);
				
				SDF cut = new SDFFlatland().setBlock(Blocks.AIR);
				cut = new SDFInvert().setSource(cut);
				cut = new SDFTranslate().setTranslate(0, radius - 2, 0).setSource(cut);
				bowl = new SDFSubtraction().setSourceA(bowl).setSourceB(cut);
				
				bowl = new SDFTranslate().setTranslate(radius, py - radius, 0).setSource(bowl);
				bowl = new SDFRotation().setRotation(Vector3f.POSITIVE_Y, i * 4F).setSource(bowl);
				sdf = /*new SDFSmoothUnion()*/new SDFUnion()/*.setRadius(3)*/.setSourceA(sdf).setSourceB(bowl);
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
