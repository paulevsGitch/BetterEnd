package ru.betterend.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import ru.betterend.BetterEnd;
import ru.betterend.entity.EntityJello;
import ru.betterend.entity.model.ModelEntityJello;

public class RendererEntityJello extends MobEntityRenderer<EntityJello, ModelEntityJello> {
	private static final Identifier TEXTURE = BetterEnd.makeID("textures/entity/jello.png");
	
    public RendererEntityJello(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ModelEntityJello(), 0.5f);
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