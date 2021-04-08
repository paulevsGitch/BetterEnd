package ru.betterend.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import ru.betterend.BetterEnd;
import ru.betterend.entity.SilkMothEntity;
import ru.betterend.entity.model.SilkMothEntityModel;

public class SilkMothEntityRenderer extends MobEntityRenderer<SilkMothEntity, SilkMothEntityModel> {
    private static final ResourceLocation TEXTURE = BetterEnd.makeID("textures/entity/silk_moth.png");

    public SilkMothEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new SilkMothEntityModel(), 0.5f);
    }

    @Override
    public ResourceLocation getTexture(SilkMothEntity entity) {
        return TEXTURE;
    }
}