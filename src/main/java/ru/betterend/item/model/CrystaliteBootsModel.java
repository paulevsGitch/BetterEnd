package ru.betterend.item.model;

import java.util.Collections;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import com.google.common.collect.Lists;

public class CrystaliteBootsModel extends HumanoidModel<LivingEntity> {

	public ModelPart leftBoot;
	public ModelPart rightBoot;
	
	public CrystaliteBootsModel(float scale) {
		super(RenderType::entityTranslucent, scale, 0.0F, 64, 48);
		this.leftBoot = new ModelPart(this, 0, 32);
		this.leftBoot.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.25F);
		this.leftBoot.setPos(1.9F, 12.0F, 0.0F);
		this.rightBoot = new ModelPart(this, 0, 16);
		this.rightBoot.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale + 0.25F);
		this.rightBoot.setPos(-1.9F, 12.0F, 0.0F);
	}
	
	@Override
	public void copyPropertiesTo(HumanoidModel<LivingEntity> bipedEntityModel) {
		super.copyPropertiesTo(bipedEntityModel);
		this.leftBoot.copyFrom(leftLeg);
		this.rightBoot.copyFrom(rightLeg);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return Collections::emptyIterator;
	}
	
	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Lists.newArrayList(leftBoot, rightBoot);
	}
}
