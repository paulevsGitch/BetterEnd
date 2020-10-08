package ru.betterend.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.registry.Registry;
import ru.betterend.blocks.model.EndModelProvider;
import ru.betterend.registry.BlockEntityRenderRegistry;
import ru.betterend.registry.EntityRenderRegistry;
import ru.betterend.registry.ParticleRegistry;
import ru.betterend.registry.ScreensRegistry;

public class BetterEndClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerRenderLayers();
		BlockEntityRenderRegistry.register();
		ScreensRegistry.register();
		ParticleRegistry.register();
		EntityRenderRegistry.register();
		
		ModelLoadingRegistry.INSTANCE.registerResourceProvider(resorceManager -> new EndModelProvider());
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
