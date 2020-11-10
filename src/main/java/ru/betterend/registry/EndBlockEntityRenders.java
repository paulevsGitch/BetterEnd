package ru.betterend.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import ru.betterend.blocks.entities.render.EndChestBlockEntityRenderer;
import ru.betterend.blocks.entities.render.EndSignBlockEntityRenderer;
import ru.betterend.blocks.entities.render.EternalPedestalBlockEntityRenderer;
import ru.betterend.blocks.entities.render.InfusionPedestalBlockEntityRenderer;
import ru.betterend.blocks.entities.render.PedestalBlockEntityRenderer;

@Environment(EnvType.CLIENT)
public class EndBlockEntityRenders {
	public static void register() {
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.CHEST, EndChestBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.SIGN, EndSignBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.PEDESTAL, PedestalBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.ETERNAL_PEDESTAL, EternalPedestalBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(EndBlockEntities.INFUSION_PEDESTAL, InfusionPedestalBlockEntityRenderer::new);
	}
}
