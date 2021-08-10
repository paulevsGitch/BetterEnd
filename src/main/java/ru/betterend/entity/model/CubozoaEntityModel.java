package ru.betterend.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import ru.betterend.entity.CubozoaEntity;

public class CubozoaEntityModel extends EntityModel<CubozoaEntity> {
	private final static int TENTACLE_COUNT = 4;
	
	private final ModelPart model;
	private final ModelPart[] tentacle_center;
	private final ModelPart[] tentacle;
	private float scaleY;
	private float scaleXZ;
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition bodyPart = modelPartData.addOrReplaceChild(
			PartNames.BODY,
			CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -12.5F, -2.0F, 4.0F, 4.0F, 4.0F),
			PartPose.offset(0.0F, 24.0F, 0.0F)
		);
		
		bodyPart.addOrReplaceChild(
			"main_cube_r1",
			CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -7.0F, -5.0F, 10.0F, 7.0F, 10.0F),
			PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, 0.0F, 0.0F, -3.1416F)
		);
		
		for (int i = 1; i <= TENTACLE_COUNT; i++) {
			PartDefinition tentaclePart = bodyPart.addOrReplaceChild(
				"tentacle_center_" + i,
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, i * 1.5708F, 0.0F)
			);
			
			tentaclePart.addOrReplaceChild(
				"tentacle_" + i,
				CubeListBuilder.create().texOffs(16, 17).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 7.0F, 0.0F),
				PartPose.offset(0.0F, -7.0F, 4.5F)
			);
		}
		
		return LayerDefinition.create(modelData, 48, 48);
	}
	
	public CubozoaEntityModel(ModelPart modelPart) {
		super(RenderType::entityTranslucent);
		tentacle = new ModelPart[TENTACLE_COUNT];
		tentacle_center = new ModelPart[TENTACLE_COUNT];
		
		model = modelPart.getChild(PartNames.BODY);
		for (int i = 1; i <= TENTACLE_COUNT; i++) {
			tentacle_center[i - 1] = model.getChild("tentacle_center_" + i);
			tentacle[i - 1] = tentacle_center[i - 1].getChild("tentacle_" + i);
		}
	}
	
	@Override
	public void setupAnim(CubozoaEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float sin = Mth.sin(animationProgress * 0.13F);
		scaleY = sin * 0.1F + 0.9F;
		scaleXZ = Mth.sin(animationProgress * 0.13F + 3.14F) * 0.1F + 0.9F;
		
		for (int i = 0; i < TENTACLE_COUNT; i++) {
			tentacle[i].xRot = sin * 0.15f;
		}
	}
	
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.pushPose();
		matrices.scale(scaleXZ, scaleY, scaleXZ);
		model.render(matrices, vertices, light, overlay);
		matrices.popPose();
	}
}
