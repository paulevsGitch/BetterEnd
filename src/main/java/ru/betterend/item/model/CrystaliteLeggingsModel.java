package ru.betterend.item.model;

import com.google.common.collect.Lists;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
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

public class CrystaliteLeggingsModel extends HumanoidModel<LivingEntity> {
	public static LayerDefinition getTexturedModelData() {
		float scale = 1.0f;
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		// TODO: see if we need to subclass HumanoidModel
		// Humanoid model tries to retrieve all parts in it's constructor,
		// so we need to add empty Nodes
		modelPartData.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		// modelPartData.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		// modelPartData.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
		// modelPartData.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
		
		CubeDeformation deformation = new CubeDeformation(scale);
		modelPartData.addOrReplaceChild(
			PartNames.BODY,
			CubeListBuilder.create().texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, deformation),
			PartPose.ZERO
		);
		
		modelPartData.addOrReplaceChild(
			PartNames.LEFT_LEG,
			CubeListBuilder.create().texOffs(0, 32).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation),
			PartPose.offset(1.9f, 12.0f, 0.0f)
		);
		
		modelPartData.addOrReplaceChild(
			PartNames.RIGHT_LEG,
			CubeListBuilder.create().texOffs(0, 16).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation),
			PartPose.offset(-1.9f, 12.0f, 0.0f)
		);
		
		return LayerDefinition.create(modelData, 64, 48);
	}
	
	final ModelPart myBody;
	final ModelPart myLeftLeg;
	final ModelPart myRightLeg;
	
	public static CrystaliteLeggingsModel createModel(EntityModelSet entityModelSet) {
		return new CrystaliteLeggingsModel(entityModelSet == null ? getTexturedModelData().bakeRoot() : entityModelSet.bakeLayer(
			EndEntitiesRenders.CRYSTALITE_LEGGINGS));
	}
	
	public CrystaliteLeggingsModel(ModelPart modelPart) {
		super(modelPart, RenderType::entityTranslucent);
		
		myBody = modelPart.getChild(PartNames.BODY);
		myLeftLeg = modelPart.getChild(PartNames.LEFT_LEG);
		myRightLeg = modelPart.getChild(PartNames.RIGHT_LEG);
	}
	
	@Override
	protected Iterable<ModelPart> headParts() {
		return Collections::emptyIterator;
	}
	
	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Lists.newArrayList(myBody, myRightLeg, myLeftLeg);
	}
}
