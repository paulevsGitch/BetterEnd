package ru.betterend.item.model;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import ru.betterend.registry.EndEntitiesRenders;

import java.util.Collections;

public class CrystaliteChestplateModel extends HumanoidModel<LivingEntity> {
	
	public ModelPart leftShoulder;
	public ModelPart rightShoulder;
	private final boolean thinArms;
	
	public static LayerDefinition getRegularTexturedModelData() {
		return getTexturedModelData(1.0f, false);
	}
	
	public static LayerDefinition getThinTexturedModelData() {
		return getTexturedModelData(1.0f, true);
	}
	
	private static LayerDefinition getTexturedModelData(float scale, boolean thinArms) {
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
		modelPartData.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
		
		CubeDeformation deformation = new CubeDeformation(scale + 0.25F);
		PartDefinition body = modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create().texOffs(16, 16).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, deformation), PartPose.ZERO);
		
		if (thinArms) {
			deformation = new CubeDeformation(scale + 0.45F);
			PartDefinition leftShoulder = modelPartData.addOrReplaceChild("leftShoulder", CubeListBuilder.create().mirror().texOffs(40, 32).addBox(-1.0f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, deformation), PartPose.offset(5.0f, 2.0f, 0.0f));
			
			PartDefinition rightShoulder = modelPartData.addOrReplaceChild("rightShoulder", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, deformation), PartPose.offset(-5.0f, 2.0f, 10.0f));
		}
		else {
			deformation = new CubeDeformation(scale + 0.45F);
			PartDefinition leftShoulder = modelPartData.addOrReplaceChild("leftShoulder", CubeListBuilder.create().mirror().texOffs(40, 32).addBox(-1.0f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, deformation), PartPose.offset(5.0f, 2.0f, 0.0f));
			
			PartDefinition rightShoulder = modelPartData.addOrReplaceChild("rightShoulder", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0f, -2.5f, -2.0f, 4.0f, 12.0f, 4.0f, deformation), PartPose.offset(-5.0f, 2.0f, 10.0f));
		}
		return LayerDefinition.create(modelData, 64, 48);
	}
	
	final ModelPart localBody;
	
	public static CrystaliteChestplateModel createRegularModel(EntityModelSet entityModelSet) {
		return new CrystaliteChestplateModel(entityModelSet == null ? getRegularTexturedModelData().bakeRoot() : entityModelSet.bakeLayer(EndEntitiesRenders.CRYSTALITE_CHESTPLATE), false);
	}
	
	public static CrystaliteChestplateModel createThinModel(EntityModelSet entityModelSet) {
		return new CrystaliteChestplateModel(entityModelSet == null ? getThinTexturedModelData().bakeRoot() : entityModelSet.bakeLayer(EndEntitiesRenders.CRYSTALITE_CHESTPLATE_THIN), true);
	}
	
	protected CrystaliteChestplateModel(ModelPart modelPart, boolean thinArms) {
		super(modelPart, RenderType::entityTranslucent);
		this.thinArms = thinArms;
		localBody = modelPart.getChild(PartNames.BODY);
		leftShoulder = modelPart.getChild("leftShoulder");
		rightShoulder = modelPart.getChild("rightShoulder");
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
		return Lists.newArrayList(localBody, leftShoulder, rightShoulder);
	}
	
	@Override
	public void translateToHand(HumanoidArm arm, PoseStack matrices) {
		ModelPart modelPart = this.getArm(arm);
		if (this.thinArms) {
			float f = 0.5F * (float) (arm == HumanoidArm.RIGHT ? 1 : -1);
			modelPart.x += f;
			modelPart.translateAndRotate(matrices);
			modelPart.x -= f;
		}
		else {
			modelPart.translateAndRotate(matrices);
		}
	}
}
