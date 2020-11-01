package ru.betterend.blocks.basis;

import java.util.List;

import com.google.common.collect.Lists;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.OreBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.MathHelper;
import ru.betterend.util.MHelper;

public class BlockOre extends OreBlock {
	private final Item dropItem;
	private final int minCount;
	private final int maxCount;
	
	public BlockOre(Item drop, int minCount, int maxCount) {
		super(FabricBlockSettings.of(Material.STONE, MaterialColor.SAND)
				.hardness(3F)
				.resistance(9F)
				.requiresTool()
				.sounds(BlockSoundGroup.STONE));
		this.dropItem = drop;
		this.minCount = minCount;
		this.maxCount = maxCount;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		if (tool != null && tool.isEffectiveOn(state)) {
			int count = 0;
			int fortune = EnchantmentHelper.getLevel(Enchantments.FORTUNE, tool);
			if (fortune > 0) {
				int min = MathHelper.clamp(minCount + fortune, minCount, maxCount);
				int max = maxCount + (fortune / Enchantments.FORTUNE.getMaxLevel());
				if (min == max) {
					return Lists.newArrayList(new ItemStack(dropItem, max));
				}
				count = MHelper.randRange(min, max, MHelper.RANDOM);
			} else {
				count = MHelper.randRange(minCount, maxCount, MHelper.RANDOM);
			}
			return Lists.newArrayList(new ItemStack(dropItem, count));
		}
		return Lists.newArrayList();
	}
}
