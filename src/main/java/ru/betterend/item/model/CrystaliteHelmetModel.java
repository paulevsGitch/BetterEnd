package ru.betterend.item.model;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

@Environment(EnvType.CLIENT)
public class CrystaliteHelmetModel extends HumanoidModel<LivingEntity> {
	final ModelPart myHat;
	
	public static LayerDefinition getTexturedModelData() {
		final float scale = 1.0f;
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		// TODO: see if we need to subclass HumanoidModel
		// Humanoid model tries to retrieve all parts in it's constructor,
		// so we need to add empty Nodes
		modelPartData.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		//modelPartData.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
		modelPartData.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
		
		CubeDeformation deformation_hat = new CubeDeformation(scale + 0.5f);
		PartDefinition hat = modelPartData.addOrReplaceChild(PartNames.HAT, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, deformation_hat), PartPose.ZERO);
		
		return LayerDefinition.create(modelData, 64, 48);
	}
	
	public static CrystaliteHelmetModel createModel(EntityModelSet entityModelSet) {
		return new CrystaliteHelmetModel(entityModelSet == null ? getTexturedModelData().bakeRoot() : entityModelSet.bakeLayer(EndEntitiesRenders.CRYSTALITE_HELMET));
	}
	
	
	public CrystaliteHelmetModel(ModelPart modelPart) {
		super(modelPart, RenderType::entityTranslucent);
		
		myHat = modelPart.getChild(PartNames.HAT);
	}
	
	@Override
	protected Iterable<ModelPart> headParts() {
		return Collections::emptyIterator;
	}
	
	@Override
	protected Iterable<ModelPart> bodyParts() {
		return Lists.newArrayList(myHat);
	}
}
