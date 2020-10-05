package ru.betterend.entity.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;

public class RendererEntityEndSlime extends SlimeEntityRenderer {
	private static final Identifier TEXTURE = BetterEnd.makeID("textures/entity/end_slime.png");
	private static final RenderLayer GLOW = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/end_slime_glow.png"));
	
    public RendererEntityEndSlime(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
        this.addFeature(new EyesFeatureRenderer<SlimeEntity, SlimeEntityModel<SlimeEntity>>(this) {
			@Override
			public RenderLayer getEyesTexture() {
				return GLOW;
			}
        });
    }
 
    @Override
    public Identifier getTexture(SlimeEntity entity) {
        return TEXTURE;
    }
}