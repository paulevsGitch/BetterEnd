package ru.betterend.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import ru.betterend.entity.EntityDragonfly;
import ru.betterend.entity.model.ModelEntityDragonfly;

public class RendererEntityDragonfly extends MobEntityRenderer<EntityDragonfly, ModelEntityDragonfly> {
 
    public RendererEntityDragonfly(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new ModelEntityDragonfly(), 0.5f);
    }
 
    @Override
    public Identifier getTexture(EntityDragonfly entity) {
        return new Identifier("textures/block/stone.png");
    }
}