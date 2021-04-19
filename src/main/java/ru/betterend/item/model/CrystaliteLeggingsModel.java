package ru.betterend.item.model;

import java.util.Collections;

import com.google.common.collect.Lists;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class CrystaliteLeggingsModel extends HumanoidModel<LivingEntity> {

	public CrystaliteLeggingsModel(float scale) {
		super(RenderType::entityTranslucent, scale, 0.0F, 64, 48);
		this.body = new ModelPart(this, 16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, scale);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		this.leftLeg = new ModelPart(this, 0, 32);
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale);
		this.leftLeg.setPos(1.9F, 12.0F, 0.0F);
		this.rightLeg = new ModelPart(this, 0, 16);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale);
		this.rightLeg.setPos(-1.9F, 12.0F, 0.0F);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return Collections::emptyIterator;
	}
	
	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Lists.newArrayList(body, rightLeg, leftLeg);
	}
}
