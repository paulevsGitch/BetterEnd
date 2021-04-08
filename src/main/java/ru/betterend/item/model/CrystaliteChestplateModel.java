package ru.betterend.item.model;

import java.util.Collections;

import com.google.common.collect.Lists;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Arm;

public class CrystaliteChestplateModel extends BipedEntityModel<LivingEntity> {

	public ModelPart leftShoulder;
	public ModelPart rightShoulder;
	private boolean thinArms;

	public CrystaliteChestplateModel(float scale, boolean thinArms) {
		super(RenderLayer::getEntityTranslucent, scale, 0.0F, 64, 48);
		this.thinArms = thinArms;
		this.torso = new ModelPart(this, 16, 16);
		this.torso.addCuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, scale + 0.25F);
		this.torso.setPivot(0.0F, 0.0F, 0.0F);
		if (thinArms) {
			this.leftShoulder = new ModelPart(this, 41, 32);
			this.leftShoulder.addCuboid(-1.0F, -2.5F, -2.0F, 3.0F, 12.0F, 4.0F, scale + 0.35F);
			this.leftShoulder.setPivot(5.0F, 2.5F, 0.0F);
			this.leftShoulder.mirror = true;
			this.rightShoulder = new ModelPart(this, 41, 16);
			this.rightShoulder.addCuboid(-2.0F, -2.5F, -2.0F, 3.0F, 12.0F, 4.0F, scale + 0.35F);
			this.rightShoulder.setPivot(-5.0F, 2.5F, 10.0F);
		} else {
			this.leftShoulder = new ModelPart(this, 40, 32);
			this.leftShoulder.addCuboid(-1.0F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.45F);
			this.leftShoulder.setPivot(5.0F, 2.0F, 0.0F);
			this.leftShoulder.mirror = true;
			this.rightShoulder = new ModelPart(this, 40, 16);
			this.rightShoulder.addCuboid(-3.0F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.45F);
			this.rightShoulder.setPivot(-5.0F, 2.0F, 10.0F);
		}
	}

	@Override
	public void setAttributes(BipedEntityModel<LivingEntity> bipedEntityModel) {
		super.setAttributes(bipedEntityModel);
		this.leftShoulder.copyPositionAndRotation(leftArm);
		this.rightShoulder.copyPositionAndRotation(rightArm);
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
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		ModelPart modelPart = this.getArm(arm);
		if (this.thinArms) {
			float f = 0.5F * (float) (arm == Arm.RIGHT ? 1 : -1);
			modelPart.pivotX += f;
			modelPart.rotate(matrices);
			modelPart.pivotX -= f;
		} else {
			modelPart.rotate(matrices);
		}
	}
}
