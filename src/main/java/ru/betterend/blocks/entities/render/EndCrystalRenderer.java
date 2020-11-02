package ru.betterend.blocks.entities.render;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;

public class EndCrystalRenderer {
	private static final Identifier TEXTURE = new Identifier("textures/entity/end_crystal/end_crystal.png");
	private static final RenderLayer END_CRYSTAL;
	private static final float SINE_45_DEGREES;
	private static final ModelPart CORE;
	private static final ModelPart FRAME;
	
	public static void render(int age, float tickDelta, float rotation, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {
		float rotation2 = (age + tickDelta) * 3.0F;
//		System.out.println("=====");
//		System.out.println("A: " + age);
//		System.out.println("R1: " + rotation);
//		System.out.println("R2: " + rotation2);
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
		matrices.push();
		matrices.scale(0.8F, 0.8F, 0.8F);
		matrices.translate(0.0D, -0.5D, 0.0D);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation2));
		matrices.translate(0.0D, 0.8F, 0.0D);
		matrices.multiply(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
		FRAME.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.scale(0.875F, 0.875F, 0.875F);
		matrices.multiply(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation2));
		FRAME.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.scale(0.875F, 0.875F, 0.875F);
		matrices.multiply(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation2));
		CORE.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}
	
	static {
		END_CRYSTAL = RenderLayer.getEntityCutoutNoCull(TEXTURE);
		SINE_45_DEGREES = (float)Math.sin(0.7853981633974483D);
		FRAME = new ModelPart(64, 32, 0, 0);
		FRAME.addCuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
		CORE = new ModelPart(64, 32, 32, 0);
		CORE.addCuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
	}
}
