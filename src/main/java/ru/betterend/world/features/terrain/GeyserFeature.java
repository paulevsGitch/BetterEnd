package ru.betterend.world.features.terrain;

import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockHydrothermalVent;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFCoordModify;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFInvert;
import ru.betterend.util.sdf.operator.SDFRotation;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSmoothUnion;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.operator.SDFUnion;
import ru.betterend.util.sdf.primitive.SDFCappedCone;
import ru.betterend.util.sdf.primitive.SDFFlatland;
import ru.betterend.util.sdf.primitive.SDFPrimitive;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class GeyserFeature extends DefaultFeature {
	protected static final Function<BlockState, Boolean> REPLACE1;
	protected static final Function<BlockState, Boolean> REPLACE2;
	private static final Function<BlockState, Boolean> IGNORE;
	private static final Direction[] HORIZONTAL = BlocksHelper.makeHorizontal();
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		pos = getPosOnSurfaceWG(world, pos);
		
		if (pos.getY() > 57) {
			int halfHeight = MHelper.randRange(10, 20, random);
			float radius1 = halfHeight * 0.5F;
			float radius2 = halfHeight * 0.1F + 0.5F;
			SDF sdf = new SDFCappedCone().setHeight(halfHeight).setRadius1(radius1).setRadius2(radius2).setBlock(EndBlocks.SULPHURIC_ROCK.stone);
			sdf = new SDFTranslate().setTranslate(0, halfHeight - 3, 0).setSource(sdf);
			
			int count = halfHeight;
			for (int i = 0; i < count; i++) {
				int py = i << 1;
				float delta = (float) i / (float) (count - 1);
				float radius = MathHelper.lerp(delta, radius1, radius2) * 1.3F;
				
				SDF bowl = new SDFCappedCone().setHeight(radius).setRadius1(0).setRadius2(radius).setBlock(EndBlocks.SULPHURIC_ROCK.stone);
				
				SDF brimstone = new SDFCappedCone().setHeight(radius).setRadius1(0).setRadius2(radius).setBlock(EndBlocks.BRIMSTONE);
				brimstone = new SDFTranslate().setTranslate(0, 2F, 0).setSource(brimstone);
				bowl = new SDFSubtraction().setSourceA(bowl).setSourceB(brimstone);
				bowl = new SDFUnion().setSourceA(brimstone).setSourceB(bowl);
				
				SDF water = new SDFCappedCone().setHeight(radius).setRadius1(0).setRadius2(radius).setBlock(Blocks.WATER);
				water = new SDFTranslate().setTranslate(0, 4, 0).setSource(water);
				bowl = new SDFSubtraction().setSourceA(bowl).setSourceB(water);
				bowl = new SDFUnion().setSourceA(water).setSourceB(bowl);
				
				final OpenSimplexNoise noise1 = new OpenSimplexNoise(random.nextLong());
				final OpenSimplexNoise noise2 = new OpenSimplexNoise(random.nextLong());
				
				bowl = new SDFCoordModify().setFunction((vec) -> {
					float dx = (float) noise1.eval(vec.getX() * 0.1, vec.getY() * 0.1, vec.getZ() * 0.1);
					float dz = (float) noise2.eval(vec.getX() * 0.1, vec.getY() * 0.1, vec.getZ() * 0.1);
					vec.set(vec.getX() + dx, vec.getY(), vec.getZ() + dz);
				}).setSource(bowl);
				
				SDF cut = new SDFFlatland().setBlock(Blocks.AIR);
				cut = new SDFInvert().setSource(cut);
				cut = new SDFTranslate().setTranslate(0, radius - 2, 0).setSource(cut);
				bowl = new SDFSubtraction().setSourceA(bowl).setSourceB(cut);
				
				bowl = new SDFTranslate().setTranslate(radius, py - radius, 0).setSource(bowl);
				bowl = new SDFRotation().setRotation(Vector3f.POSITIVE_Y, i * 4F).setSource(bowl);
				sdf = new SDFUnion().setSourceA(sdf).setSourceB(bowl);
			}
			sdf.setReplaceFunction(REPLACE2).fillRecursive(world, pos);
			
			radius2 = radius2 * 0.5F;
			if (radius2 < 0.7F) {
				radius2 = 0.7F;
			}
			final OpenSimplexNoise noise = new OpenSimplexNoise(random.nextLong());
			
			SDFPrimitive obj1;
			SDFPrimitive obj2;
			
			obj1 = new SDFCappedCone().setHeight(halfHeight + 5).setRadius1(radius1 * 0.5F).setRadius2(radius2);
			sdf = new SDFTranslate().setTranslate(0, halfHeight - 13, 0).setSource(obj1);
			sdf = new SDFDisplacement().setFunction((vec) -> {
				return (float) noise.eval(vec.getX() * 0.3F, vec.getY() * 0.3F, vec.getZ() * 0.3F) * 0.5F;
			}).setSource(sdf);
			
			obj2 = new SDFSphere().setRadius(radius1);
			SDF cave = new SDFScale3D().setScale(1.5F, 1, 1.5F).setSource(obj2);
			cave = new SDFDisplacement().setFunction((vec) -> {
				return (float) noise.eval(vec.getX() * 0.1F, vec.getY() * 0.1F, vec.getZ() * 0.1F) * 2F;
			}).setSource(cave);
			cave = new SDFTranslate().setTranslate(0, -halfHeight - 10, 0).setSource(cave);
			
			sdf = new SDFSmoothUnion().setRadius(5).setSourceA(cave).setSourceB(sdf);
			
			obj1.setBlock(WATER);
			obj2.setBlock(WATER);
			sdf.setReplaceFunction(REPLACE2);
			sdf.fillRecursive(world, pos);
			
			obj1.setBlock(EndBlocks.BRIMSTONE);
			obj2.setBlock(EndBlocks.BRIMSTONE);
			new SDFDisplacement().setFunction((vec) -> {
				return -2F;
			}).setSource(sdf).setReplaceFunction(REPLACE1).fillRecursiveIgnore(world, pos, IGNORE);
			
			obj1.setBlock(EndBlocks.SULPHURIC_ROCK.stone);
			obj2.setBlock(EndBlocks.SULPHURIC_ROCK.stone);
			new SDFDisplacement().setFunction((vec) -> {
				return -4F;
			}).setSource(cave).setReplaceFunction(REPLACE1).fillRecursiveIgnore(world, pos, IGNORE);
			
			obj1.setBlock(Blocks.END_STONE);
			obj2.setBlock(Blocks.END_STONE);
			new SDFDisplacement().setFunction((vec) -> {
				return -6F;
			}).setSource(cave).setReplaceFunction(REPLACE1).fillRecursiveIgnore(world, pos, IGNORE);
			
			BlocksHelper.setWithoutUpdate(world, pos, WATER);
			Mutable mut = new Mutable().set(pos);
			count = getYOnSurface(world, pos.getX(), pos.getZ()) - pos.getY();
			for (int i = 0; i < count; i++) {
				BlocksHelper.setWithoutUpdate(world, mut, WATER);
				for (Direction dir: BlocksHelper.HORIZONTAL) {
					BlocksHelper.setWithoutUpdate(world, mut.offset(dir), WATER);
				}
				mut.setY(mut.getY() + 1);
			}
			
			for (int i = 0; i < 150; i++) {
				mut.set(pos).move(MHelper.floor(random.nextGaussian() * 4 + 0.5), -halfHeight - 10, MHelper.floor(random.nextGaussian() * 4 + 0.5));
				float distRaw = MHelper.length(mut.getX() - pos.getX(), mut.getZ() - pos.getZ());
				int dist = MHelper.floor(6 - distRaw) + random.nextInt(2);
				if (dist >= 0) {
					BlockState state = world.getBlockState(mut);
					while (!state.getFluidState().isEmpty() || state.getMaterial().equals(Material.UNDERWATER_PLANT)) {
						mut.setY(mut.getY() - 1);
						state = world.getBlockState(mut);
					}
					if (state.isIn(EndTags.GEN_TERRAIN) && !world.getBlockState(mut.up()).isOf(EndBlocks.HYDROTHERMAL_VENT)) {
						for (int j = 0; j <= dist; j++) {
							BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.SULPHURIC_ROCK.stone);
							MHelper.shuffle(HORIZONTAL, random);
							for (Direction dir: HORIZONTAL) {
								BlockPos p = mut.offset(dir);
								if (random.nextBoolean() && world.getBlockState(p).isOf(Blocks.WATER)) {
									BlocksHelper.setWithoutUpdate(world, p, EndBlocks.TUBE_WORM.getDefaultState().with(HorizontalFacingBlock.FACING, dir));
								}
							}
							mut.setY(mut.getY() + 1);
						}
						state = EndBlocks.HYDROTHERMAL_VENT.getDefaultState().with(BlockHydrothermalVent.ACTIVATED, distRaw < 2);
						BlocksHelper.setWithoutUpdate(world, mut, state);
						mut.setY(mut.getY() + 1);
						state = world.getBlockState(mut);
						while (state.isOf(Blocks.WATER)) {
							BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.VENT_BUBBLE_COLUMN.getDefaultState());
							mut.setY(mut.getY() + 1);
							state = world.getBlockState(mut);
						}
					}
				}
			}
			
			for (int i = 0; i < 10; i++) {
				mut.set(pos).move(MHelper.floor(random.nextGaussian() * 0.7 + 0.5), -halfHeight - 10, MHelper.floor(random.nextGaussian() * 0.7 + 0.5));
				float distRaw = MHelper.length(mut.getX() - pos.getX(), mut.getZ() - pos.getZ());
				int dist = MHelper.floor(6 - distRaw) + random.nextInt(2);
				if (dist >= 0) {
					BlockState state = world.getBlockState(mut);
					while (state.isOf(Blocks.WATER)) {
						mut.setY(mut.getY() - 1);
						state = world.getBlockState(mut);
					}
					if (state.isIn(EndTags.GEN_TERRAIN))  {
						for (int j = 0; j <= dist; j++) {
							BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.SULPHURIC_ROCK.stone);
							mut.setY(mut.getY() + 1);
						}
						state = EndBlocks.HYDROTHERMAL_VENT.getDefaultState().with(BlockHydrothermalVent.ACTIVATED, distRaw < 2);
						BlocksHelper.setWithoutUpdate(world, mut, state);
						mut.setY(mut.getY() + 1);
						state = world.getBlockState(mut);
						while (state.isOf(Blocks.WATER)) {
							BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.VENT_BUBBLE_COLUMN.getDefaultState());
							mut.setY(mut.getY() + 1);
							state = world.getBlockState(mut);
						}
					}
				}
			}
			
			EndFeatures.SULPHURIC_LAKE.getFeature().generate(world, chunkGenerator, random, pos, null);
			
			double distance = radius1 * 1.7;
			BlockPos start = pos.add(-distance, -halfHeight - 15 - distance, -distance);
			BlockPos end = pos.add(distance, -halfHeight - 5 + distance, distance);
			BlocksHelper.fixBlocks(world, start, end);
			
			return true;
		}
		
		return false;
	}
	
	static {
		REPLACE1 = (state) -> {
			return state.isAir() || (state.isIn(EndTags.GEN_TERRAIN));
		};
		
		REPLACE2 = (state) -> {
			if (state.isIn(EndTags.GEN_TERRAIN) || state.isOf(EndBlocks.HYDROTHERMAL_VENT) || state.isOf(EndBlocks.SULPHUR_CRYSTAL)) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		IGNORE = (state) -> {
			return state.isOf(Blocks.WATER) || state.isOf(Blocks.CAVE_AIR) || state.isOf(EndBlocks.SULPHURIC_ROCK.stone) || state.isOf(EndBlocks.BRIMSTONE);
		};
	}
}
