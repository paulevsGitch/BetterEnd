package ru.betterend.entity.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EntityCubozoa;
import ru.betterend.entity.model.ModelEntityCubozoa;

public class RendererEntityCubozoa extends MobEntityRenderer<EntityCubozoa, ModelEntityCubozoa> {
	private static final Identifier[] TEXTURE = new Identifier[2];
	private static final RenderLayer[] GLOW = new RenderLayer[2];

	public RendererEntityCubozoa(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new ModelEntityCubozoa(), 0.5f);
		this.addFeature(new EyesFeatureRenderer<EntityCubozoa, ModelEntityCubozoa>(this) {
			@Override
			public RenderLayer getEyesTexture() {
				return GLOW[0];
			}

			@Override
			public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EntityCubozoa entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(GLOW[entity.getVariant()]);
				this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
			}
        });
	}

	@Override
	protected void scale(EntityCubozoa entity, MatrixStack matrixStack, float f) {
		float scale = entity.getScale();
		matrixStack.scale(scale, scale, scale);
	}

	@Override
	public Identifier getTexture(EntityCubozoa entity) {
		return TEXTURE[entity.getVariant()];
	}
	
	static {
		TEXTURE[0] = BetterEnd.makeID("textures/entity/cubozoa/cubozoa.png");
		TEXTURE[1] = BetterEnd.makeID("textures/entity/cubozoa/cubozoa_sulphur.png");
		
		GLOW[0] = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/cubozoa/cubozoa_glow.png"));
		GLOW[1] = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/cubozoa/cubozoa_sulphur_glow.png"));
	}
}