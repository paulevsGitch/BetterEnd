package ru.betterend.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import ru.betterend.entity.CubozoaEntity;

public class CubozoaEntityModel extends BlockBenchModel<CubozoaEntity> {
	private final ModelPart model;
	private final ModelPart main_cube_r1;
	private final ModelPart tentacle_center_1;
	private final ModelPart tentacle_1;
	private final ModelPart tentacle_center_2;
	private final ModelPart tentacle_2;
	private final ModelPart tentacle_center_3;
	private final ModelPart tentacle_3;
	private final ModelPart tentacle_center_4;
	private final ModelPart tentacle_4;
	private float scaleY;
	private float scaleXZ;

	public CubozoaEntityModel() {
		super(RenderLayer::getEntityTranslucent);
		
		textureWidth = 48;
		textureHeight = 48;
		
		model = new ModelPart(this);
		model.setPivot(0.0F, 24.0F, 0.0F);
		model.setTextureOffset(0, 17).addCuboid(-2.0F, -12.5F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F);

		main_cube_r1 = new ModelPart(this);
		main_cube_r1.setPivot(0.0F, -14.0F, 0.0F);
		model.addChild(main_cube_r1);
		setRotationAngle(main_cube_r1, 0.0F, 0.0F, -3.1416F);
		main_cube_r1.setTextureOffset(0, 0).addCuboid(-5.0F, -7.0F, -5.0F, 10.0F, 7.0F, 10.0F, 0.0F);

		tentacle_center_1 = new ModelPart(this);
		tentacle_center_1.setPivot(0.0F, 0.0F, 0.0F);
		model.addChild(tentacle_center_1);
		

		tentacle_1 = new ModelPart(this);
		tentacle_1.setPivot(0.0F, -7.0F, 4.5F);
		tentacle_center_1.addChild(tentacle_1);
		tentacle_1.setTextureOffset(16, 17).addCuboid(-4.0F, 0.0F, 0.0F, 8.0F, 7.0F, 0.0F, 0.0F);

		tentacle_center_2 = new ModelPart(this);
		tentacle_center_2.setPivot(0.0F, 0.0F, 0.0F);
		model.addChild(tentacle_center_2);
		setRotationAngle(tentacle_center_2, 0.0F, -1.5708F, 0.0F);
		

		tentacle_2 = new ModelPart(this);
		tentacle_2.setPivot(0.0F, -7.0F, 4.5F);
		tentacle_center_2.addChild(tentacle_2);
		tentacle_2.setTextureOffset(16, 17).addCuboid(-4.0F, 0.0F, 0.0F, 8.0F, 7.0F, 0.0F, 0.0F);

		tentacle_center_3 = new ModelPart(this);
		tentacle_center_3.setPivot(0.0F, 0.0F, 0.0F);
		model.addChild(tentacle_center_3);
		setRotationAngle(tentacle_center_3, 0.0F, 3.1416F, 0.0F);
		

		tentacle_3 = new ModelPart(this);
		tentacle_3.setPivot(0.0F, -7.0F, 4.5F);
		tentacle_center_3.addChild(tentacle_3);
		tentacle_3.setTextureOffset(16, 17).addCuboid(-4.0F, 0.0F, 0.0F, 8.0F, 7.0F, 0.0F, 0.0F);

		tentacle_center_4 = new ModelPart(this);
		tentacle_center_4.setPivot(0.0F, 0.0F, 0.0F);
		model.addChild(tentacle_center_4);
		setRotationAngle(tentacle_center_4, 0.0F, 1.5708F, 0.0F);
		

		tentacle_4 = new ModelPart(this);
		tentacle_4.setPivot(0.0F, -7.0F, 4.5F);
		tentacle_center_4.addChild(tentacle_4);
		tentacle_4.setTextureOffset(16, 17).addCuboid(-4.0F, 0.0F, 0.0F, 8.0F, 7.0F, 0.0F, 0.0F);
	}
	
	@Override
	public void setAngles(CubozoaEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float sin = MathHelper.sin(animationProgress * 0.13F);
		scaleY = sin * 0.1F + 0.9F;
		scaleXZ = MathHelper.sin(animationProgress * 0.13F + 3.14F) * 0.1F + 0.9F;
		
		tentacle_1.pitch = sin * 0.15F;
		tentacle_2.pitch = sin * 0.15F;
		tentacle_3.pitch = sin * 0.15F;
		tentacle_4.pitch = sin * 0.15F;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		matrices.push();
		matrices.scale(scaleXZ, scaleY, scaleXZ);
		model.render(matrices, vertices, light, overlay);
		matrices.pop();
	}

}
