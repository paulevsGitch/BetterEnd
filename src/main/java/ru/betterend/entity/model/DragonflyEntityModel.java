package ru.betterend.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import ru.betterend.entity.DragonflyEntity;

public class DragonflyEntityModel extends BlockBenchModel<DragonflyEntity> {
	private final ModelPart model;
	private final ModelPart head;
	private final ModelPart tail;
	private final ModelPart tail_2;
	private final ModelPart wing_1;
	private final ModelPart wing_2;
	private final ModelPart wing_3;
	private final ModelPart wing_4;
	private final ModelPart legs_1;
	private final ModelPart legs_2;
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		
		PartDefinition bodyPart = modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, 0.0F, 4.0F, 4.0F, 9.0F), PartPose.offset(2.0F, 21.5F, -4.0F));
		
		bodyPart.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create().texOffs(17, 0).addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 3.0F), PartPose.offsetAndRotation(-2.0F, -2.0F, 0.0F, 0.3491F, 0.0F, 0.0F));
		
		PartDefinition tailPart = bodyPart.addOrReplaceChild(PartNames.TAIL, CubeListBuilder.create().texOffs(26, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 7.0F), PartPose.offset(-2.0F, -2.0F, 9.0F));
		
		tailPart.addOrReplaceChild(PartNames.TAIL_FIN, CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 10.0F), PartPose.offset(0.0F, 0.0F, 7.0F));
		
		bodyPart.addOrReplaceChild(PartNames.LEFT_WING, CubeListBuilder.create().texOffs(0, 13).addBox(-15.0F, 0.0F, -3.0F, 15.0F, 0.0F, 4.0F), PartPose.offset(-2.0F, -4.0F, 4.0F));
		
		bodyPart.addOrReplaceChild(PartNames.RIGHT_WING, CubeListBuilder.create().mirror().texOffs(0, 13).addBox(0.0F, 0.0F, -3.0F, 15.0F, 0.0F, 4.0F), PartPose.offset(-2.0F, -4.0F, 4.0F));
		
		bodyPart.addOrReplaceChild(PartNames.LEFT_WING_BASE, CubeListBuilder.create().texOffs(4, 17).addBox(-12.0F, 0.0F, -2.5F, 12.0F, 0.0F, 3.0F), PartPose.offset(-2.0F, -4.0F, 8.0F));
		
		bodyPart.addOrReplaceChild(PartNames.RIGHT_WING_BASE, CubeListBuilder.create().mirror().texOffs(4, 17).addBox(0.0F, 0.0F, -2.5F, 12.0F, 0.0F, 3.0F), PartPose.offset(-2.0F, -4.0F, 8.0F));
		
		bodyPart.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create().texOffs(50, 1).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 6.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 1.0F, 0.0F, 0.0F, -0.5236F));
		
		bodyPart.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create().texOffs(50, 1).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 6.0F), PartPose.offsetAndRotation(-3.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.5236F));
		
		return LayerDefinition.create(modelData, 64, 64);
	}
	
	public DragonflyEntityModel(ModelPart modelPart) {
		super(RenderType::entityCutout);
		
		model = modelPart.getChild(PartNames.BODY);
		head = model.getChild(PartNames.HEAD);
		tail = model.getChild(PartNames.TAIL);
		tail_2 = tail.getChild(PartNames.TAIL_FIN);
		wing_1 = model.getChild(PartNames.LEFT_WING);
		wing_2 = model.getChild(PartNames.RIGHT_WING);
		wing_3 = model.getChild(PartNames.LEFT_WING_BASE);
		wing_4 = model.getChild(PartNames.RIGHT_WING_BASE);
		legs_1 = model.getChild(PartNames.LEFT_LEG);
		legs_2 = model.getChild(PartNames.RIGHT_LEG);
	}
	
	@Override
	public void setupAnim(DragonflyEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float progress = animationProgress * 2F;
		
		wing_1.zRot = 0.3491F + (float) Math.sin(progress) * 0.3491F;
		wing_2.zRot = -wing_1.zRot;
		
		wing_3.zRot = 0.3491F + (float) Math.cos(progress) * 0.3491F;
		wing_4.zRot = -wing_3.zRot;
		
		progress = animationProgress * 0.05F;
		
		head.xRot = 0.3491F + (float) Math.sin(progress * 0.7F) * 0.1F;
		tail.xRot = (float) Math.cos(progress) * 0.05F - 0.05F;
		tail_2.xRot = -tail.xRot * 1.5F;
	}
	
	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		model.render(matrices, vertices, light, overlay);
	}
}
