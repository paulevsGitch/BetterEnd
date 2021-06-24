package ru.betterend.item.model;

import java.util.Collections;

import com.google.common.collect.Lists;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import ru.betterend.registry.EndEntitiesRenders;

public class CrystaliteLeggingsModel extends HumanoidModel<LivingEntity> {
	public static LayerDefinition getTexturedModelData() {
		float scale = 1.0f;
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		CubeDeformation deformation = new CubeDeformation(scale);
		modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create()
						.addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, deformation)
						.texOffs(16, 16),
				PartPose.ZERO);

		modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create()
						.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation)
						.texOffs(0, 32),
				PartPose.offset(1.9f, 12.0f, 0.0f));

		modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create()
						.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, deformation)
						.texOffs(0, 16),
				PartPose.offset(-1.9f, 12.0f, 0.0f));

		return LayerDefinition.create(modelData, 64, 48);
	}

	final ModelPart myBody;
	final ModelPart myLeftLeg;
	final ModelPart myRightLeg;

	public static CrystaliteLeggingsModel createModel(EntityModelSet entityModelSet){
		return new CrystaliteLeggingsModel(entityModelSet==null?getTexturedModelData().bakeRoot():entityModelSet.bakeLayer(EndEntitiesRenders.CRYSTALITE_LEGGINGS));
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
