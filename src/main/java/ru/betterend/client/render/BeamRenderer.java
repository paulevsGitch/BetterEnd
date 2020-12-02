package ru.betterend.client.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public class BeamRenderer {
	public static void renderLightBeam(MatrixStack matrixStack, VertexConsumer vertexConsumer, int age, float tick, int minY, int maxY, float[] colors, float alpha, float h, float k) {
		int m = minY + maxY;
		
		float delta = maxY < 0 ? tick : -tick;
		float p = MathHelper.fractionalPart(delta * 0.2F - (float) MathHelper.floor(delta * 0.1F));
		float red = colors[0];
		float green = colors[1];
		float blue = colors[2];
		
		float af = 0.0F;
		float aj = -h;
		float ap = p - 1.0F;
		float aq = (float) maxY * (0.5F / h) + ap;
		
		float rotation = (age + tick) / 25.0F + 6.0F;
		
		matrixStack.push();
		matrixStack.push();
		matrixStack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(-rotation));
		renderBeam(matrixStack, vertexConsumer, red, green, blue, alpha, minY, m, 0.0F, h, h, 0.0F, aj, 0.0F, 0.0F, aj, 0.0F, 1.0F, aq, ap);
		matrixStack.pop();
		
		af = -k;
		aq = (float) maxY + ap;
		matrixStack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(-rotation));
		renderBeam(matrixStack, vertexConsumer, red, green, blue, alpha, minY, m, af, af, k, af, af, k, k, k, 0.0F, 1.0F, aq, ap);
		matrixStack.pop();
	}

	private static void renderBeam(MatrixStack matrixStack, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int j, int k, float l, float m, float n, float o, float p, float q, float r, float s, float t, float u, float v, float w) {
		MatrixStack.Entry entry = matrixStack.peek();
		Matrix4f matrix4f = entry.getModel();
		Matrix3f matrix3f = entry.getNormal();		
		renderBeam(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, j, k, l, m, n, o, t, u, v, w);
		renderBeam(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, j, k, r, s, p, q, t, u, v, w);
		renderBeam(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, j, k, n, o, r, s, t, u, v, w);
		renderBeam(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, j, k, p, q, l, m, t, u, v, w);
	}

	private static void renderBeam(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int maxY, int minY, float maxX, float m, float minX, float o, float minU, float maxU, float minV, float maxV) {
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, maxX, minY, m, maxU, minV);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, maxX, maxY, m, maxU, maxV);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, minX, maxY, o, minU, maxV);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, minX, minY, o, minU, minV);
	}

	private static void addVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, float x, float y, float d, float u, float v) {
		vertexConsumer.vertex(matrix4f, x, y, d).color(red, green, blue, alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).next();
	}
}
