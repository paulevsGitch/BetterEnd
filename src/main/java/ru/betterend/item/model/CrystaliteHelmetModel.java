package ru.betterend.item.model;

import java.util.Collections;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class CrystaliteHelmetModel extends HumanoidModel<LivingEntity> {

	public CrystaliteHelmetModel(float scale) {
		super(RenderType::entityTranslucent, scale, 0.0F, 64, 48);
		this.hat = new ModelPart(this, 0, 0);
		this.hat.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, scale + 0.5F);
		this.hat.setPos(0.0F, 0.0F, 0.0F);
	}
	
	@Override
	protected Iterable<ModelPart> headParts() {
		return Collections::emptyIterator;
	}
	
	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Lists.newArrayList(hat);
	}
}
