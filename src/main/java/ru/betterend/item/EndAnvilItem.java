package ru.betterend.item;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.blocks.basis.EndAnvilBlock;
import ru.betterend.registry.EndBlocks;

public class EndAnvilItem extends BlockItem {
	public EndAnvilItem(Block block) {
		super(block, EndBlocks.makeBlockItemSettings());
	}
	
	@Override
	protected BlockState getPlacementState(BlockPlaceContext blockPlaceContext) {
		BlockState blockState = super.getPlacementState(blockPlaceContext);
		ItemStack stack = blockPlaceContext.getItemInHand();
		int level = stack.getOrCreateTag().getInt("level");
		blockState = blockState.setValue(((EndAnvilBlock) blockState.getBlock()).getDestructionProperty(), level);
		return blockState;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, level, list, tooltipFlag);
		int l = itemStack.getOrCreateTag().getInt("level");
		if (l > 0) {
			list.add(new TranslatableComponent("message.betterend.anvil_damage").append(": " + l));
		}
	}
}
