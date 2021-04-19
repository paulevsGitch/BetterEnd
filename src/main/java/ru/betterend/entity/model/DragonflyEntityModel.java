package ru.betterend.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
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

	public DragonflyEntityModel() {
		super(RenderType::entityCutout);

		texWidth = 64;
		texHeight = 64;

		model = new ModelPart(this);
		model.setPos(2.0F, 21.5F, -4.0F);
		model.texOffs(0, 0).addBox(-4.0F, -4.0F, 0.0F, 4.0F, 4.0F, 9.0F, 0.0F);

		head = new ModelPart(this);
		head.setPos(-2.0F, -2.0F, 0.0F);
		model.addChild(head);
		setRotationAngle(head, 0.3491F, 0.0F, 0.0F);
		head.texOffs(17, 0).addBox(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 3.0F, 0.0F);

		tail = new ModelPart(this);
		tail.setPos(-2.0F, -2.0F, 9.0F);
		model.addChild(tail);
		tail.texOffs(26, 0).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 7.0F, 0.0F);

		tail_2 = new ModelPart(this);
		tail_2.setPos(0.0F, 0.0F, 7.0F);
		tail.addChild(tail_2);
		tail_2.texOffs(36, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 10.0F, 0.0F);

		wing_1 = new ModelPart(this);
		wing_1.setPos(-2.0F, -4.0F, 4.0F);
		model.addChild(wing_1);
		wing_1.texOffs(0, 13).addBox(-15.0F, 0.0F, -3.0F, 15.0F, 0.0F, 4.0F, 0.0F);

		wing_2 = new ModelPart(this);
		wing_2.setPos(-2.0F, -4.0F, 4.0F);
		model.addChild(wing_2);
		wing_2.mirror = true;
		wing_2.texOffs(0, 13).addBox(0.0F, 0.0F, -3.0F, 15.0F, 0.0F, 4.0F, 0.0F);

		wing_3 = new ModelPart(this);
		wing_3.setPos(-2.0F, -4.0F, 8.0F);
		model.addChild(wing_3);
		wing_3.texOffs(4, 17).addBox(-12.0F, 0.0F, -2.5F, 12.0F, 0.0F, 3.0F, 0.0F);

		wing_4 = new ModelPart(this);
		wing_4.setPos(-2.0F, -4.0F, 8.0F);
		model.addChild(wing_4);
		wing_4.mirror = true;
		wing_4.texOffs(4, 17).addBox(0.0F, 0.0F, -2.5F, 12.0F, 0.0F, 3.0F, 0.0F);

		legs_1 = new ModelPart(this);
		legs_1.setPos(-1.0F, 0.0F, 1.0F);
		model.addChild(legs_1);
		setRotationAngle(legs_1, 0.0F, 0.0F, -0.5236F);
		legs_1.texOffs(50, 1).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F);

		legs_2 = new ModelPart(this);
		legs_2.setPos(-3.0F, 0.0F, 1.0F);
		model.addChild(legs_2);
		setRotationAngle(legs_2, 0.0F, 0.0F, 0.5236F);
		legs_2.texOffs(50, 1).addBox(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F);
	}

	@Override
	public void setupAnim(DragonflyEntity entity, float limbAngle, float limbDistance, float animationProgress,
			float headYaw, float headPitch) {
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
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, float red,
			float green, float blue, float alpha) {
		model.render(matrices, vertices, light, overlay);
	}
}
