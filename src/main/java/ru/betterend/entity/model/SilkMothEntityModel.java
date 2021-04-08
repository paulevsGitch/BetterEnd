package ru.betterend.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
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
		super(RenderLayer::getEntityCutout);

		textureWidth = 64;
		textureHeight = 64;

		legsL = new ModelPart(this);
		legsL.setPivot(1.5F, 19.9F, -0.45F);
		setRotationAngle(legsL, 0.0F, 0.0F, 0.6981F);

		cube_r1 = new ModelPart(this);
		cube_r1.setPivot(0.0F, 0.0F, -1.0F);
		legsL.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.2182F, 0.3927F);
		cube_r1.setTextureOffset(0, 13).addCuboid(0.0216F, 0.0F, -0.5976F, 3.0F, 0.0F, 1.0F, 0.0F);

		cube_r2 = new ModelPart(this);
		cube_r2.setPivot(0.5F, 0.1F, -0.05F);
		legsL.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.3927F);
		cube_r2.setTextureOffset(0, 15).addCuboid(0.0F, 0.0F, -0.6F, 3.0F, 0.0F, 1.0F, 0.0F);

		cube_r3 = new ModelPart(this);
		cube_r3.setPivot(0.0F, 0.0F, 0.9F);
		legsL.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, -0.2182F, 0.3927F);
		cube_r3.setTextureOffset(0, 14).addCuboid(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F);

		legsR = new ModelPart(this);
		legsR.setPivot(-1.5F, 19.9F, -0.55F);
		setRotationAngle(legsR, 0.0F, 3.1416F, -0.6545F);

		cube_r4 = new ModelPart(this);
		cube_r4.setPivot(0.0F, 0.0F, -1.0F);
		legsR.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.2182F, 0.3927F);
		cube_r4.setTextureOffset(0, 10).addCuboid(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F);

		cube_r5 = new ModelPart(this);
		cube_r5.setPivot(0.5F, 0.1F, -0.05F);
		legsR.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 0.3927F);
		cube_r5.setTextureOffset(0, 11).addCuboid(0.0F, 0.0F, -0.4F, 3.0F, 0.0F, 1.0F, 0.0F);

		cube_r6 = new ModelPart(this);
		cube_r6.setPivot(0.0F, 0.0F, 0.9F);
		legsR.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, -0.2182F, 0.3927F);
		cube_r6.setTextureOffset(0, 12).addCuboid(0.0216F, 0.0F, -0.4024F, 3.0F, 0.0F, 1.0F, 0.0F);

		head_pivot = new ModelPart(this);
		head_pivot.setPivot(0.0F, 18.0F, -3.0F);
		head_pivot.setTextureOffset(15, 10).addCuboid(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 3.0F, 0.0F);

		tendril_r_r1 = new ModelPart(this);
		tendril_r_r1.setPivot(1.0F, -1.15F, -1.0F);
		head_pivot.addChild(tendril_r_r1);
		setRotationAngle(tendril_r_r1, 0.0F, 0.0F, 0.3927F);
		tendril_r_r1.setTextureOffset(23, 0).addCuboid(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F, true);

		tendril_r_r2 = new ModelPart(this);
		tendril_r_r2.setPivot(-1.0F, -1.15F, -1.0F);
		head_pivot.addChild(tendril_r_r2);
		setRotationAngle(tendril_r_r2, 0.0F, 0.0F, -0.3927F);
		tendril_r_r2.setTextureOffset(23, 0).addCuboid(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F);

		bb_main = new ModelPart(this);
		bb_main.setPivot(0.0F, 24.0F, 0.0F);
		bb_main.setTextureOffset(19, 19).addCuboid(-2.5F, -8.5F, -3.0F, 5.0F, 5.0F, 3.0F, 0.0F);

		wingR_r1 = new ModelPart(this);
		wingR_r1.setPivot(-1.5F, -6.5F, 0.5F);
		bb_main.addChild(wingR_r1);
		setRotationAngle(wingR_r1, 0.0F, 0.0F, 0.3927F);
		wingR_r1.setTextureOffset(0, 5).addCuboid(-7.0F, 0.0F, -3.0F, 9.0F, 0.0F, 5.0F, 0.0F, true);

		wingL_r1 = new ModelPart(this);
		wingL_r1.setPivot(1.5F, -6.5F, 0.5F);
		bb_main.addChild(wingL_r1);
		setRotationAngle(wingL_r1, 0.0F, 0.0F, -0.3927F);
		wingL_r1.setTextureOffset(0, 5).addCuboid(-2.0F, 0.0F, -3.0F, 9.0F, 0.0F, 5.0F, 0.0F);

		abdomen_r1 = new ModelPart(this);
		abdomen_r1.setPivot(1.0F, -3.9F, 0.0F);
		bb_main.addChild(abdomen_r1);
		setRotationAngle(abdomen_r1, -0.3927F, 0.0F, 0.0F);
		abdomen_r1.setTextureOffset(0, 10).addCuboid(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 7.0F, 0.0F);
	}

	@Override
	public void setAngles(SilkMothEntity entity, float limbAngle, float limbDistance, float animationProgress,
			float headYaw, float headPitch) {
		wingR_r1.roll = Mth.sin(animationProgress * 2F) * 0.4F + 0.3927F;
		wingL_r1.roll = -wingR_r1.roll;
		head_pivot.pitch = Mth.sin(animationProgress * 0.03F) * 0.1F;
		tendril_r_r1.roll = Mth.sin(animationProgress * 0.07F) * 0.2F + 0.3927F;
		tendril_r_r2.roll = -tendril_r_r1.roll;
		abdomen_r1.pitch = Mth.sin(animationProgress * 0.05F) * 0.1F - 0.3927F;
		legsR.roll = Mth.sin(animationProgress * 0.07F) * 0.1F - 0.6545F;
		legsL.roll = -legsR.roll;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green,
			float blue, float alpha) {
		bb_main.render(matrices, vertices, light, overlay);
		head_pivot.render(matrices, vertices, light, overlay);
		legsL.render(matrices, vertices, light, overlay);
		legsR.render(matrices, vertices, light, overlay);
	}
}
