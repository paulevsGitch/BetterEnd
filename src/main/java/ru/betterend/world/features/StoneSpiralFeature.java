package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class StoneSpiralFeature extends Feature<DefaultFeatureConfig> {
	public StoneSpiralFeature() {
		super(DefaultFeatureConfig.CODEC);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		BlockPos topPos = world.getTopPosition(Type.WORLD_SURFACE, pos);
		Direction offset = Direction.NORTH;

		System.out.println(topPos);
		
		for (int y = 1; y <= 15; y++) {
			offset = offset.rotateYClockwise();
			world.setBlockState(topPos.up(y).offset(offset), Blocks.STONE.getDefaultState(), 3);
		}

		return true;
	}
}