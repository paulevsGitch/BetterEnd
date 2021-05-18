package ru.betterend.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import ru.betterend.BetterEnd;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.events.ItemTooltipCallback;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.interfaces.MultiModelItem;
import ru.betterend.item.CrystaliteArmor;
import ru.betterend.registry.*;
import ru.betterend.util.TranslationHelper;

public class BetterEndClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerRenderLayers();
		EndBlockEntityRenders.register();
		EndScreens.register();
		EndParticles.register();
		EndEntitiesRenders.register();
		EndModelProviders.register();
		MultiModelItem.register();
		ClientOptions.init();
		registerTooltips();
		
		if (BetterEnd.isDevEnvironment()) {
			TranslationHelper.printMissingNames();
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
				setDesc.setStyle(Style.EMPTY.applyFormats(hasSet ? ChatFormatting.BLUE : ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
				lines.add(TextComponent.EMPTY);
				lines.add(setDesc);
			}
		});
	}

	private void registerRenderLayers() {
		RenderType cutout = RenderType.cutout();
		RenderType translucent = RenderType.translucent();
		Registry.BLOCK.forEach(block -> {
			if (block instanceof IRenderTypeable) {
				ERenderLayer layer = ((IRenderTypeable) block).getRenderLayer();
				if (layer == ERenderLayer.CUTOUT)
					BlockRenderLayerMap.INSTANCE.putBlock(block, cutout);
				else if (layer == ERenderLayer.TRANSLUCENT)
					BlockRenderLayerMap.INSTANCE.putBlock(block, translucent);
			}
		});
	}
}
