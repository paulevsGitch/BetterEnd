package ru.betterend.world.features;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.SplineHelper;
import ru.betterend.util.sdf.PosInfo;
import ru.betterend.util.sdf.SDF;

public class LacugroveFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE;
	private static final Function<PosInfo, BlockState> POST;
	
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).isIn(EndTags.END_GROUND)) return false;
		
		float size = MHelper.randRange(15, 25, random);
		List<Vector3f> spline = SplineHelper.makeSpline(0, 0, 0, 0, size, 0, 6);
		SplineHelper.offsetParts(spline, random, 1F, 0, 1F);
		float radius = MHelper.randRange(1.2F, 1.8F, random);
		SDF function = SplineHelper.buildSDF(spline, radius, 0.7F, (bpos) -> {
			return EndBlocks.LACUGROVE.bark.getDefaultState();
		});
		function.setReplaceFunction(REPLACE);
		function.setPostProcess(POST);
		function.fillRecursive(world, pos);
		
		Mutable mut = new Mutable();
		int offset = random.nextInt(2);
		for (int i = 0; i < 100; i++) {
			double px = pos.getX() + MHelper.randRange(-5, 5, random);
			double pz = pos.getZ() + MHelper.randRange(-5, 5, random);
			mut.setX(MHelper.floor(px + 0.5));
			mut.setZ(MHelper.floor(pz + 0.5));
			if (((mut.getX() + mut.getZ() + offset) & 1) == 0) {
				double distance = 3.5 - MHelper.length(px - pos.getX(), pz - pos.getZ()) * 0.5;
				if (distance > 0) {
					int minY = MHelper.floor(pos.getY() - distance * 0.5);
					int maxY = MHelper.floor(pos.getY() + distance + random.nextDouble());
					boolean generate = false;
					for (int y = minY; y < maxY; y++) {
						mut.setY(y);
						if (world.getBlockState(mut).isIn(EndTags.END_GROUND)) {
							generate = true;
							break;
						}
					}
					if (generate) {
						int top = maxY - 1;
						for (int y = top; y >= minY; y--) {
							mut.setY(y);
							BlockState state = world.getBlockState(mut);
							if (state.getMaterial().isReplaceable() || state.getMaterial().equals(Material.PLANT) || state.isIn(EndTags.END_GROUND)) {
								BlocksHelper.setWithoutUpdate(world, mut, y == top ? EndBlocks.LACUGROVE.bark : EndBlocks.LACUGROVE.log);
							}
							else {
								break;
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	static {
		REPLACE = (state) -> {
			if (state.isIn(EndTags.END_GROUND)) {
				return true;
			}
			if (state.getBlock() == EndBlocks.PYTHADENDRON_LEAVES) {
				return true;
			}
			if (state.getMaterial().equals(Material.PLANT)) {
				return true;
			}
			return state.getMaterial().isReplaceable();
		};
		
		POST = (info) -> {
			if (EndBlocks.LACUGROVE.isTreeLog(info.getStateUp()) && EndBlocks.LACUGROVE.isTreeLog(info.getStateDown())) {
				return EndBlocks.LACUGROVE.log.getDefaultState();
			}
			return info.getState();
		};
	}
}
