package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.IRenderTyped;
import ru.bclib.util.BlocksHelper;

public class DenseEmeraldIceBlock extends BaseBlock implements IRenderTyped {
	public DenseEmeraldIceBlock() {
		super(BlocksHelper.copySettingsOf(Blocks.PACKED_ICE));
	}

	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getOptionalParameter(LootContextParams.TOOL);
		if (tool != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) != 0) {
			return Collections.singletonList(new ItemStack(this));
		}
		else {
			return Collections.emptyList();
		}
	}
}
