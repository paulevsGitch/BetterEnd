package ru.betterend.client;

import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Block;
import ru.bclib.blocks.BaseChestBlock;
import ru.bclib.blocks.BaseSignBlock;
import ru.bclib.client.render.BaseChestBlockEntityRenderer;
import ru.bclib.client.render.BaseSignBlockEntityRenderer;
import ru.bclib.client.render.ERenderLayer;
import ru.bclib.interfaces.IRenderTyped;
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
		registerRenderers();
		registerTooltips();
		
		if (BetterEnd.isDevEnvironment()) {
			TranslationHelper.printMissingNames(BetterEnd.MOD_ID);
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
			if (block instanceof IRenderTyped) {
				ERenderLayer layer = ((IRenderTyped) block).getRenderLayer();
				if (layer == ERenderLayer.CUTOUT)
					BlockRenderLayerMap.INSTANCE.putBlock(block, cutout);
				else if (layer == ERenderLayer.TRANSLUCENT)
					BlockRenderLayerMap.INSTANCE.putBlock(block, translucent);
			}
		});
	}

	private static void registerRenderers() {
		List<Block> modBlocks = EndBlocks.getModBlocks();
		modBlocks.stream().filter(BaseChestBlock.class::isInstance).forEach(BaseChestBlockEntityRenderer::registerRenderLayer);
		modBlocks.stream().filter(BaseSignBlock.class::isInstance).forEach(BaseSignBlockEntityRenderer::registerRenderLayer);
	}
}
