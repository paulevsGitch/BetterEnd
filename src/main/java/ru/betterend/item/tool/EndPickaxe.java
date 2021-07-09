package ru.betterend.item.tool;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.items.tool.BasePickaxeItem;

public class EndPickaxe extends BasePickaxeItem {
	public EndPickaxe(Tier material, int attackDamage, float attackSpeed, Properties settings) {
		super(material, attackDamage, attackSpeed, settings);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (state.is(Blocks.END_STONE) && this.getTier().getLevel() > 2) {
			return this.speed * 3;
		}
		return super.getDestroySpeed(stack, state);
	}
}
