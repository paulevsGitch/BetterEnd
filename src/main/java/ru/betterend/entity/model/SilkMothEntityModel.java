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
import net.minecraft.util.Mth;
import ru.betterend.entity.SilkMothEntity;

public class SilkMothEntityModel extends BlockBenchModel<SilkMothEntity> {
	private final ModelPart legsL;
	private final ModelPart cube_r1;
	private final ModelPart cube_r2;
	private final ModelPart cube_r3;
	private final ModelPart legsR;
	private final ModelPart cube_r4;
	private final ModelPart cube_r5;
	private final ModelPart cube_r6;
	private final ModelPart head_pivot;
	private final ModelPart tendril_r_r1;
	private final ModelPart tendril_r_r2;
	private final ModelPart bb_main;
	private final ModelPart wingR_r1;
	private final ModelPart wingL_r1;
	private final ModelPart abdomen_r1;

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();

        PartDefinition legsL = modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create()
                        .texOffs(0, 0),
                PartPose.offsetAndRotation(1.5f, 19.9f, -0.45f,
                        0.0f, 0.0f, 0.6981f));

        legsL.addOrReplaceChild("cube_r1", CubeListBuilder.create()
                        .addBox(0.0216f, 0.0f, -0.5976f, 3.0f, 0.0f, 1.0f)
                        .texOffs(0, 13),
                PartPose.offsetAndRotation(0.0f, 0.0f, -1.0f,
                        0.0f, 0.2182f, 0.3927f));

        legsL.addOrReplaceChild("cube_r2", CubeListBuilder.create()
                        .addBox(0.0f, 0.0f, -0.6f, 3.0f, 0.0f, 1.0f)
                        .texOffs(0, 15),
                PartPose.offsetAndRotation(0.5f, 0.1f, -0.05f,
                        0.0f, 0.0f, 0.3927f));

        legsL.addOrReplaceChild("cube_r3", CubeListBuilder.create()
                        .addBox(0.0f, 0.0f, -0.5f, 3.0f, 0.0f, 1.0f)
                        .texOffs(0, 14),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.9f,
                        0.0f, -0.2182f, 0.3927f));

        PartDefinition legsR = modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create()
                        .texOffs(0, 0),
                PartPose.offsetAndRotation(-1.5f, 19.9f, -0.55f,
                        0.0f, 3.1416f, -0.6545f));

        legsR.addOrReplaceChild("cube_r4", CubeListBuilder.create()
                        .addBox(0.0f, 0.0f, -0.5f, 3.0f, 0.0f, 1.0f)
                        .texOffs(0, 10),
                PartPose.offsetAndRotation(0.0f, 0.0f, -1.0f,
                        0.0f, 0.2182f, 0.3927f));

        legsR.addOrReplaceChild("cube_r5", CubeListBuilder.create()
                        .addBox(0.0f, 0.0f, -0.4f, 3.0f, 0.0f, 1.0f)
                        .texOffs(0, 11),
                PartPose.offsetAndRotation(0.5f, 0.1f, -0.05f,
                        0.0f, 0.0f, 0.3927f));

        legsR.addOrReplaceChild("cube_r6", CubeListBuilder.create()
                        .addBox(0.0216f, 0.0f, -0.4024f, 3.0f, 0.0f, 1.0f)
                        .texOffs(0, 12),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.9f,
                        0.0f, -0.2182f, 0.3927f));

        PartDefinition head_pivot = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create()
                        .addBox(-1.5f, -1.5f, -2.0f, 3.0f, 3.0f, 3.0f)
                        .texOffs(15, 10),
                PartPose.offset(0.0f, 18.0f, -3.0f));

        head_pivot.addOrReplaceChild("tendril_r_r1", CubeListBuilder.create()
                        .mirror()
                        .addBox(-1.5f, -5.0f, 0.0f, 3.0f, 5.0f, 0.0f)
                        .texOffs(23, 0),
                PartPose.offsetAndRotation(1.0f, -1.15f, -1.0f,
                        0.0f, 0.0f, 0.3927f));

        head_pivot.addOrReplaceChild("tendril_r_r2", CubeListBuilder.create()
                        .addBox(-1.5f, -5.0f, 0.0f, 3.0f, 5.0f, 0.0f)
                        .texOffs(23, 0),
                PartPose.offsetAndRotation(-1.0f, -1.15f, -1.0f,
                        0.0f, 0.0f, -0.3927f));

        PartDefinition bb_main = modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create()
                        .addBox(-2.5f, -8.5f, -3.0f, 5.0f, 5.0f, 3.0f)
                        .texOffs(19, 19),
                PartPose.offset(0.0f, 24.0f, 0.0f));

        bb_main.addOrReplaceChild(PartNames.RIGHT_WING, CubeListBuilder.create()
                        .mirror()
                        .addBox(-7.0f, 0.0f, -3.0f, 9.0f, 0.0f, 5.0f)
                        .texOffs(0, 5),
                PartPose.offsetAndRotation(-1.5f, -6.5f, 0.5f,
                        0.0f, 0.0f, 0.3927f));

        bb_main.addOrReplaceChild(PartNames.LEFT_WING, CubeListBuilder.create()
                        .addBox(-2.0f, 0.0f, -3.0f, 9.0f, 0.0f, 5.0f)
                        .texOffs(0, 5),
                PartPose.offsetAndRotation(1.5f, -6.5f, 0.5f,
                        0.0f, 0.0f, -0.3927f));

        bb_main.addOrReplaceChild("abdomen_r1", CubeListBuilder.create()
                        .addBox(-3.0f, -4.0f, -1.0f, 4.0f, 4.0f, 7.0f)
                        .texOffs(0, 10),
                PartPose.offsetAndRotation(1.0f, -3.9f, 0.0f,
                        -0.3927f, 0.0f, 0.0f));

		/*texWidth = 64;
		texHeight = 64;*/
		return LayerDefinition.create(modelData, 64, 64);
	}

	public SilkMothEntityModel(ModelPart modelPart) {
		super(RenderType::entityCutout);

        legsL = modelPart.getChild(PartNames.LEFT_LEG);
        cube_r1 = legsL.getChild("cube_r1");
        cube_r2 = legsL.getChild("cube_r2");
        cube_r3 = legsL.getChild("cube_r3");
        legsR = modelPart.getChild(PartNames.RIGHT_LEG);
        cube_r4 = legsR.getChild("cube_r4");
        cube_r5 = legsR.getChild("cube_r5");
        cube_r6 = legsR.getChild("cube_r6");
        head_pivot = modelPart.getChild(PartNames.HEAD);
        tendril_r_r1 = head_pivot.getChild("tendril_r_r1");
        tendril_r_r2 = head_pivot.getChild("tendril_r_r2");
        bb_main = modelPart.getChild(PartNames.BODY);
        wingR_r1 = bb_main.getChild(PartNames.RIGHT_WING);
        wingL_r1 = bb_main.getChild(PartNames.LEFT_WING);
        abdomen_r1 = bb_main.getChild("abdomen_r1");

		/*legsL = new ModelPart(this);
		legsL.setPos(1.5F, 19.9F, -0.45F);
		setRotationAngle(legsL, 0.0F, 0.0F, 0.6981F);

		cube_r1 = new ModelPart(this);
		cube_r1.setPos(0.0F, 0.0F, -1.0F);
		legsL.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.2182F, 0.3927F);
		cube_r1.texOffs(0, 13).addBox(0.0216F, 0.0F, -0.5976F, 3.0F, 0.0F, 1.0F, 0.0F);

		cube_r2 = new ModelPart(this);
		cube_r2.setPos(0.5F, 0.1F, -0.05F);
		legsL.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.3927F);
		cube_r2.texOffs(0, 15).addBox(0.0F, 0.0F, -0.6F, 3.0F, 0.0F, 1.0F, 0.0F);

		cube_r3 = new ModelPart(this);
		cube_r3.setPos(0.0F, 0.0F, 0.9F);
		legsL.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, -0.2182F, 0.3927F);
		cube_r3.texOffs(0, 14).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F);

		legsR = new ModelPart(this);
		legsR.setPos(-1.5F, 19.9F, -0.55F);
		setRotationAngle(legsR, 0.0F, 3.1416F, -0.6545F);

		cube_r4 = new ModelPart(this);
		cube_r4.setPos(0.0F, 0.0F, -1.0F);
		legsR.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.2182F, 0.3927F);
		cube_r4.texOffs(0, 10).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F);

		cube_r5 = new ModelPart(this);
		cube_r5.setPos(0.5F, 0.1F, -0.05F);
		legsR.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 0.3927F);
		cube_r5.texOffs(0, 11).addBox(0.0F, 0.0F, -0.4F, 3.0F, 0.0F, 1.0F, 0.0F);

		cube_r6 = new ModelPart(this);
		cube_r6.setPos(0.0F, 0.0F, 0.9F);
		legsR.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, -0.2182F, 0.3927F);
		cube_r6.texOffs(0, 12).addBox(0.0216F, 0.0F, -0.4024F, 3.0F, 0.0F, 1.0F, 0.0F);

		head_pivot = new ModelPart(this);
		head_pivot.setPos(0.0F, 18.0F, -3.0F);
		head_pivot.texOffs(15, 10).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 3.0F, 0.0F);

		tendril_r_r1 = new ModelPart(this);
		tendril_r_r1.setPos(1.0F, -1.15F, -1.0F);
		head_pivot.addChild(tendril_r_r1);
		setRotationAngle(tendril_r_r1, 0.0F, 0.0F, 0.3927F);
		tendril_r_r1.texOffs(23, 0).addBox(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F, true);

		tendril_r_r2 = new ModelPart(this);
		tendril_r_r2.setPos(-1.0F, -1.15F, -1.0F);
		head_pivot.addChild(tendril_r_r2);
		setRotationAngle(tendril_r_r2, 0.0F, 0.0F, -0.3927F);
		tendril_r_r2.texOffs(23, 0).addBox(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F);

		bb_main = new ModelPart(this);
		bb_main.setPos(0.0F, 24.0F, 0.0F);
		bb_main.texOffs(19, 19).addBox(-2.5F, -8.5F, -3.0F, 5.0F, 5.0F, 3.0F, 0.0F);

		wingR_r1 = new ModelPart(this);
		wingR_r1.setPos(-1.5F, -6.5F, 0.5F);
		bb_main.addChild(wingR_r1);
		setRotationAngle(wingR_r1, 0.0F, 0.0F, 0.3927F);
		wingR_r1.texOffs(0, 5).addBox(-7.0F, 0.0F, -3.0F, 9.0F, 0.0F, 5.0F, 0.0F, true);

		wingL_r1 = new ModelPart(this);
		wingL_r1.setPos(1.5F, -6.5F, 0.5F);
		bb_main.addChild(wingL_r1);
		setRotationAngle(wingL_r1, 0.0F, 0.0F, -0.3927F);
		wingL_r1.texOffs(0, 5).addBox(-2.0F, 0.0F, -3.0F, 9.0F, 0.0F, 5.0F, 0.0F);

		abdomen_r1 = new ModelPart(this);
		abdomen_r1.setPos(1.0F, -3.9F, 0.0F);
		bb_main.addChild(abdomen_r1);
		setRotationAngle(abdomen_r1, -0.3927F, 0.0F, 0.0F);
		abdomen_r1.texOffs(0, 10).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 7.0F, 0.0F);*/
	}

	@Override
	public void setupAnim(SilkMothEntity entity, float limbAngle, float limbDistance, float animationProgress,
			float headYaw, float headPitch) {
		wingR_r1.zRot = Mth.sin(animationProgress * 2F) * 0.4F + 0.3927F;
		wingL_r1.zRot = -wingR_r1.zRot;
		head_pivot.xRot = Mth.sin(animationProgress * 0.03F) * 0.1F;
		tendril_r_r1.zRot = Mth.sin(animationProgress * 0.07F) * 0.2F + 0.3927F;
		tendril_r_r2.zRot = -tendril_r_r1.zRot;
		abdomen_r1.xRot = Mth.sin(animationProgress * 0.05F) * 0.1F - 0.3927F;
		legsR.zRot = Mth.sin(animationProgress * 0.07F) * 0.1F - 0.6545F;
		legsL.zRot = -legsR.zRot;
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red,
			float green, float blue, float alpha) {
		bb_main.render(matrices, vertices, light, overlay);
		head_pivot.render(matrices, vertices, light, overlay);
		legsL.render(matrices, vertices, light, overlay);
		legsR.render(matrices, vertices, light, overlay);
	}
}
