package ru.betterend.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndItems;
import ru.betterend.util.LangUtil;
import vazkii.patchouli.api.PatchouliAPI;

public class GuideBook extends PatternedItem {

	public final static Identifier BOOK_ID = BetterEnd.makeID("guidebook");
	public static final Item GUIDE_BOOK = EndItems.registerItem(BOOK_ID, new GuideBook());
	
	public static void register() {}
	
	public GuideBook() {
		super(EndItems.makeItemSettings().maxCount(1));
	}
	
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    	if (!world.isClient && user instanceof ServerPlayerEntity) {
            PatchouliAPI.get().openBookGUI((ServerPlayerEntity) user, BOOK_ID);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(LangUtil.getText("book.betterend", "subtitle").formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
    }
}
