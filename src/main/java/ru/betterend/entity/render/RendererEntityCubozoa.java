package ru.betterend.entity.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EntityCubozoa;
import ru.betterend.entity.model.ModelEntityCubozoa;

public class RendererEntityCubozoa extends MobEntityRenderer<EntityCubozoa, ModelEntityCubozoa> {
	private static final Identifier TEXTURE = BetterEnd.makeID("textures/entity/cubozoa/cubozoa.png");
	private static final RenderLayer GLOW = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/cubozoa/cubozoa_glow.png"));

	public RendererEntityCubozoa(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher, new ModelEntityCubozoa(), 0.5f);
		this.addFeature(new EyesFeatureRenderer<EntityCubozoa, ModelEntityCubozoa>(this) {
			@Override
			public RenderLayer getEyesTexture() {
				return GLOW;
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
		return TEXTURE;
	}

	/*@Override
	protected void setupTransforms(EntityJello squidEntity, MatrixStack matrixStack, float f, float g, float h) {
		float i = MathHelper.lerp(h, squidEntity.prevTiltAngle, squidEntity.tiltAngle);
		float j = MathHelper.lerp(h, squidEntity.prevRollAngle, squidEntity.rollAngle);
		matrixStack.translate(0, -0.5F, 0);
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - g));
		matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(i));
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(j));
	}*/
}