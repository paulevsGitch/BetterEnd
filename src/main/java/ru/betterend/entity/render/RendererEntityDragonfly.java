package ru.betterend.entity.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EntityDragonfly;
import ru.betterend.entity.model.ModelEntityDragonfly;

public class RendererEntityDragonfly extends MobEntityRenderer<EntityDragonfly, ModelEntityDragonfly> {
	private static final Identifier TEXTURE = BetterEnd.makeID("textures/entity/dragonfly.png");
	private static final RenderLayer GLOW = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/dragonfly_glow.png"));
	
    public RendererEntityDragonfly(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ModelEntityDragonfly(), 0.5f);
        this.addFeature(new EyesFeatureRenderer<EntityDragonfly, ModelEntityDragonfly>(this) {
			@Override
			public RenderLayer getEyesTexture() {
				return GLOW;
			}
        });
    }
 
    @Override
    public Identifier getTexture(EntityDragonfly entity) {
        return TEXTURE;
    }
}