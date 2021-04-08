package ru.betterend.item.model;

import java.util.Collections;

import com.google.common.collect.Lists;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.world.entity.LivingEntity;

public class CrystaliteBootsModel extends BipedEntityModel<LivingEntity> {

	public ModelPart leftBoot;
	public ModelPart rightBoot;

	public CrystaliteBootsModel(float scale) {
		super(RenderLayer::getEntityTranslucent, scale, 0.0F, 64, 48);
		this.leftBoot = new ModelPart(this, 0, 32);
		this.leftBoot.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.25F);
		this.leftBoot.setPivot(1.9F, 12.0F, 0.0F);
		this.rightBoot = new ModelPart(this, 0, 16);
		this.rightBoot.addCuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.25F);
		this.rightBoot.setPivot(-1.9F, 12.0F, 0.0F);
	}

	@Override
	public void setAttributes(BipedEntityModel<LivingEntity> bipedEntityModel) {
		super.setAttributes(bipedEntityModel);
		this.leftBoot.copyPositionAndRotation(leftLeg);
		this.rightBoot.copyPositionAndRotation(rightLeg);
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return Collections::emptyIterator;
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return Lists.newArrayList(leftBoot, rightBoot);
	}
}
