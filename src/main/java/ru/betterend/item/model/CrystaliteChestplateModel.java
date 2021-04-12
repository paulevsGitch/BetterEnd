package ru.betterend.item.model;

import java.util.Collections;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;

public class CrystaliteChestplateModel extends HumanoidModel<LivingEntity> {

	public ModelPart leftShoulder;
	public ModelPart rightShoulder;
	private boolean thinArms;
	
	public CrystaliteChestplateModel(float scale, boolean thinArms) {
		super(RenderType::entityTranslucent, scale, 0.0F, 64, 48);
		this.thinArms = thinArms;
		this.body = new ModelPart(this, 16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, scale + 0.25F);
		this.body.setPos(0.0F, 0.0F, 0.0F);
		if (thinArms) {
			this.leftShoulder = new ModelPart(this, 41, 32);
			this.leftShoulder.addBox(-1.0F, -2.5F, -2.0F, 3.0F, 12.0F, 4.0F, scale + 0.35F);
			this.leftShoulder.setPos(5.0F, 2.5F, 0.0F);
			this.leftShoulder.mirror = true;
			this.rightShoulder = new ModelPart(this, 41, 16);
			this.rightShoulder.addBox(-2.0F, -2.5F, -2.0F, 3.0F, 12.0F, 4.0F, scale + 0.35F);
			this.rightShoulder.setPos(-5.0F, 2.5F, 10.0F);
		} else {
			this.leftShoulder = new ModelPart(this, 40, 32);
			this.leftShoulder.addBox(-1.0F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.45F);
			this.leftShoulder.setPos(5.0F, 2.0F, 0.0F);
			this.leftShoulder.mirror = true;
			this.rightShoulder = new ModelPart(this, 40, 16);
			this.rightShoulder.addBox(-3.0F, -2.5F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.45F);
			this.rightShoulder.setPos(-5.0F, 2.0F, 10.0F);
		}
	}
	
	@Override
	public void copyPropertiesTo(HumanoidModel<LivingEntity> bipedEntityModel) {
		super.copyPropertiesTo(bipedEntityModel);
		this.leftShoulder.copyFrom(leftArm);
		this.rightShoulder.copyFrom(rightArm);
	}
	
	@Override
	protected Iterable<ModelPart> headParts() {
		return Collections::emptyIterator;
	}
	
	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Lists.newArrayList(body, leftShoulder, rightShoulder);
	}
	
	@Override
	public void translateToHand(HumanoidArm arm, PoseStack matrices) {
		ModelPart modelPart = this.getArm(arm);
		if (this.thinArms) {
			float f = 0.5F * (float)(arm == HumanoidArm.RIGHT ? 1 : -1);
			modelPart.x += f;
			modelPart.translateAndRotate(matrices);
			modelPart.x -= f;
		} else {
			modelPart.translateAndRotate(matrices);
		}
	}
}
