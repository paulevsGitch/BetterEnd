package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class FilaluxFeature extends SkyScatterFeature {
	public FilaluxFeature() {
		super(10);
	}

	@Override
	public void generate(StructureWorldAccess world, Random random, BlockPos blockPos) {
		BlockState vine = EndBlocks.FILALUX.getDefaultState();
		BlockState wings = EndBlocks.FILALUX_WINGS.getDefaultState();
		BlocksHelper.setWithoutUpdate(world, blockPos, EndBlocks.FILALUX_LANTERN);
		BlocksHelper.setWithoutUpdate(world, blockPos.up(), wings.with(Properties.FACING, Direction.UP));
		for (Direction dir: BlocksHelper.HORIZONTAL) {
			BlocksHelper.setWithoutUpdate(world, blockPos.offset(dir), wings.with(Properties.FACING, dir));
		}
		int length = MHelper.randRange(1, 3, random);
		for (int i = 1; i <= length; i++) {
			TripleShape shape = length > 1 ? TripleShape.TOP : TripleShape.BOTTOM;
			if (i > 1) {
				shape = i == length ? TripleShape.BOTTOM : TripleShape.MIDDLE;
			}
			BlocksHelper.setWithoutUpdate(world, blockPos.down(i), vine.with(BlockProperties.TRIPLE_SHAPE, shape));
		}
	}
}
