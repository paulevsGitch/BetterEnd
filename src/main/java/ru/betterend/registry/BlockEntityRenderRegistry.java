package ru.betterend.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import ru.betterend.blocks.entities.render.EChestBlockEntityRenderer;
import ru.betterend.blocks.entities.render.ESignBlockEntityRenderer;

public class BlockEntityRenderRegistry {
	@Environment(EnvType.CLIENT)
	public static void register() {
		BlockEntityRendererRegistry.INSTANCE.register(BlockEntityRegistry.CHEST, EChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(BlockEntityRegistry.SIGN, ESignBlockEntityRenderer::new);
	}
}
