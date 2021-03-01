package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.EndPlantBlock;
import ru.betterend.registry.EndBlocks;

public class LargeAmaranitaBlock extends EndPlantBlock {
	public static final EnumProperty<TripleShape> SHAPE = BlockProperties.TRIPLE_SHAPE;
	
	public LargeAmaranitaBlock() {
		super(FabricBlockSettings.of(Material.PLANT)
				.luminance((state) -> (state.get(SHAPE) == TripleShape.TOP) ? 15 : 0)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.GRASS)
				.breakByHand(true)
				.noCollision());
	}
	
	@Override
	protected boolean isTerrain(BlockState state) {
		return state.getBlock() == EndBlocks.SANGNUM;
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		TripleShape shape = state.get(SHAPE);
		if (shape == TripleShape.BOTTOM) {
			return isTerrain(world.getBlockState(pos.down())) && world.getBlockState(pos.up()).isOf(this);
		}
		else if (shape == TripleShape.TOP) {
			return world.getBlockState(pos.down()).isOf(this);
		}
		else {
			return world.getBlockState(pos.down()).isOf(this) && world.getBlockState(pos.up()).isOf(this);
		}
	}
	
	@Override
	public OffsetType getOffsetType() {
		return OffsetType.NONE;
	}
}
