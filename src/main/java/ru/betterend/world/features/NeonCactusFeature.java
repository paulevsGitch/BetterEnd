package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class NeonCactusFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.down()).isOf(EndBlocks.ENDSTONE_DUST)) {
			return false;
		}
		
		int h = MHelper.randRange(5, 20, random);
		Mutable mut = new Mutable().set(pos);
		Direction hor = BlocksHelper.randomHorizontal(random);
		for (int i = 0; i < h; i++) {
			if (!world.getBlockState(mut).getMaterial().isReplaceable()) {
				break;
			}
			int size = (h - i) >> 2;
			BlocksHelper.setWithUpdate(world, mut,
				EndBlocks.NEON_CACTUS.getDefaultState()
				.with(BlockProperties.TRIPLE_SHAPE, getBySize(size))
				.with(Properties.FACING, Direction.UP)
			);
			if (i > 2 && i < (h - 1) && random.nextBoolean()) {
				int length = h - i - MHelper.randRange(1, 2, random);
				if (length > 0) {
					Direction dir2 = hor;
					hor = hor.rotateYClockwise();
					int bsize = i > ((h << 1) / 3) ? 0 : size > 1 ? 1 : size;
					branch(world, mut.offset(dir2), dir2, random, length, bsize);
				}
			}
			mut.move(Direction.UP);
		}
		
		return true;
	}
	
	private void branch(StructureWorldAccess world, BlockPos pos, Direction dir, Random random, int length, int size) {
		int rotIndex = length >> 2;
		Mutable mut = new Mutable().set(pos);
		Direction hor = BlocksHelper.randomHorizontal(random);
		for (int i = 0; i < length; i++) {
			if (!world.getBlockState(mut).getMaterial().isReplaceable()) {
				return;
			}
			BlocksHelper.setWithUpdate(world, mut,
				EndBlocks.NEON_CACTUS.getDefaultState()
				.with(BlockProperties.TRIPLE_SHAPE, getBySize(size))
				.with(Properties.FACING, dir)
			);
			if (i == rotIndex) {
				dir = Direction.UP;
				size --;
			}
			if (i > 1 && i < (length - 1) && random.nextBoolean()) {
				Direction dir2 = dir == Direction.UP ? hor : Direction.UP;
				hor = hor.rotateYClockwise();
				branch(world, mut.offset(dir2), dir2, random, MHelper.randRange(length / 4, length / 2, random), size > 0 ? size - 1 : size);
			}
			mut.move(dir);
		}
	}
	
	private TripleShape getBySize(int size) {
		return size < 1 ? TripleShape.TOP : size == 1 ? TripleShape.MIDDLE : TripleShape.BOTTOM;
	}
}
