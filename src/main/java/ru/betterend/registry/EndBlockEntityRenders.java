package ru.betterend.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import ru.betterend.client.render.PedestalItemRenderer;

@Environment(EnvType.CLIENT)
public class EndBlockEntityRenders {
	public static void register() {
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.PEDESTAL, PedestalItemRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.ETERNAL_PEDESTAL, PedestalItemRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.INFUSION_PEDESTAL, PedestalItemRenderer::new);
	}
}
