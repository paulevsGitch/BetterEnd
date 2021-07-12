package ru.betterend.entity.render;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.BetterEnd;
import ru.betterend.entity.ShadowWalkerEntity;

public class RendererEntityShadowWalker extends HumanoidMobRenderer<ShadowWalkerEntity, PlayerModel<ShadowWalkerEntity>> {
	private static final ResourceLocation TEXTURE = BetterEnd.makeID("textures/entity/shadow_walker.png");
	
	public RendererEntityShadowWalker(EntityRendererProvider.Context ctx) {
		super(ctx, new PlayerModel<ShadowWalkerEntity>(ctx.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
		//super(entityRenderDispatcher, new PlayerModel<ShadowWalkerEntity>(0.0F, false), 0.5F);
	}
	
	@Override
	public ResourceLocation getTextureLocation(ShadowWalkerEntity zombieEntity) {
		return TEXTURE;
	}
}
