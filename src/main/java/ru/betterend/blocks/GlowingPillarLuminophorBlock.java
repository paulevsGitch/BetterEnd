package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import ru.bclib.blocks.BaseBlock;
import ru.betterend.registry.EndBlocks;

public class GlowingPillarLuminophorBlock extends BaseBlock {
	public static final BooleanProperty NATURAL = EndBlockProperties.NATURAL;
	
	public GlowingPillarLuminophorBlock() {
		super(FabricBlockSettings.of(Material.LEAVES)
								 .materialColor(MaterialColor.COLOR_ORANGE)
								 .breakByTool(FabricToolTags.SHEARS)
								 .strength(0.2F)
								 .luminance(15)
								 .sound(SoundType.GRASS));
		this.registerDefaultState(this.stateDefinition.any().setValue(NATURAL, false));
	}
	
	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return !state.getValue(NATURAL) || world.getBlockState(pos.below()).is(EndBlocks.GLOWING_PILLAR_ROOTS);
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, world, pos)) {
			return Blocks.AIR.defaultBlockState();
		}
		else {
			return state;
		}
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(NATURAL);
	}
}
