package ru.betterend.entity.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.BetterEnd;
import ru.betterend.entity.DragonflyEntity;
import ru.betterend.entity.model.DragonflyEntityModel;

public class RendererEntityDragonfly extends MobRenderer<DragonflyEntity, DragonflyEntityModel> {
    private static final ResourceLocation TEXTURE = BetterEnd.makeID("textures/entity/dragonfly.png");
    private static final RenderType GLOW = RenderType.eyes(BetterEnd.makeID("textures/entity/dragonfly_glow.png"));

    public RendererEntityDragonfly(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new DragonflyEntityModel(), 0.5f);
        this.addLayer(new EyesLayer<DragonflyEntity, DragonflyEntityModel>(this) {
            @Override
            public RenderType renderType() {
                return GLOW;
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(DragonflyEntity entity) {
        return TEXTURE;
    }
}