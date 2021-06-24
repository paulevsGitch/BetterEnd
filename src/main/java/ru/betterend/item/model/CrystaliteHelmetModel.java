package ru.betterend.item.model;

import java.util.Collections;

import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class CrystaliteHelmetModel extends HumanoidModel<LivingEntity> {
	final ModelPart myHat;
	public static LayerDefinition getTexturedModelData() {
		final float scale = 1.0f;
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		CubeDeformation deformation_hat = new CubeDeformation(scale + 0.5f);
		PartDefinition hat = modelPartData.addOrReplaceChild(PartNames.HAT, CubeListBuilder.create()
						.addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, deformation_hat)
						.texOffs(0, 0),
				PartPose.ZERO);

		return LayerDefinition.create(modelData, 64, 48);
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
