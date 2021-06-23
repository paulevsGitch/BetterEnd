package ru.betterend.registry;

import java.util.function.Function;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import ru.betterend.BetterEnd;
import ru.betterend.entity.model.CubozoaEntityModel;
import ru.betterend.entity.model.RendererEntityCubozoa;
import ru.betterend.entity.render.RendererEntityDragonfly;
import ru.betterend.entity.render.RendererEntityEndFish;
import ru.betterend.entity.render.RendererEntityEndSlime;
import ru.betterend.entity.render.RendererEntityShadowWalker;
import ru.betterend.entity.render.SilkMothEntityRenderer;

public class EndEntitiesRenders {
	public static final ModelLayerLocation DRAGONFLY_MODEL = registerMain("dragonfly");
	
	public static void register() {
		register(EndEntities.DRAGONFLY, RendererEntityDragonfly.class);
		register(EndEntities.END_SLIME, RendererEntityEndSlime.class);
		register(EndEntities.END_FISH, RendererEntityEndFish.class);
		register(EndEntities.SHADOW_WALKER, RendererEntityShadowWalker.class);
		register(EndEntities.CUBOZOA, RendererEntityCubozoa.class);
		register(EndEntities.SILK_MOTH, SilkMothEntityRenderer.class);

		EntityModelLayerRegistry.registerModelLayer(DRAGONFLY_MODEL, CubozoaEntityModel::getTexturedModelData);
	}

	private static void register(EntityType<?> type, Class<? extends MobRenderer<?, ?>> renderer) {
		EntityRendererRegistry.INSTANCE.register(type, (context) -> {
			MobRenderer render = null;
			try {
				render = renderer.getConstructor(context.getClass()).newInstance(context);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return render;
		});
	}

	private static ModelLayerLocation registerMain(String id){
		return new ModelLayerLocation(new ResourceLocation(BetterEnd.MOD_ID, id), "main");
	}
}
