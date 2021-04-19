package ru.betterend.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EndSlimeEntity;
import ru.betterend.entity.model.EndSlimeEntityModel;

public class RendererEntityEndSlime extends MobRenderer<EndSlimeEntity, EndSlimeEntityModel<EndSlimeEntity>> {
	private static final ResourceLocation TEXTURE[] = new ResourceLocation[4];
	private static final RenderType GLOW[] = new RenderType[4];

	public RendererEntityEndSlime(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new EndSlimeEntityModel<EndSlimeEntity>(false), 0.25F);
		this.addLayer(new OverlayFeatureRenderer<EndSlimeEntity>(this));
		this.addLayer(new EyesLayer<EndSlimeEntity, EndSlimeEntityModel<EndSlimeEntity>>(this) {
			@Override
			public RenderType renderType() {
				return GLOW[0];
			}

			@Override
			public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, EndSlimeEntity entity,
					float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw,
					float headPitch) {
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(GLOW[entity.getSlimeType()]);
				this.getParentModel().renderToBuffer(matrices, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY,
						1.0F, 1.0F, 1.0F, 1.0F);
				if (entity.isLake()) {
					this.getParentModel().renderFlower(matrices, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY);
				}
			}
		});
	}

	@Override
	public ResourceLocation getTextureLocation(EndSlimeEntity entity) {
		return TEXTURE[entity.getSlimeType()];
	}

	@Override
	public void render(EndSlimeEntity slimeEntity, float f, float g, PoseStack matrixStack,
			MultiBufferSource vertexConsumerProvider, int i) {
		this.shadowRadius = 0.25F * (float) slimeEntity.getSize();
		super.render(slimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	protected void scale(EndSlimeEntity slimeEntity, PoseStack matrixStack, float f) {
		matrixStack.scale(0.999F, 0.999F, 0.999F);
		matrixStack.translate(0.0D, 0.0010000000474974513D, 0.0D);
		float h = (float) slimeEntity.getSize();
		float i = Mth.lerp(f, slimeEntity.oSquish, slimeEntity.squish) / (h * 0.5F + 1.0F);
		float j = 1.0F / (i + 1.0F);
		matrixStack.scale(j * h, 1.0F / j * h, j * h);
	}

	private final class OverlayFeatureRenderer<T extends EndSlimeEntity>
			extends RenderLayer<T, EndSlimeEntityModel<T>> {
		private final EndSlimeEntityModel<T> modelOrdinal = new EndSlimeEntityModel<T>(true);
		private final EndSlimeEntityModel<T> modelLake = new EndSlimeEntityModel<T>(true);

		public OverlayFeatureRenderer(RenderLayerParent<T, EndSlimeEntityModel<T>> featureRendererContext) {
			super(featureRendererContext);
		}

		public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, T livingEntity,
				float f, float g, float h, float j, float k, float l) {
			if (!livingEntity.isInvisible()) {
				if (livingEntity.isLake()) {
					VertexConsumer vertexConsumer = vertexConsumerProvider
							.getBuffer(RenderType.entityCutout(this.getTextureLocation(livingEntity)));
					this.getParentModel().renderFlower(matrixStack, vertexConsumer, i,
							LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F));
				} else if (livingEntity.isAmber() || livingEntity.isChorus()) {
					VertexConsumer vertexConsumer = vertexConsumerProvider
							.getBuffer(RenderType.entityCutout(this.getTextureLocation(livingEntity)));
					this.getParentModel().renderCrop(matrixStack, vertexConsumer, i,
							LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F));
				}

				EndSlimeEntityModel<T> model = livingEntity.getSlimeType() == 1 ? modelLake : modelOrdinal;
				this.getParentModel().copyPropertiesTo(model);
				model.prepareMobModel(livingEntity, f, g, h);
				model.setupAnim(livingEntity, f, g, j, k, l);
				VertexConsumer vertexConsumer = vertexConsumerProvider
						.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(livingEntity)));
				model.renderToBuffer(matrixStack, vertexConsumer, i,
						LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	static {
		TEXTURE[0] = BetterEnd.makeID("textures/entity/end_slime/end_slime.png");
		TEXTURE[1] = BetterEnd.makeID("textures/entity/end_slime/end_slime_mossy.png");
		TEXTURE[2] = BetterEnd.makeID("textures/entity/end_slime/end_slime_lake.png");
		TEXTURE[3] = BetterEnd.makeID("textures/entity/end_slime/end_slime_amber.png");
		GLOW[0] = RenderType.eyes(BetterEnd.makeID("textures/entity/end_slime/end_slime_glow.png"));
		GLOW[1] = GLOW[0];
		GLOW[2] = RenderType.eyes(BetterEnd.makeID("textures/entity/end_slime/end_slime_lake_glow.png"));
		GLOW[3] = RenderType.eyes(BetterEnd.makeID("textures/entity/end_slime/end_slime_amber_glow.png"));
	}
}