package ru.betterend.item.model;

import java.util.Collections;

import com.google.common.collect.Lists;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

public class CrystaliteChestplateModel extends BipedEntityModel<LivingEntity> {

	public ModelPart leftShoulder;
	public ModelPart rightShoulder;
	
	public CrystaliteChestplateModel(float scale, boolean thinArms) {
		super(RenderLayer::getEntityTranslucent, scale, 0.0F, 64, 32);
		this.torso = new ModelPart(this, 16, 16);
		this.torso.addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, scale);
		this.torso.setPivot(0.0F, 0.0F, 0.0F);
		if (thinArms) {
			this.leftShoulder = new ModelPart(this, 40, 16);
			this.leftShoulder.addCuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, scale + 0.25F);
			this.leftShoulder.setPivot(5.0F, 2.5F, 0.0F);
			this.rightShoulder = new ModelPart(this, 40, 16);
			this.rightShoulder.addCuboid(-2.0F, -2.0F, -12.0F, 3.0F, 12.0F, 4.0F, scale + 0.25F);
			this.rightShoulder.setPivot(-5.0F, 2.5F, 10.0F);
		} else {
			this.leftShoulder = new ModelPart(this, 40, 16);
			this.leftShoulder.addCuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.25F);
			this.leftShoulder.setPivot(5.0F, 2.0F, 0.0F);
			this.rightShoulder = new ModelPart(this, 40, 16);
			this.rightShoulder.addCuboid(-3.0F, -2.0F, -12.0F, 4.0F, 12.0F, 4.0F, scale + 0.25F);
			this.rightShoulder.setPivot(-5.0F, 2.0F, 10.0F);
		}
	}
	
	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return Collections::emptyIterator;
	}
	
	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return Lists.newArrayList(torso, leftShoulder, rightShoulder);
	}
	
	@Override
	public void setAngles(LivingEntity livingEntity, float f, float g, float h, float i, float j) {
		super.setAngles(livingEntity, f, g, h, i, j);
		this.leftShoulder.copyPositionAndRotation(leftArm);
		this.rightShoulder.copyPositionAndRotation(rightArm);
	}
}
