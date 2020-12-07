package ru.betterend.registry;

import java.util.function.Function;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.EntityType;
import ru.betterend.entity.render.RendererEntityDragonfly;
import ru.betterend.entity.render.RendererEntityEndFish;
import ru.betterend.entity.render.RendererEntityEndSlime;
import ru.betterend.entity.render.RendererEntityCubozoa;
import ru.betterend.entity.render.RendererEntityShadowWalker;

public class EndEntitiesRenders {
	
	public static void register() {
		register(EndEntities.DRAGONFLY, RendererEntityDragonfly::new);
		register(EndEntities.END_SLIME, RendererEntityEndSlime::new);
		register(EndEntities.END_FISH, RendererEntityEndFish::new);
		register(EndEntities.SHADOW_WALKER, RendererEntityShadowWalker::new);
		register(EndEntities.CUBOZOA, RendererEntityCubozoa::new);
	}
	
	private static void register(EntityType<?> type, Function<EntityRenderDispatcher, MobEntityRenderer<?, ?>> render) {
		EntityRendererRegistry.INSTANCE.register(type, (entityRenderDispatcher, context) -> {
			return render.apply(entityRenderDispatcher);
		});
	}
}
