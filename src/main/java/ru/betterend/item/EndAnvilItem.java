package ru.betterend.item;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.client.models.ItemModelProvider;
import ru.bclib.items.BaseAnvilItem;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.registry.EndBlocks;

public class EndAnvilItem extends BaseAnvilItem {

	public final static String DURABILITY = "durability";

	public EndAnvilItem(Block anvilBlock) {
		super(anvilBlock, EndBlocks.makeBlockItemSettings());
	}

	@Override
	@SuppressWarnings("ConstantConditions")
	protected BlockState getPlacementState(BlockPlaceContext blockPlaceContext) {
		BlockState blockState = super.getPlacementState(blockPlaceContext);
		ItemStack stack = blockPlaceContext.getItemInHand();
		int durability = stack.getOrCreateTag().getInt(DURABILITY);
		blockState = blockState.setValue(((EndAnvilBlock) blockState.getBlock()).getDurability(), durability);
		return blockState;
	}

	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		Block block = getBlock();
		ResourceLocation blockId = Registry.BLOCK.getKey(block);
		return ((ItemModelProvider) block).getItemModel(blockId);
	}
}
