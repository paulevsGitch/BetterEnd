package ru.betterend.entity.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import ru.betterend.entity.BlockBenchModel;
import ru.betterend.entity.EntityJello;

public class ModelEntityJello extends BlockBenchModel<EntityJello> {
	private final ModelPart model;
	private final ModelPart tentacles;
	private final ModelPart main_cube;

	public ModelEntityJello() {
		super(RenderLayer::getEntityTranslucent);
		
		textureWidth = 48;
		textureHeight = 48;

		model = new ModelPart(this);
		model.setPivot(0.0F, 24.0F, 0.0F);
		model.setTextureOffset(16, 17).addCuboid(-4.0F, -7.0F, 4.5F, 8.0F, 7.0F, 0.0F, 0.0F);
		model.setTextureOffset(16, 17).addCuboid(-4.0F, -7.0F, -4.5F, 8.0F, 7.0F, 0.0F, 0.0F);
		model.setTextureOffset(0, 17).addCuboid(-2.0F, -12.5F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F);

		tentacles = new ModelPart(this);
		tentacles.setPivot(0.0F, -6.0F, 0.0F);
		model.addChild(tentacles);
		setRotationAngle(tentacles, 0.0F, -1.5708F, 0.0F);
		tentacles.setTextureOffset(16, 17).addCuboid(-4.0F, -1.0F, 4.5F, 8.0F, 7.0F, 0.0F, 0.0F);
		tentacles.setTextureOffset(16, 17).addCuboid(-4.0F, -1.0F, -4.5F, 8.0F, 7.0F, 0.0F, 0.0F);

		main_cube = new ModelPart(this);
		main_cube.setPivot(0.0F, -14.0F, 0.0F);
		model.addChild(main_cube);
		setRotationAngle(main_cube, 0.0F, 0.0F, -3.1416F);
		main_cube.setTextureOffset(0, 0).addCuboid(-5.0F, -7.0F, -5.0F, 10.0F, 7.0F, 10.0F, 0.0F);
	}
	
	@Override
	public void setAngles(EntityJello entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		model.render(matrices, vertices, light, overlay);
	}

}
