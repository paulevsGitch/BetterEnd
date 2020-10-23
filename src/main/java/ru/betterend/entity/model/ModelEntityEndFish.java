package ru.betterend.entity.model;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import ru.betterend.entity.BlockBenchModel;
import ru.betterend.entity.EntityEndFish;

public class ModelEntityEndFish extends BlockBenchModel<EntityEndFish> {
	private final ModelPart model;
	private final ModelPart fin_top;
	private final ModelPart fin_bottom;
	private final ModelPart flipper;
	private final ModelPart fin_right;
	private final ModelPart fin_left;

	public ModelEntityEndFish() {
		textureWidth = 32;
		textureHeight = 32;

		model = new ModelPart(this);
		model.setPivot(0.0F, 20.0F, 0.0F);
		model.setTextureOffset(0, 0).addCuboid(-1.0F, -2.0F, -4.0F, 2.0F, 4.0F, 8.0F, 0.0F);

		fin_top = new ModelPart(this);
		fin_top.setPivot(0.0F, -2.0F, -4.0F);
		model.addChild(fin_top);
		setRotationAngle(fin_top, -0.6981F, 0.0F, 0.0F);
		fin_top.setTextureOffset(0, 6).addCuboid(0.0F, -8.0F, 0.0F, 0.0F, 8.0F, 6.0F, 0.0F);

		fin_bottom = new ModelPart(this);
		fin_bottom.setPivot(0.0F, 2.0F, -4.0F);
		model.addChild(fin_bottom);
		setRotationAngle(fin_bottom, 0.6981F, 0.0F, 0.0F);
		fin_bottom.setTextureOffset(0, 6).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 6.0F, 0.0F);

		flipper = new ModelPart(this);
		flipper.setPivot(0.0F, 0.0F, 2.0F);
		model.addChild(flipper);
		setRotationAngle(flipper, -0.7854F, 0.0F, 0.0F);
		flipper.setTextureOffset(0, 15).addCuboid(0.0F, -5.0F, 0.0F, 0.0F, 5.0F, 5.0F, 0.0F);

		fin_right = new ModelPart(this);
		fin_right.setPivot(-1.0F, 0.0F, -1.0F);
		model.addChild(fin_right);
		setRotationAngle(fin_right, 1.5708F, 0.7854F, 0.0F);
		fin_right.setTextureOffset(0, 25).addCuboid(-3.7071F, 0.7071F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F);

		fin_left = new ModelPart(this);
		fin_left.setPivot(1.0F, 0.0F, -1.0F);
		model.addChild(fin_left);
		setRotationAngle(fin_left, 1.5708F, -0.7854F, 0.0F);
		fin_left.setTextureOffset(0, 25).addCuboid(0.7071F, 0.7071F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, true);
	}


	@Override
	public void setAngles(EntityEndFish entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float s1 = (float) Math.sin(animationProgress * 0.1);
		float s2 = (float) Math.sin(animationProgress * 0.05);
		flipper.yaw = s1 * 0.3F;
		fin_top.pitch = s2 * 0.02F - 0.6981F;
		fin_bottom.pitch = 0.6981F - s2 * 0.02F;
		fin_left.yaw = s1 * 0.3F - 0.7854F;
		fin_right.yaw = 0.7854F - s1 * 0.3F;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		RenderSystem.enableCull();
		model.render(matrices, vertices, light, overlay);
		RenderSystem.disableCull();
	}
}
