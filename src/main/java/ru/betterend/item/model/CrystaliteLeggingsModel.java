package ru.betterend.item.model;

import java.util.Collections;

import com.google.common.collect.Lists;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.world.entity.LivingEntity;

public class CrystaliteLeggingsModel extends BipedEntityModel<LivingEntity> {

	public CrystaliteLeggingsModel(float scale) {
		super(RenderLayer::getEntityTranslucent, scale, 0.0F, 64, 48);
		this.torso = new ModelPart(this, 16, 16);
		this.torso.addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, scale);
		this.torso.setPivot(0.0F, 0.0F, 0.0F);
		this.leftLeg = new ModelPart(this, 0, 32);
		this.leftLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale);
		this.leftLeg.setPivot(1.9F, 12.0F, 0.0F);
		this.rightLeg = new ModelPart(this, 0, 16);
		this.rightLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale);
		this.rightLeg.setPivot(-1.9F, 12.0F, 0.0F);
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return Collections::emptyIterator;
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return Lists.newArrayList(torso, rightLeg, leftLeg);
	}
}
