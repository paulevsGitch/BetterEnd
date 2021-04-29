package ru.betterend.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class DrinkItem extends PatternedItem {
	public DrinkItem(Properties settings) {
		super(settings);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		return ItemUtils.useDrink(world, user, hand);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (user instanceof ServerPlayer) {
			ServerPlayer serverPlayerEntity = (ServerPlayer) user;
			CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.awardStat(Stats.ITEM_USED.get(this));
		}

		if (user instanceof Player && !((Player) user).abilities.instabuild) {
			stack.shrink(1);
		}

		if (!world.isClientSide) {
			user.removeAllEffects();
		}

		return stack.isEmpty() ? new ItemStack(Items.GLASS_BOTTLE) : stack;
	}
}
