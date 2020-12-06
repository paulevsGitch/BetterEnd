package ru.betterend.entity.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EntityJello;
import ru.betterend.entity.model.ModelEntityJello;

public class RendererEntityJello extends MobEntityRenderer<EntityJello, ModelEntityJello> {
	private static final Identifier TEXTURE = BetterEnd.makeID("textures/entity/jello.png");
	private static final RenderLayer GLOW = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/jello_glow.png"));
	
    public RendererEntityJello(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ModelEntityJello(), 0.5f);
        this.addFeature(new EyesFeatureRenderer<EntityJello, ModelEntityJello>(this) {
			@Override
			public RenderLayer getEyesTexture() {
				return GLOW;
			}
        });
    }
 
    @Override
	protected void scale(EntityJello entity, MatrixStack matrixStack, float f) {
		float scale = entity.getScale();
		matrixStack.scale(scale, scale, scale);
	}
    
    @Override
    public Identifier getTexture(EntityJello entity) {
        return TEXTURE;
    }
}