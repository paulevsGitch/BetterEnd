package ru.betterend.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.registry.Registry;
import ru.betterend.registry.EndBlockEntityRenders;
import ru.betterend.registry.EndEntitiesRenders;
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
	}

	private void registerRenderLayers() {
		RenderLayer cutout = RenderLayer.getCutout();
		RenderLayer translucent = RenderLayer.getTranslucent();
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
