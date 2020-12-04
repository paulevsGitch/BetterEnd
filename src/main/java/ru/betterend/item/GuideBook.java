package ru.betterend.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndItems;
import vazkii.patchouli.api.PatchouliAPI;

public class GuideBook extends PatternedItem {

	private static final Style TEXT_STYLE = Style.EMPTY.withColor(TextColor.fromRgb(0x7e6b9a)).withItalic(true);
	public final static Identifier BOOK_ID = BetterEnd.makeID("guidebook");
	
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
        tooltip.add(new TranslatableText("book.betterend.subtitle").setStyle(TEXT_STYLE));
    }
}
