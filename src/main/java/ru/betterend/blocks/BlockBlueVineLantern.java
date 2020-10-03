package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import ru.betterend.blocks.basis.BlockBase;

public class BlockBlueVineLantern extends BlockBase {
	public static final BooleanProperty NATURAL = BooleanProperty.of("natural");
	
	public BlockBlueVineLantern() {
		super(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).sounds(BlockSoundGroup.WART_BLOCK).lightLevel(15));
		this.setDefaultState(this.stateManager.getDefaultState().with(NATURAL, false));
	}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(NATURAL) && world.isAir(pos.down())) {
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
