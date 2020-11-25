package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;
import ru.betterend.util.MHelper;

public class BlockOre extends OreBlock implements BlockPatterned {
	private final Item dropItem;
	private final int minCount;
	private final int maxCount;
	private final int expirience;
	
	public BlockOre(Item drop, int minCount, int maxCount, int expirience) {
		super(FabricBlockSettings.of(Material.STONE, MaterialColor.SAND)
				.hardness(3F)
				.resistance(9F)
				.requiresTool()
				.sounds(BlockSoundGroup.STONE));
		this.dropItem = drop;
		this.minCount = minCount;
		this.maxCount = maxCount;
		this.expirience = expirience;
	}
	
	@Override
	protected int getExperienceWhenMined(Random random) {
		return this.expirience > 0 ? random.nextInt(expirience) + 1 : 0;
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		if (tool != null && tool.isEffectiveOn(state)) {
			if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tool) > 0) {
				return Collections.singletonList(new ItemStack(this));
			}
			int count = 0;
			int enchantment = EnchantmentHelper.getLevel(Enchantments.FORTUNE, tool);
			if (enchantment > 0) {
				int min = MathHelper.clamp(minCount + enchantment, minCount, maxCount);
				int max = maxCount + (enchantment / Enchantments.FORTUNE.getMaxLevel());
				if (min == max) {
					return Collections.singletonList(new ItemStack(dropItem, max));
				}
				count = MHelper.randRange(min, max, MHelper.RANDOM);
			} else {
				count = MHelper.randRange(minCount, maxCount, MHelper.RANDOM);
			}
			return Collections.singletonList(new ItemStack(dropItem, count));
		}
		return Collections.emptyList();
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getId(this).getPath();
		return Patterns.createJson(data, block, block);
	}
	
	@Override
	public String getModelPattern(String block) {
		Identifier blockId = Registry.BLOCK.getId(this);
		return Patterns.createJson(Patterns.BLOCK_BASE, blockId.getPath(), block);
	}
	
	@Override
	public Identifier statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
}
