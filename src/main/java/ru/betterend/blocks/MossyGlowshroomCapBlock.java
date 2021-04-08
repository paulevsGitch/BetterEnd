package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.ItemPlacementContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;

public class MossyGlowshroomCapBlock extends BlockBase {
	public static final BooleanProperty TRANSITION = BlockProperties.TRANSITION;

	public MossyGlowshroomCapBlock() {
		super(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).sounds(SoundType.WOOD));
		this.setDefaultState(this.stateManager.defaultBlockState().with(TRANSITION, false));
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.defaultBlockState().with(TRANSITION,
				EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(ctx.getLevel().getBlockState(ctx.getBlockPos().below())));
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TRANSITION);
	}
}
