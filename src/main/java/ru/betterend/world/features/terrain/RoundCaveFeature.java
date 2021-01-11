package ru.betterend.world.features.terrain;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.noise.OpenSimplexNoise;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndStructures;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.sdf.SDF;
import ru.betterend.util.sdf.operator.SDFDisplacement;
import ru.betterend.util.sdf.operator.SDFRotation;
import ru.betterend.util.sdf.operator.SDFScale3D;
import ru.betterend.util.sdf.operator.SDFSubtraction;
import ru.betterend.util.sdf.operator.SDFTranslate;
import ru.betterend.util.sdf.primitive.SDFHexPrism;
import ru.betterend.util.sdf.primitive.SDFSphere;
import ru.betterend.world.features.DefaultFeature;

public class RoundCaveFeature extends DefaultFeature {
	private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (pos.getX() * pos.getX() + pos.getZ() * pos.getZ() <= 22500) {
			return false;
		}
		
		int radius = MHelper.randRange(10, 30, random);
		
		int top = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
		Mutable bpos = new Mutable();
		bpos.setX(pos.getX());
		bpos.setZ(pos.getZ());
		bpos.setY(top - 1);
		
		BlockState state = world.getBlockState(bpos);
		while (!state.isIn(EndTags.GEN_TERRAIN) && bpos.getY() > 5) {
			bpos.setY(bpos.getY() - 1);
			state = world.getBlockState(bpos);
		}
		if (bpos.getY() < 10) {
			return false;
		}
		top = (int) (bpos.getY() - (radius * 1.3F + 5));
		
		while (state.isIn(EndTags.GEN_TERRAIN) || !state.getFluidState().isEmpty() && bpos.getY() > 5) {
			bpos.setY(bpos.getY() - 1);
			state = world.getBlockState(bpos);
		}
		int bottom = (int) (bpos.getY() + radius * 1.3F + 5);
		
		if (top <= bottom) {
			return false;
		}
		
		pos = new BlockPos(pos.getX(), MHelper.randRange(bottom, top, random), pos.getZ());
		
		OpenSimplexNoise noise = new OpenSimplexNoise(MHelper.getSeed(534, pos.getX(), pos.getZ()));
		
		int x1 = pos.getX() - radius - 5;
		int z1 = pos.getZ() - radius - 5;
		int x2 = pos.getX() + radius + 5;
		int z2 = pos.getZ() + radius + 5;
		int y1 = MHelper.floor(pos.getY() - (radius + 5) / 1.6);
		int y2 = MHelper.floor(pos.getY() + (radius + 5) / 1.6);
		
		double hr = radius * 0.75;
		double nr = radius * 0.25;
		
		Set<BlockPos> bushes = Sets.newHashSet();
		BlockState terrain = EndBlocks.CAVE_MOSS.getDefaultState();
		for (int x = x1; x <= x2; x++) {
			int xsq = x - pos.getX();
			xsq *= xsq;
			bpos.setX(x);
			for (int z = z1; z <= z2; z++) {
				int zsq = z - pos.getZ();
				zsq *= zsq;
				bpos.setZ(z);
				for (int y = y1; y <= y2; y++) {
					int ysq = y - pos.getY();
					ysq *= 1.6;
					ysq *= ysq;
					bpos.setY(y);
					double r = noise.eval(x * 0.1, y * 0.1, z * 0.1) * nr + hr;
					double r2 = r + 5;
					double dist = xsq + ysq + zsq;
					if (dist < r * r) {
						state = world.getBlockState(bpos);
						if (isReplaceable(state)) {
							BlocksHelper.setWithoutUpdate(world, bpos, CAVE_AIR);
							
							while (state.getMaterial().equals(Material.LEAVES)) {
								BlocksHelper.setWithoutUpdate(world, bpos, CAVE_AIR);
								bpos.setY(bpos.getY() + 1);
								state = world.getBlockState(bpos);
							}
							
							bpos.setY(y - 1);
							while (state.getMaterial().equals(Material.LEAVES)) {
								BlocksHelper.setWithoutUpdate(world, bpos, CAVE_AIR);
								bpos.setY(bpos.getY() - 1);
								state = world.getBlockState(bpos);
							}
						}
						bpos.setY(y - 1);
						if (world.getBlockState(bpos).isIn(EndTags.GEN_TERRAIN)) {
							BlocksHelper.setWithoutUpdate(world, bpos, terrain);
						}
					}
					else if (dist < r2 * r2) {
						state = world.getBlockState(bpos);
						if (!state.getFluidState().isEmpty()) {
							BlocksHelper.setWithoutUpdate(world, bpos, Blocks.END_STONE.getDefaultState());
						}
						else if (world.getBlockState(bpos).isIn(EndTags.GEN_TERRAIN)) {
							if (world.isAir(bpos.down())) {
								int h = BlocksHelper.downRay(world, bpos.down(), 64);
								if (h > 6 && h < 32 && world.getBlockState(bpos.down(h + 3)).isIn(EndTags.GEN_TERRAIN)) {
									bushes.add(bpos.down());
								}
							}
							else if (world.isAir(bpos.up())) {
								int h = BlocksHelper.upRay(world, bpos.up(), 64);
								if (h > 6 && h < 32 && world.getBlockState(bpos.up(h + 3)).isIn(EndTags.GEN_TERRAIN)) {
									bushes.add(bpos.up());
								}
							}
						}
					}
				}
			}
		}
		bushes.forEach((cpos) -> {
			if (random.nextInt(32) == 0) {
				generateBush(world, random, cpos);
			}
		});
		
