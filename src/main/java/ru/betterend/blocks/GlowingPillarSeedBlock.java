package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;

public class GlowingPillarSeedBlock extends EndPlantWithAgeBlock {
	public GlowingPillarSeedBlock() {
		super(FabricBlockSettings.of(Material.PLANT)
				.breakByTool(FabricToolTags.SHEARS)
				.breakByHand(true)
				.sound(SoundType.GRASS)
				.lightLevel(state -> state.getValue(AGE) * 3 + 3)
				.randomTicks()
				.noCollission());
	}
	
	@Override
	public void growAdult(WorldGenLevel world, Random random, BlockPos pos) {
		int height = MHelper.randRange(1, 2, random);
		int h = BlocksHelper.upRay(world, pos, height + 2);
		if (h < height) {
			return;
		}
		
		MutableBlockPos mut = new MutableBlockPos().set(pos);
		BlockState roots = EndBlocks.GLOWING_PILLAR_ROOTS.defaultBlockState();
		if (height < 2) {
			BlocksHelper.setWithUpdate(world, mut, roots.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE));
			mut.move(Direction.UP);
		}
		else {
			BlocksHelper.setWithUpdate(world, mut, roots.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM));
			mut.move(Direction.UP);
			BlocksHelper.setWithUpdate(world, mut, roots.setValue(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP));
			mut.move(Direction.UP);
		}
		BlocksHelper.setWithUpdate(world, mut, EndBlocks.GLOWING_PILLAR_LUMINOPHOR.defaultBlockState().setValue(BlueVineLanternBlock.NATURAL, true));
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			pos = mut.relative(dir);
			if (world.isEmptyBlock(pos)) {
				BlocksHelper.setWithUpdate(world, pos, EndBlocks.GLOWING_PILLAR_LEAVES.defaultBlockState().setValue(BlockStateProperties.FACING, dir));
			}
		}
		mut.move(Direction.UP);
		if (world.isEmptyBlock(mut)) {
			BlocksHelper.setWithUpdate(world, mut, EndBlocks.GLOWING_PILLAR_LEAVES.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP));
		}
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.is(EndBlocks.AMBER_MOSS);
	}
	
	@Override
	public BlockBehaviour.OffsetType getOffsetType() {
		return BlockBehaviour.OffsetType.NONE;
	}
}
