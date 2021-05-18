package ru.betterend.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.interfaces.MultiModelItem;
import ru.betterend.registry.EndBlockEntityRenders;
import ru.betterend.registry.EndEntitiesRenders;
import ru.betterend.registry.EndModelProviders;
import ru.betterend.registry.EndParticles;
import ru.betterend.registry.EndScreens;
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
		
		if (BetterEnd.isDevEnvironment()) {
			TranslationHelper.printMissingNames();
		}
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
