package ru.betterend.item.model;

import java.util.Collections;

import com.google.common.collect.Lists;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import ru.betterend.registry.EndEntitiesRenders;

public class CrystaliteBootsModel extends HumanoidModel<LivingEntity> {

	public ModelPart leftBoot;
	public ModelPart rightBoot;

	public static LayerDefinition getTexturedModelData() {
		final float scale = 1.0f;
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		CubeDeformation deformation = new CubeDeformation(scale + 0.25f);

		modelPartData.addOrReplaceChild("leftBoot", CubeListBuilder.create()
						.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation)
						.texOffs(0, 32),
				PartPose.offset(1.9f, 12.0f, 0.0f));

		modelPartData.addOrReplaceChild("rightBoot", CubeListBuilder.create()
						.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation)
						.texOffs(0, 16),
				PartPose.offset(-1.9f, 12.0f, 0.0f));

		return LayerDefinition.create(modelData, 64, 48);
	}

	public static CrystaliteBootsModel createModel(EntityModelSet entityModelSet){
		return new CrystaliteBootsModel(entityModelSet==null?getTexturedModelData().bakeRoot():entityModelSet.bakeLayer(EndEntitiesRenders.CRYSTALITE_BOOTS));
	}
	
	public CrystaliteBootsModel(ModelPart modelPart) {
		super(modelPart, RenderType::entityTranslucent);

		leftBoot = modelPart.getChild("leftBoot");
		rightBoot = modelPart.getChild("rightBoot");
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
