package ru.betterend.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import ru.betterend.blocks.entities.render.EChestBlockEntityRenderer;
import ru.betterend.blocks.entities.render.ESignBlockEntityRenderer;
import ru.betterend.blocks.entities.render.EternalPedestalItemRenderer;

@Environment(EnvType.CLIENT)
public class EndBlockEntityRenders {
	public static void register() {
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.CHEST, EChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.SIGN, ESignBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.ETERNAL_PEDESTAL, EternalPedestalItemRenderer::new);
	}
}
