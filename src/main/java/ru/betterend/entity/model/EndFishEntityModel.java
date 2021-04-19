package ru.betterend.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import ru.betterend.entity.EndFishEntity;

public class EndFishEntityModel extends BlockBenchModel<EndFishEntity> {
	private final ModelPart model;
	private final ModelPart fin_top;
	private final ModelPart fin_bottom;
	private final ModelPart flipper;
	private final ModelPart fin_right;
	private final ModelPart fin_left;

	public EndFishEntityModel() {
		super(RenderType::entityCutout);

		texWidth = 32;
		texHeight = 32;

		model = new ModelPart(this);
		model.setPos(0.0F, 20.0F, 0.0F);
		model.texOffs(0, 0).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 4.0F, 8.0F, 0.0F);

		fin_top = new ModelPart(this);
		fin_top.setPos(0.0F, -2.0F, -4.0F);
		model.addChild(fin_top);
		setRotationAngle(fin_top, -0.6981F, 0.0F, 0.0F);
		fin_top.texOffs(0, 6).addBox(0.0F, -8.0F, 0.0F, 0.0F, 8.0F, 6.0F, 0.0F);

		fin_bottom = new ModelPart(this);
		fin_bottom.setPos(0.0F, 2.0F, -4.0F);
		model.addChild(fin_bottom);
		setRotationAngle(fin_bottom, 0.6981F, 0.0F, 0.0F);
		fin_bottom.texOffs(0, 6).addBox(0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 6.0F, 0.0F);

		flipper = new ModelPart(this);
		flipper.setPos(0.0F, 0.0F, 2.0F);
		model.addChild(flipper);
		setRotationAngle(flipper, -0.7854F, 0.0F, 0.0F);
		flipper.texOffs(0, 15).addBox(0.0F, -5.0F, 0.0F, 0.0F, 5.0F, 5.0F, 0.0F);

		fin_right = new ModelPart(this);
		fin_right.setPos(-1.0F, 0.0F, -1.0F);
		model.addChild(fin_right);
		setRotationAngle(fin_right, 1.5708F, 0.7854F, 0.0F);
		fin_right.texOffs(0, 25).addBox(-3.7071F, 0.7071F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F);

		fin_left = new ModelPart(this);
		fin_left.setPos(1.0F, 0.0F, -1.0F);
		model.addChild(fin_left);
		setRotationAngle(fin_left, 1.5708F, -0.7854F, 0.0F);
		fin_left.texOffs(0, 25).addBox(0.7071F, 0.7071F, -1.5F, 3.0F, 0.0F, 3.0F, 0.0F, true);
	}

	@Override
	public void setupAnim(EndFishEntity entity, float limbAngle, float limbDistance, float animationProgress,
			float headYaw, float headPitch) {
		float s1 = (float) Math.sin(animationProgress * 0.1);
		float s2 = (float) Math.sin(animationProgress * 0.05);
		flipper.yRot = s1 * 0.3F;
		fin_top.xRot = s2 * 0.02F - 0.6981F;
		fin_bottom.xRot = 0.6981F - s2 * 0.02F;
		fin_left.yRot = s1 * 0.3F - 0.7854F;
		fin_right.yRot = 0.7854F - s1 * 0.3F;
	}

	@Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red,
			float green, float blue, float alpha) {
		model.render(matrices, vertices, light, overlay);
	}
}
