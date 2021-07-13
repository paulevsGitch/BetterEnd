package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import ru.bclib.blocks.BaseBlock;
import ru.betterend.registry.EndBlocks;

public class MossyGlowshroomCapBlock extends BaseBlock {
	public static final BooleanProperty TRANSITION = EndBlockProperties.TRANSITION;
	
	public MossyGlowshroomCapBlock() {
		super(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).sound(SoundType.WOOD));
		this.registerDefaultState(this.stateDefinition.any().setValue(TRANSITION, false));
	}
	
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(TRANSITION, EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(ctx.getLevel().getBlockState(ctx.getClickedPos().below())));
	}
	
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TRANSITION);
	}
}
