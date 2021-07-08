package ru.betterend.item.model;

import com.google.common.collect.Lists;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import ru.betterend.registry.EndEntitiesRenders;

import java.util.Collections;

public class CrystaliteBootsModel extends HumanoidModel<LivingEntity> {

	public ModelPart leftBoot;
	public ModelPart rightBoot;

	public static LayerDefinition getTexturedModelData() {
		final float scale = 1.0f;
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

		// TODO: see if we need to subclass HumanoidModel
		// Humanoid model tries to retrieve all parts in it's constructor,
		// so we need to add empty Nodes
		modelPartData.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);

		CubeDeformation deformation = new CubeDeformation(scale + 0.25f);
		modelPartData.addOrReplaceChild("leftBoot", CubeListBuilder.create()
						.texOffs(0, 32)
						.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation),
				PartPose.offset(1.9f, 12.0f, 0.0f));

		modelPartData.addOrReplaceChild("rightBoot", CubeListBuilder.create()
						.texOffs(0, 16)
						.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation),
				PartPose.offset(-1.9f, 12.0f, 0.0f));

		return LayerDefinition.create(modelData, 64, 48);
	}

	public static CrystaliteBootsModel createModel(EntityModelSet entityModelSet) {
		return new CrystaliteBootsModel(entityModelSet == null ? getTexturedModelData().bakeRoot() : entityModelSet.bakeLayer(EndEntitiesRenders.CRYSTALITE_BOOTS));
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
