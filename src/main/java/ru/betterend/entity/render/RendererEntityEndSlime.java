package ru.betterend.entity.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EndSlimeEntity;
import ru.betterend.entity.model.EndSlimeEntityModel;

public class RendererEntityEndSlime extends MobEntityRenderer<EndSlimeEntity, EndSlimeEntityModel<EndSlimeEntity>> {
	private static final Identifier TEXTURE[] = new Identifier[4];
	private static final RenderLayer GLOW[] = new RenderLayer[4];
	
    public RendererEntityEndSlime(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new EndSlimeEntityModel<EndSlimeEntity>(false), 0.25F);
        this.addFeature(new OverlayFeatureRenderer<EndSlimeEntity>(this));
        this.addFeature(new EyesFeatureRenderer<EndSlimeEntity, EndSlimeEntityModel<EndSlimeEntity>>(this) {
			@Override
			public RenderLayer getEyesTexture() {
				return GLOW[0];
			}
			
			@Override
			public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EndSlimeEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(GLOW[entity.getSlimeType()]);
				this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
				if (entity.isLake()) {
					this.getContextModel().renderFlower(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV);
				}
			}
        });
    }

	@Override
	public Identifier getTexture(EndSlimeEntity entity) {
		return TEXTURE[entity.getSlimeType()];
	}

	@Override
	public void render(EndSlimeEntity slimeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		this.shadowRadius = 0.25F * (float) slimeEntity.getSize();
		super.render(slimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	protected void scale(EndSlimeEntity slimeEntity, MatrixStack matrixStack, float f) {
		matrixStack.scale(0.999F, 0.999F, 0.999F);
		matrixStack.translate(0.0D, 0.0010000000474974513D, 0.0D);
		float h = (float) slimeEntity.getSize();
		float i = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (h * 0.5F + 1.0F);
		float j = 1.0F / (i + 1.0F);
		matrixStack.scale(j * h, 1.0F / j * h, j * h);
	}

	private final class OverlayFeatureRenderer<T extends EndSlimeEntity> extends FeatureRenderer<T, EndSlimeEntityModel<T>> {
		private final EndSlimeEntityModel<T> modelOrdinal = new EndSlimeEntityModel<T>(true);
		private final EndSlimeEntityModel<T> modelLake = new EndSlimeEntityModel<T>(true);

		public OverlayFeatureRenderer(FeatureRendererContext<T, EndSlimeEntityModel<T>> featureRendererContext) {
			super(featureRendererContext);
		}

		public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
			if (!livingEntity.isInvisible()) {
				if (livingEntity.isLake()) {
					VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.getTexture(livingEntity)));
					this.getContextModel().renderFlower(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(livingEntity, 0.0F));
				}
				else if (livingEntity.isAmber() || livingEntity.isChorus()) {
					VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.getTexture(livingEntity)));
					this.getContextModel().renderCrop(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(livingEntity, 0.0F));
				}
				
				EndSlimeEntityModel<T> model = livingEntity.getSlimeType() == 1 ? modelLake : modelOrdinal;
				this.getContextModel().copyStateTo(model);
				model.animateModel(livingEntity, f, g, h);
				model.setAngles(livingEntity, f, g, j, k, l);
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(livingEntity)));
				model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(livingEntity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}
	
	static {
		TEXTURE[0] = BetterEnd.makeID("textures/entity/end_slime/end_slime.png");
		TEXTURE[1] = BetterEnd.makeID("textures/entity/end_slime/end_slime_mossy.png");
		TEXTURE[2] = BetterEnd.makeID("textures/entity/end_slime/end_slime_lake.png");
		TEXTURE[3] = BetterEnd.makeID("textures/entity/end_slime/end_slime_amber.png");
		GLOW[0] = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/end_slime/end_slime_glow.png"));
		GLOW[1] = GLOW[0];
		GLOW[2] = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/end_slime/end_slime_lake_glow.png"));
		GLOW[3] = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/end_slime/end_slime_amber_glow.png"));
	}
}