package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.EndPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

public class GlowingPillarSeedBlock extends EndPlantWithAgeBlock {
	public GlowingPillarSeedBlock() {
		super(FabricBlockSettings.of(Material.PLANT)
				.luminance((state) -> { return state.get(AGE) * 3 + 3; })
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.GRASS)
				.breakByHand(true)
				.ticksRandomly()
				.noCollision());
	}
	
	@Override
	public void growAdult(StructureWorldAccess world, Random random, BlockPos pos) {
		int height = MHelper.randRange(1, 2, random);
		int h = BlocksHelper.upRay(world, pos, height + 2);
		if (h < height) {
			return;
		}
		
		Mutable mut = new Mutable().set(pos);
		BlockState roots = EndBlocks.GLOWING_PILLAR_ROOTS.getDefaultState();
		if (height < 2) {
			BlocksHelper.setWithUpdate(world, mut, roots.with(BlockProperties.TRIPLE_SHAPE, TripleShape.MIDDLE));
			mut.move(Direction.UP);
		}
		else {
			BlocksHelper.setWithUpdate(world, mut, roots.with(BlockProperties.TRIPLE_SHAPE, TripleShape.BOTTOM));
			mut.move(Direction.UP);
			BlocksHelper.setWithUpdate(world, mut, roots.with(BlockProperties.TRIPLE_SHAPE, TripleShape.TOP));
			mut.move(Direction.UP);
		}
		BlocksHelper.setWithUpdate(world, mut, EndBlocks.GLOWING_PILLAR_LUMINOPHOR.getDefaultState().with(BlueVineLanternBlock.NATURAL, true));
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			pos = mut.offset(dir);
			if (world.isAir(pos)) {
				BlocksHelper.setWithUpdate(world, pos, EndBlocks.GLOWING_PILLAR_LEAVES.getDefaultState().with(Properties.FACING, dir));
			}
		}
		mut.move(Direction.UP);
		if (world.isAir(mut)) {
			BlocksHelper.setWithUpdate(world, mut, EndBlocks.GLOWING_PILLAR_LEAVES.getDefaultState().with(Properties.FACING, Direction.UP));
		}
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.isOf(EndBlocks.AMBER_MOSS);
	}
}
