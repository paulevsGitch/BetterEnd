package ru.betterend.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import ru.betterend.entity.EntityDragonfly;

public class ModelEntityDragonfly extends EntityModel<EntityDragonfly> {
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

	public ModelEntityDragonfly() {
		textureWidth = 64;
		textureHeight = 64;

		model = new ModelPart(this);
		model.setPivot(2.0F, 21.5F, -4.0F);
		model.setTextureOffset(0, 0).addCuboid(-4.0F, -4.0F, 0.0F, 4.0F, 4.0F, 9.0F, 0.0F);

		head = new ModelPart(this);
		head.setPivot(-2.0F, -2.0F, 0.0F);
		model.addChild(head);
		setRotationAngle(head, 0.3491F, 0.0F, 0.0F);
		head.setTextureOffset(17, 0).addCuboid(-1.5F, -1.5F, -2.5F, 3.0F, 3.0F, 3.0F, 0.0F);

		tail = new ModelPart(this);
		tail.setPivot(-2.0F, -2.0F, 9.0F);
		model.addChild(tail);
		tail.setTextureOffset(26, 0).addCuboid(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 7.0F, 0.0F);

		tail_2 = new ModelPart(this);
		tail_2.setPivot(0.0F, 0.0F, 7.0F);
		tail.addChild(tail_2);
		tail_2.setTextureOffset(36, 0).addCuboid(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 10.0F, 0.0F);

		wing_1 = new ModelPart(this);
		wing_1.setPivot(-2.0F, -4.0F, 4.0F);
		model.addChild(wing_1);
		wing_1.setTextureOffset(0, 13).addCuboid(-15.0F, 0.0F, -3.0F, 15.0F, 0.0F, 4.0F, 0.0F);

		wing_2 = new ModelPart(this);
		wing_2.setPivot(-2.0F, -4.0F, 4.0F);
		model.addChild(wing_2);
		wing_2.mirror = true;
		wing_2.setTextureOffset(0, 13).addCuboid(0.0F, 0.0F, -3.0F, 15.0F, 0.0F, 4.0F, 0.0F);

		wing_3 = new ModelPart(this);
		wing_3.setPivot(-2.0F, -4.0F, 8.0F);
		model.addChild(wing_3);
		wing_3.setTextureOffset(4, 17).addCuboid(-12.0F, 0.0F, -2.5F, 12.0F, 0.0F, 3.0F, 0.0F);

		wing_4 = new ModelPart(this);
		wing_4.setPivot(-2.0F, -4.0F, 8.0F);
		model.addChild(wing_4);
		wing_4.mirror = true;
		wing_4.setTextureOffset(4, 17).addCuboid(0.0F, 0.0F, -2.5F, 12.0F, 0.0F, 3.0F, 0.0F);

		legs_1 = new ModelPart(this);
		legs_1.setPivot(-1.0F, 0.0F, 1.0F);
		model.addChild(legs_1);
		setRotationAngle(legs_1, 0.0F, 0.0F, -0.5236F);
		legs_1.setTextureOffset(50, 1).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F);

		legs_2 = new ModelPart(this);
		legs_2.setPivot(-3.0F, 0.0F, 1.0F);
		model.addChild(legs_2);
		setRotationAngle(legs_2, 0.0F, 0.0F, 0.5236F);
		legs_2.setTextureOffset(50, 1).addCuboid(0.0F, 0.0F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F);
	}


	@Override
	public void setAngles(EntityDragonfly entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float progress = animationProgress * 2F;
		
		wing_1.roll = 0.3491F + (float) Math.sin(progress) * 0.3491F;
		wing_2.roll = -wing_1.roll;
		
		wing_3.roll = 0.3491F + (float) Math.cos(progress) * 0.3491F;
		wing_4.roll = -wing_3.roll;
		
		progress = animationProgress * 0.05F;
		
		head.pitch = 0.3491F + (float) Math.sin(progress * 0.7F) * 0.1F;
		tail.pitch = (float) Math.cos(progress) * 0.05F - 0.05F;
		tail_2.pitch = -tail.pitch * 1.5F;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		model.render(matrices, vertices, light, overlay);
	}
	
	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}
}
