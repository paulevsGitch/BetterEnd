package ru.betterend.client.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

public class BeamRenderer {
	public static void renderLightBeam(MatrixStack matrixStack, VertexConsumer vertexConsumer, float tick, int minY, int maxY, float[] colors, float alpha, float h, float k) {
		int m = minY + maxY;
		
		float o = maxY < 0 ? tick : -tick;
		float p = MathHelper.fractionalPart(o * 0.2F - (float) MathHelper.floor(o * 0.1F));
		float red = colors[0];
		float green = colors[1];
		float blue = colors[2];
		
		matrixStack.push();
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(tick * 2.25F - 45.0F));
		float af = 0.0F;
		float ai = 0.0F;
		float aj = -h;
		float aa = -h;
		float ap = -1.0F + p;
		float aq = (float) maxY * (0.5F / h) + ap;
		
		renderBeam(matrixStack, vertexConsumer, red, green, blue, alpha, minY, m, 0.0F, h, h, 0.0F, aj, 0.0F, 0.0F, aa, 0.0F, 1.0F, aq, ap);
		matrixStack.pop();
		
		af = -k;
		float ag = -k;
		ai = -k;
		aj = -k;
		ap = -1.0F + p;
		aq = (float) maxY + ap;
		renderBeam(matrixStack, vertexConsumer, red, green, blue, alpha, minY, m, af, ag, k, ai, aj, k, k, k, 0.0F, 1.0F, aq, ap);
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

	private static void renderBeam(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int j, int k, float l, float m, float n, float o, float p, float q, float r, float s) {
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, k, l, m, q, r);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, j, l, m, q, s);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, j, n, o, p, s);
		addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, k, n, o, p, r);
	}

	private static void addVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, float y, float x, float l, float m, float n) {
		vertexConsumer.vertex(matrix4f, x, y, l).color(red, green, blue, alpha).texture(m, n).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).next();
	}
}
