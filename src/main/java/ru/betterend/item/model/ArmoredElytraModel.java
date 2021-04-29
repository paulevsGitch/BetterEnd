package ru.betterend.item.model;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class ArmoredElytraModel<T extends LivingEntity> extends AgeableListModel<T> {
	private final ModelPart rightWing;
	private final ModelPart leftWing;

	public ArmoredElytraModel() {
		this.leftWing = new ModelPart(this, 22, 0);
		this.leftWing.addBox(-10.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, 1.0F);
		this.rightWing = new ModelPart(this, 22, 0);
		this.rightWing.mirror = true;
		this.rightWing.addBox(0.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, 1.0F);
	}

	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(leftWing, rightWing);
	}

	public void setupAnim(T livingEntity, float f, float g, float h, float i, float j) {
		float rotX = 0.2617994F;
		float rotZ = -0.2617994F;
		float rotY = 0.0F;
		float wingY = 0.0F;
		if (livingEntity.isFallFlying()) {
			float coef = 1.0F;
			Vec3 vec3 = livingEntity.getDeltaMovement();
			if (vec3.y < 0.0D) {
				Vec3 normalized = vec3.normalize();
				coef = 1.0F - (float) Math.pow(-normalized.y, 2.5D);
			}
			rotX = coef * 0.34906584F + (1.0F - coef) * rotX;
			rotZ = coef * -1.5707964F + (1.0F - coef) * rotZ;
		} else if (livingEntity.isCrouching()) {
			rotX = 0.6981317F;
			rotZ = -0.7853982F;
			rotY = 0.08726646F;
			wingY = 3.0F;
		}

		leftWing.x = 5.0F;
		leftWing.y = wingY;
		if (livingEntity instanceof AbstractClientPlayer) {
			AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) livingEntity;
			abstractClientPlayer.elytraRotX = (float) ((double) abstractClientPlayer.elytraRotX + (double) (rotX - abstractClientPlayer.elytraRotX) * 0.1D);
			abstractClientPlayer.elytraRotY = (float) ((double) abstractClientPlayer.elytraRotY + (double) (rotY - abstractClientPlayer.elytraRotY) * 0.1D);
			abstractClientPlayer.elytraRotZ = (float) ((double) abstractClientPlayer.elytraRotZ + (double) (rotZ - abstractClientPlayer.elytraRotZ) * 0.1D);
			leftWing.xRot = abstractClientPlayer.elytraRotX;
			leftWing.yRot = abstractClientPlayer.elytraRotY;
			leftWing.zRot = abstractClientPlayer.elytraRotZ;
		} else {
			leftWing.xRot = rotX;
			leftWing.zRot = rotZ;
			leftWing.yRot = rotY;
		}

		rightWing.x = -leftWing.x;
		rightWing.yRot = -leftWing.yRot;
		rightWing.y = leftWing.y;
		rightWing.xRot = leftWing.xRot;
		rightWing.zRot = -leftWing.zRot;
	}
}
