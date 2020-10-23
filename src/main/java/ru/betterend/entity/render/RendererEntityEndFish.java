package ru.betterend.entity.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EntityEndFish;
import ru.betterend.entity.model.ModelEntityEndFish;

public class RendererEntityEndFish extends MobEntityRenderer<EntityEndFish, ModelEntityEndFish> {
	private static final Identifier TEXTURE = BetterEnd.makeID("textures/entity/end_fish/end_fish_2.png");
	private static final RenderLayer GLOW = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/end_fish/end_fish_2_glow.png"));
	
    public RendererEntityEndFish(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ModelEntityEndFish(), 0.5f);
        this.addFeature(new EyesFeatureRenderer<EntityEndFish, ModelEntityEndFish>(this) {
			@Override
			public RenderLayer getEyesTexture() {
				return GLOW;
			}
        });
    }
 
    @Override
    public Identifier getTexture(EntityEndFish entity) {
        return TEXTURE;
    }
}