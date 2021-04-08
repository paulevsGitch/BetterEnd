package ru.betterend.client.render;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

public class EndCrystalRenderer {
	private static final ResourceLocation CRYSTAL_TEXTURE = new ResourceLocation(
			"textures/entity/end_crystal/end_crystal.png");
	private static final ResourceLocation CRYSTAL_BEAM_TEXTURE = new ResourceLocation(
			"textures/entity/end_crystal/end_crystal_beam.png");
	private static final RenderLayer CRYSTAL_BEAM_LAYER;
	private static final RenderLayer END_CRYSTAL;
	private static final ModelPart CORE;
	private static final ModelPart FRAME;
	private static final int AGE_CYCLE = 240;
	private static final float SINE_45_DEGREES;

	public static void render(int age, int maxAge, float tickDelta, MatrixStack matrices,
			VertexConsumerProvider vertexConsumerProvider, int light) {
		float k = (float) AGE_CYCLE / maxAge;
		float rotation = (age * k + tickDelta) * 3.0F;
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
		matrices.push();
		matrices.scale(0.8F, 0.8F, 0.8F);
		matrices.translate(0.0D, -0.5D, 0.0D);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
		matrices.translate(0.0D, 0.8F, 0.0D);
		matrices.multiply(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
		FRAME.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.scale(0.875F, 0.875F, 0.875F);
		matrices.multiply(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
		FRAME.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.scale(0.875F, 0.875F, 0.875F);
		matrices.multiply(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
		CORE.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}

	public static void renderBeam(BlockPos start, BlockPos end, float tickDelta, int age, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light) {
		float dx = start.getX() - end.getX() + 1.0F;
		float dy = start.getY() - end.getY() + 1.0F;
		float dz = start.getZ() - end.getZ() + 1.0F;
		float f = Mth.sqrt(dx * dx + dz * dz);
		float g = Mth.sqrt(dx * dx + dy * dy + dz * dz);
		matrices.push();
		matrices.translate(0.0D, 2.0D, 0.0D);
		matrices.multiply(
				Vector3f.POSITIVE_Y.getRadialQuaternion((float) (-Math.atan2((double) dz, (double) dx)) - 1.5707964F));
		matrices.multiply(
				Vector3f.POSITIVE_X.getRadialQuaternion((float) (-Math.atan2((double) f, (double) dy)) - 1.5707964F));
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(CRYSTAL_BEAM_LAYER);
		float h = 0.0F - ((float) age + tickDelta) * 0.01F;
		float i = Mth.sqrt(dx * dx + dy * dy + dz * dz) / 32.0F - ((float) age + tickDelta) * 0.01F;
		float k = 0.0F;
		float l = 0.75F;
		float m = 0.0F;
		MatrixStack.Entry entry = matrices.peek();
		Matrix4f matrix4f = entry.getModel();
		Matrix3f matrix3f = entry.getNormal();

		for (int n = 1; n <= 8; ++n) {
			float o = Mth.sin((float) n * 6.2831855F / 8.0F) * 0.75F;
			float p = Mth.cos((float) n * 6.2831855F / 8.0F) * 0.75F;
			float q = (float) n / 8.0F;
			vertexConsumer.vertex(matrix4f, k * 0.2F, l * 0.2F, 0.0F).color(0, 0, 0, 255).texture(m, h)
					.overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, k, l, g).color(255, 255, 255, 255).texture(m, i)
					.overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, o, p, g).color(255, 255, 255, 255).texture(q, i)
					.overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			vertexConsumer.vertex(matrix4f, o * 0.2F, p * 0.2F, 0.0F).color(0, 0, 0, 255).texture(q, h)
					.overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
			k = o;
			l = p;
			m = q;
		}

		matrices.pop();
	}

	static {
		END_CRYSTAL = RenderLayer.getEntityCutoutNoCull(CRYSTAL_TEXTURE);
		CRYSTAL_BEAM_LAYER = RenderLayer.getEntitySmoothCutout(CRYSTAL_BEAM_TEXTURE);
		SINE_45_DEGREES = (float) Math.sin(0.7853981633974483D);
		FRAME = new ModelPart(64, 32, 0, 0);
		FRAME.addCuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
		CORE = new ModelPart(64, 32, 32, 0);
		CORE.addCuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
	}
}
