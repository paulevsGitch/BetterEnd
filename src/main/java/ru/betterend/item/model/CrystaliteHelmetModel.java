package ru.betterend.item.model;

import java.util.Collections;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class CrystaliteHelmetModel extends BipedEntityModel<LivingEntity> {

	public CrystaliteHelmetModel(float scale) {
		super(RenderLayer::getEntityTranslucent, scale, 0.0F, 64, 48);
		this.helmet = new ModelPart(this, 0, 0);
		this.helmet.addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, scale + 0.5F);
		this.helmet.setPivot(0.0F, 0.0F, 0.0F);
	}
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return Collections::emptyIterator;
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return Lists.newArrayList(helmet);
	}
}
