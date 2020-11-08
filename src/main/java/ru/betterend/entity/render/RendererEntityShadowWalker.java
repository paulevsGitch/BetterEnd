package ru.betterend.entity.render;

import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EntityShadowWalker;

public class RendererEntityShadowWalker extends BipedEntityRenderer<EntityShadowWalker, PlayerEntityModel<EntityShadowWalker>> {
	private static final Identifier TEXTURE = BetterEnd.makeID("textures/entity/shadow_walker.png");
	
	public RendererEntityShadowWalker(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new PlayerEntityModel<EntityShadowWalker>(0.0F, false), 0.5F);
	}

	@Override
	public Identifier getTexture(EntityShadowWalker zombieEntity) {
		return TEXTURE;
	}
}
