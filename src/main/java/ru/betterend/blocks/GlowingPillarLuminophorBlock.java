package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;

public class GlowingPillarLuminophorBlock extends BlockBase {
	public static final BooleanProperty NATURAL = BlockProperties.NATURAL;
	
	public GlowingPillarLuminophorBlock() {
		super(FabricBlockSettings.of(Material.LEAVES)
				.materialColor(MaterialColor.ORANGE)
				.breakByTool(FabricToolTags.SHEARS)
				.sounds(BlockSoundGroup.GRASS)
				.strength(0.2F)
				.luminance(15));
		this.setDefaultState(this.stateManager.getDefaultState().with(NATURAL, false));
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return state.get(NATURAL) ? world.getBlockState(pos.down()).isOf(EndBlocks.GLOWING_PILLAR_ROOTS) : true;
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!canPlaceAt(state, world, pos)) {
			return Blocks.AIR.getDefaultState();
		}
		else {
			return state;
		}
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(NATURAL);
	}
}
