package ru.betterend.entity.render;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.BetterEnd;
import ru.betterend.entity.ShadowWalkerEntity;

public class RendererEntityShadowWalker
		extends HumanoidMobRenderer<ShadowWalkerEntity, PlayerModel<ShadowWalkerEntity>> {
	private static final ResourceLocation TEXTURE = BetterEnd.makeID("textures/entity/shadow_walker.png");

	public RendererEntityShadowWalker(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new PlayerModel<ShadowWalkerEntity>(0.0F, false), 0.5F);
	}

	@Override
	public ResourceLocation getTextureLocation(ShadowWalkerEntity zombieEntity) {
		return TEXTURE;
	}
}
