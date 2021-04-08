package ru.betterend.item;

import java.util.List;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.level.Level;
import ru.betterend.BetterEnd;
import ru.betterend.registry.EndItems;
import ru.betterend.util.LangUtil;
import vazkii.patchouli.api.PatchouliAPI;

public class GuideBookItem extends PatternedItem {
    public final static ResourceLocation BOOK_ID = BetterEnd.makeID("guidebook");
    public static final Item GUIDE_BOOK = EndItems.registerItem(BOOK_ID, new GuideBookItem());

    public static void register() {
    }

    public GuideBookItem() {
        super(EndItems.makeItemSettings().maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(Level world, PlayerEntity user, Hand hand) {
        if (!world.isClientSide && user instanceof ServerPlayer) {
            PatchouliAPI.get().openBookGUI((ServerPlayer) user, BOOK_ID);
            return TypedActionResult.success(user.getStackInHand(hand));
        }
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, Level world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(
                LangUtil.getText("book.betterend", "subtitle").formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
    }
}
