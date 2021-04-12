package ru.betterend.client.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import com.mojang.math.Vector3f;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public class BeamRenderer {
	private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation("textures/entity/end_gateway_beam.png");

	public static void renderLightBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int age,
			float tick, int minY, int maxY, float[] colors, float alpha, float beamIn, float beamOut) {
		float red = colors[0];
		float green = colors[1];
		float blue = colors[2];

		int maxBY = minY + maxY;
		float delta = maxY < 0 ? tick : -tick;
		float fractDelta = Mth.fractionalPart(delta * 0.2F - (float) Mth.floor(delta * 0.1F));
		float xIn = -beamIn;
		float minV = Mth.clamp(fractDelta - 1.0F, 0.0F, 1.0F);
		float maxV = (float) maxY * (0.5F / beamIn) + minV;
		float rotation = (age + tick) / 25.0F + 6.0F;

		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, true));

		matrices.push();
		matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(-rotation));
		renderBeam(matrices, vertexConsumer, red, green, blue, alpha, minY, maxBY, beamIn, 0.0F, 0.0F, beamIn, 0.0F,
				xIn, xIn, 0.0F, 0.0F, 1.0F, minV, maxV);

		float xOut = -beamOut;
		maxV = (float) maxY + minV;
		renderBeam(matrices, vertexConsumer, red, green, blue, alpha, minY, maxBY, xOut, xOut, beamOut, xOut, xOut,
				beamOut, beamOut, beamOut, 0.0F, 1.0F, minV, maxV);
		matrices.pop();
	}

	private static void renderBeam(MatrixStack matrices, VertexConsumer vertexConsumer, float red, float green,
			float blue, float alpha, int minY, int maxY, float x1, float d1, float x2, float d2, float x3, float d3,
			float x4, float d4, float minU, float maxU, float minV, float maxV) {
		MatrixStack.Entry entry = matrices.peek();
		Matrix4f matrix4f = entry.getModel();
		Matrix3f matrix3f = entry.getNormal();
		renderBeam(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, maxY, minY, x1, d1, x2, d2, minU, maxU,
				minV, maxV);
		renderBeam(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, maxY, minY, x4, d4, x3, d3, minU, maxU,
				minV, maxV);
		renderBeam(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, maxY, minY, x2, d2, x4, d4, minU, maxU,
				minV, maxV);
		renderBeam(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, maxY, minY, x3, d3, x1, d1, minU, maxU,
				minV, maxV);
	}

	private static void renderBeam(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red,
			float green, float blue, float alpha, int minY, int maxY, float minX, float minD, float maxX, float maxD,
			float minU, float maxU, float minV, float maxV) {
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, maxX, minY, maxD, maxU, minV);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, maxX, maxY, maxD, maxU, maxV);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, minX, maxY, minD, minU, maxV);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, minX, minY, minD, minU, minV);
	}

	private static void addVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red,
			float green, float blue, float alpha, float x, float y, float d, float u, float v) {
		vertexConsumer.vertex(matrix4f, x, y, d).color(red, green, blue, alpha).texture(u, v)
				.overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).next();
	}
}
