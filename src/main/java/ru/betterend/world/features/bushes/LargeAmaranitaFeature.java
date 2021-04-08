package ru.betterend.world.features.bushes;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.world.features.DefaultFeature;

public class LargeAmaranitaFeature extends DefaultFeature {
	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			DefaultFeatureConfig config) {
		if (!world.getBlockState(pos.below()).getBlock().isIn(EndTags.END_GROUND))
			return false;

		MutableBlockPos mut = new MutableBlockPos().set(pos);
		int height = MHelper.randRange(2, 3, random);
		for (int i = 1; i < height; i++) {
			mut.setY(mut.getY() + 1);
			if (!world.isAir(mut)) {
				return false;
			}
		}
		mut.set(pos);

		BlockState state = EndBlocks.LARGE_AMARANITA_MUSHROOM.defaultBlockState();
		BlocksHelper.setWithUpdate(world, mut, state.with(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM));
		if (height > 2) {
			BlocksHelper.setWithUpdate(world, mut.move(Direction.UP),
					state.with(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE));
		}
		BlocksHelper.setWithUpdate(world, mut.move(Direction.UP),
				state.with(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP));

		return true;
	}
}
