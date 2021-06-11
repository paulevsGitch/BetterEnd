package ru.betterend.world.features.bushes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.blocks.BlockProperties.TripleShape;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;
import ru.betterend.registry.EndBlocks;

import java.util.Random;

public class LargeAmaranitaFeature extends DefaultFeature {
	@Override
	public boolean place(WorldGenLevel world, ChunkGenerator chunkGenerator, Random random, BlockPos pos,
			NoneFeatureConfiguration config) {
		if (!world.getBlockState(pos.below()).getBlock().is(TagAPI.END_GROUND))
			return false;

		MutableBlockPos mut = new MutableBlockPos().set(pos);
		int height = MHelper.randRange(2, 3, random);
		for (int i = 1; i < height; i++) {
			mut.setY(mut.getY() + 1);
			if (!world.isEmptyBlock(mut)) {
				return false;
			}
		}
		mut.set(pos);

		BlockState state = EndBlocks.LARGE_AMARANITA_MUSHROOM.defaultBlockState();
		BlocksHelper.setWithUpdate(world, mut, state.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM));
		if (height > 2) {
			BlocksHelper.setWithUpdate(world, mut.move(Direction.UP),
					state.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE));
		}
		BlocksHelper.setWithUpdate(world, mut.move(Direction.UP),
				state.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP));

		return true;
	}
}