		if (random.nextBoolean() && world.getBiome(pos).getGenerationSettings().hasStructureFeature(EndStructures.MOUNTAIN.getStructure())) {
			pos = pos.add(random.nextGaussian() * 5, random.nextGaussian() * 5, random.nextGaussian() * 5);
			BlockPos down = pos.down(BlocksHelper.downRay(world, pos, 64) + 2);
			if (isReplaceable(world.getBlockState(down))) {
				SDF prism = new SDFHexPrism().setHeight(radius * MHelper.randRange(0.6F, 0.75F, random)).setRadius(MHelper.randRange(1.7F, 3F, random)).setBlock(EndBlocks.AURORA_CRYSTAL);
				float angleY = MHelper.randRange(0, MHelper.PI2, random);
				float vx = (float) Math.sin(angleY);
				float vz = (float) Math.sin(angleY);
				prism = new SDFRotation().setRotation(new Vector3f(vx, 0, vz), random.nextFloat()).setSource(prism);
				prism.setReplaceFunction((bState) -> {
					return bState.getMaterial().isReplaceable()
							|| bState.isIn(EndTags.GEN_TERRAIN)
							|| bState.getMaterial().equals(Material.PLANT)
							|| bState.getMaterial().equals(Material.LEAVES);
				});
				prism.fillRecursive(world, pos);
				BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.AURORA_CRYSTAL);
			}
		}
		
		BlocksHelper.fixBlocks(world, new BlockPos(x1, y1, z1), new BlockPos(x2, y2, z2));
		
		return true;
	}
	
	private boolean isReplaceable(BlockState state) {
		return state.isIn(EndTags.GEN_TERRAIN)
				|| state.getMaterial().isReplaceable()
				|| state.getMaterial().equals(Material.PLANT)
				|| state.getMaterial().equals(Material.LEAVES);
	}
	
	private void generateBush(StructureWorldAccess world, Random random, BlockPos blockPos) {
		float radius = MHelper.randRange(1.0F, 3.2F, random);
		OpenSimplexNoise noise = new OpenSimplexNoise(random.nextInt());
		SDF sphere = new SDFSphere().setRadius(radius).setBlock(EndBlocks.CAVE_BUSH);
		sphere = new SDFScale3D().setScale(MHelper.randRange(0.8F, 1.2F, random), MHelper.randRange(0.8F, 1.2F, random), MHelper.randRange(0.8F, 1.2F, random)).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return (float) noise.eval(vec.getX() * 0.2, vec.getY() * 0.2, vec.getZ() * 0.2) * 3; }).setSource(sphere);
		sphere = new SDFDisplacement().setFunction((vec) -> { return random.nextFloat() * 3F - 1.5F; }).setSource(sphere);
		sphere = new SDFSubtraction().setSourceA(sphere).setSourceB(new SDFTranslate().setTranslate(0, -radius, 0).setSource(sphere));
		sphere.fillRecursive(world, blockPos);
		BlocksHelper.setWithoutUpdate(world, blockPos, EndBlocks.CAVE_BUSH);
	}
}
