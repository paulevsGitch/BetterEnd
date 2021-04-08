package ru.betterend.entity.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EndFishEntity;
import ru.betterend.entity.model.EndFishEntityModel;

public class RendererEntityEndFish extends MobEntityRenderer<EndFishEntity, EndFishEntityModel> {
	private static final ResourceLocation[] TEXTURE = new ResourceLocation[EndFishEntity.VARIANTS];
	private static final RenderLayer[] GLOW = new RenderLayer[EndFishEntity.VARIANTS];

	public RendererEntityEndFish(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new EndFishEntityModel(), 0.5f);
		this.addFeature(new EyesFeatureRenderer<EndFishEntity, EndFishEntityModel>(this) {
			@Override
			public RenderLayer getEyesTexture() {
				return GLOW[0];
			}

			@Override
			public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
					EndFishEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress,
					float headYaw, float headPitch) {
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(GLOW[entity.getVariant()]);
				this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F,
						1.0F, 1.0F);
			}
		});
	}

	@Override
	protected void scale(EndFishEntity entity, MatrixStack matrixStack, float f) {
		float scale = entity.getScale();
		matrixStack.scale(scale, scale, scale);
	}

	@Override
	public ResourceLocation getTexture(EndFishEntity entity) {
		return TEXTURE[entity.getVariant()];
	}

	static {
		for (int i = 0; i < EndFishEntity.VARIANTS; i++) {
			TEXTURE[i] = BetterEnd.makeID("textures/entity/end_fish/end_fish_" + i + ".png");
			GLOW[i] = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/end_fish/end_fish_" + i + "_glow.png"));
		}
	}
}