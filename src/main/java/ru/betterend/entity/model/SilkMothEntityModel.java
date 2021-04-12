package ru.betterend.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
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

	public SilkMothEntityModel() {
		super(RenderType::entityCutout);

		texWidth = 64;
		texHeight = 64;

		legsL = new ModelPart(this);
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
		abdomen_r1.texOffs(0, 10).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 7.0F, 0.0F);
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
