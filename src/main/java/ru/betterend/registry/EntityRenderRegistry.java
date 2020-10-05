package ru.betterend.registry;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.EntityType;
import ru.betterend.entity.render.RendererEntityDragonfly;
import ru.betterend.entity.render.RendererEntityEndSlime;

public class EntityRenderRegistry {
	
	public static void register() {
		register(EntityRegistry.DRAGONFLY, RendererEntityDragonfly.class);
		register(EntityRegistry.END_SLIME, RendererEntityEndSlime.class);
	}
	
	private static void register(EntityType<?> type, Class<? extends MobEntityRenderer<?, ?>> renderer) {
		EntityRendererRegistry.INSTANCE.register(type, (entityRenderDispatcher, context) -> {
			MobEntityRenderer<?, ?> render = null;
			try {
				render = renderer.getConstructor(entityRenderDispatcher.getClass()).newInstance(entityRenderDispatcher);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return render;
		});
	}
}
