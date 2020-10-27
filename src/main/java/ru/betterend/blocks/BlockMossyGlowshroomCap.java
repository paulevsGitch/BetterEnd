package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import ru.betterend.registry.EndBlocks;

public class BlockMossyGlowshroomCap extends Block {
	public static final BooleanProperty TRANSITION = BooleanProperty.of("transition");
	
	public BlockMossyGlowshroomCap() {
		super(FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES).sounds(BlockSoundGroup.WOOD));
		this.setDefaultState(this.stateManager.getDefaultState().with(TRANSITION, false));
	}
	
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(TRANSITION, EndBlocks.MOSSY_GLOWSHROOM.isTreeLog(ctx.getWorld().getBlockState(ctx.getBlockPos().down())));
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(TRANSITION);
	}
}
