package ru.betterend.entity.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.BetterEnd;
import ru.betterend.entity.DragonflyEntity;
import ru.betterend.entity.model.DragonflyEntityModel;

public class RendererEntityDragonfly extends MobEntityRenderer<DragonflyEntity, DragonflyEntityModel> {
    private static final ResourceLocation TEXTURE = BetterEnd.makeID("textures/entity/dragonfly.png");
    private static final RenderLayer GLOW = RenderLayer.getEyes(BetterEnd.makeID("textures/entity/dragonfly_glow.png"));

    public RendererEntityDragonfly(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new DragonflyEntityModel(), 0.5f);
        this.addFeature(new EyesFeatureRenderer<DragonflyEntity, DragonflyEntityModel>(this) {
            @Override
            public RenderLayer getEyesTexture() {
                return GLOW;
            }
        });
    }

    @Override
    public ResourceLocation getTexture(DragonflyEntity entity) {
        return TEXTURE;
    }
}