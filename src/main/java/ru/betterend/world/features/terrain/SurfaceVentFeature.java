package ru.betterend.world.features.terrain;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockHydrothermalVent;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.features.DefaultFeature;

public class SurfaceVentFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		pos = getPosOnSurface(world, new BlockPos(pos.getX() + random.nextInt(16), pos.getY(), pos.getZ() + random.nextInt(16)));
		if (pos.getY() < 57) {
			return false;
		}
		
		Mutable mut = new Mutable();
		int count = MHelper.randRange(15, 30, random);
		BlockState vent = EndBlocks.HYDROTHERMAL_VENT.getDefaultState().with(BlockHydrothermalVent.WATERLOGGED, false);
		for (int i = 0; i < count; i++) {
			mut.set(pos).move(MHelper.floor(random.nextGaussian() * 2 + 0.5), 5, MHelper.floor(random.nextGaussian() * 2 + 0.5));
			int dist = MHelper.floor(2 - MHelper.length(mut.getX() - pos.getX(), mut.getZ() - pos.getZ())) + random.nextInt(2);
			if (dist > 0) {
				BlockState state = world.getBlockState(mut);
				for (int n = 0; n < 10 && state.isAir(); n++) {
					mut.setY(mut.getY() - 1);
					state = world.getBlockState(mut);
				}
				if (state.isIn(EndTags.GEN_TERRAIN) && !world.getBlockState(mut.up()).isOf(EndBlocks.HYDROTHERMAL_VENT)) {
					for (int j = 0; j <= dist; j++) {
						BlocksHelper.setWithoutUpdate(world, mut, EndBlocks.SULPHURIC_ROCK.stone);
						mut.setY(mut.getY() + 1);
					}
					BlocksHelper.setWithoutUpdate(world, mut, vent);
				}
			}
		}
		
		return true;
	}
}
