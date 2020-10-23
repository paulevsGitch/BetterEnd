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
import ru.betterend.entity.EntityEndFish;
import ru.betterend.entity.model.ModelEntityEndFish;

public class RendererEntityEndFish extends MobEntityRenderer<EntityEndFish, ModelEntityEndFish> {
	private static final Identifier[] TEXTURE = new Identifier[EntityEndFish.VARIANTS];
	private static final RenderLayer[] GLOW = new RenderLayer[EntityEndFish.VARIANTS];
	
    public RendererEntityEndFish(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ModelEntityEndFish(), 0.5f);
        this.addFeature(new EyesFeatureRenderer<EntityEndFish, ModelEntityEndFish>(this) {
			@Override
			public RenderLayer getEyesTexture() {
				return GLOW[0];
			}

			@Override
			public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EntityEndFish entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
				VertexConsumer vertexConsumer = vertexConsumers.getBuffer(GLOW[entity.getVariant()]);
				this.getContextModel().render(matrices, vertexConsumer, 15728640, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
			}
        });
    }
 
    @Override
	protected void scale(EntityEndFish entity, MatrixStack matrixStack, float f) {
		float scale = entity.getScale();
		matrixStack.scale(scale, scale, scale);
	}
    
    @Override
    public Identifier getTexture(EntityEndFish entity) {
        return TEXTURE[entity.getVariant()];
    }
    
    static {
    	for (int i = 0; i < EntityEndFish.VARIANTS; i++) {
    		TEXTURE[i] = BetterEnd.makeID("textures/entity/end_fish/end_fish_" + i + ".png");
    		GLOW[i] = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/end_fish/end_fish_" + i + "_glow.png"));
    	}
    }
}