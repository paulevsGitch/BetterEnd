package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import ru.betterend.util.MHelper;

public class EndstoneDustBlock extends FallingBlock {
	@Environment(EnvType.CLIENT)
	private static final int COLOR = MHelper.color(226, 239, 168);
	
	public EndstoneDustBlock() {
		super(FabricBlockSettings.copyOf(Blocks.SAND)
				.breakByTool(FabricToolTags.SHOVELS)
				.materialColor(Blocks.END_STONE.getDefaultMaterialColor()));
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Environment(EnvType.CLIENT)
	public int getColor(BlockState state, BlockView world, BlockPos pos) {
		return COLOR;
	}
}
