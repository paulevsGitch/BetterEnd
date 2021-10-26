package ru.betterend.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import ru.bclib.items.ModelProviderItem;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndItems;
import ru.betterend.util.LangUtil;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class GuideBookItem extends ModelProviderItem {
	public final static ResourceLocation BOOK_ID = BetterEnd.makeID("guidebook");
	public static final Item GUIDE_BOOK = EndItems.getItemRegistry().register(BOOK_ID, new GuideBookItem());
	
	public static void register() {
	}
	
	public GuideBookItem() {
		super(EndItems.makeEndItemSettings().stacksTo(1));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		if (!world.isClientSide && user instanceof ServerPlayer) {
			PatchouliAPI.get().openBookGUI((ServerPlayer) user, BOOK_ID);
			return InteractionResultHolder.success(user.getItemInHand(hand));
		}
		return InteractionResultHolder.consume(user.getItemInHand(hand));
	}
	
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag context) {
		tooltip.add(LangUtil.getText("book.betterend", "subtitle")
							.withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
	}
}
