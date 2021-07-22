package ru.betterend.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Block;
import ru.bclib.BCLib;
import ru.bclib.blocks.BaseChestBlock;
import ru.bclib.blocks.BaseSignBlock;
import ru.bclib.client.render.BaseChestBlockEntityRenderer;
import ru.bclib.client.render.BaseSignBlockEntityRenderer;
import ru.bclib.util.TranslationHelper;
import ru.betterend.BetterEnd;
import ru.betterend.events.ItemTooltipCallback;
import ru.betterend.interfaces.MultiModelItem;
import ru.betterend.item.CrystaliteArmor;
import ru.betterend.registry.EndBlockEntityRenders;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndEntitiesRenders;
import ru.betterend.registry.EndModelProviders;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndScreens;

import java.util.List;

public class BetterEndClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		//registerRenderLayers();
		EndBlockEntityRenders.register();
		EndScreens.register();
		EndParticles.register();
		EndEntitiesRenders.register();
		EndModelProviders.register();
		MultiModelItem.register();
		ClientOptions.init();
		registerTooltips();
		
		if (BCLib.isDevEnvironment()) {
			TranslationHelper.printMissingEnNames(BetterEnd.MOD_ID);
			TranslationHelper.printMissingNames(BetterEnd.MOD_ID, "ru_ru");
		}
	}
	
	public static void registerTooltips() {
		ItemTooltipCallback.EVENT.register((player, stack, context, lines) -> {
			if (stack.getItem() instanceof CrystaliteArmor) {
				boolean hasSet = false;
				if (player != null) {
					hasSet = CrystaliteArmor.hasFullSet(player);
				}
				TranslatableComponent setDesc = new TranslatableComponent("tooltip.armor.crystalite_set");
				setDesc.setStyle(Style.EMPTY.applyFormats(
					hasSet ? ChatFormatting.BLUE : ChatFormatting.DARK_GRAY,
					ChatFormatting.ITALIC
				));
				lines.add(TextComponent.EMPTY);
				lines.add(setDesc);
			}
		});
	}
}
