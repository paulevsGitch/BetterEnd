package ru.betterend.client.render;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

import ru.betterend.BetterEnd;
import ru.betterend.blocks.AuroraCrystalBlock;
import ru.betterend.util.ColorUtil;
import ru.betterend.util.MHelper;

public class EternalCrystalRenderer {
	private static final Identifier CRYSTAL_TEXTURE = BetterEnd.makeID("textures/entity/eternal_crystal.png");
	private static final RenderLayer RENDER_LAYER;
	private static final ModelPart SHARDS;
	private static final ModelPart CORE;
	
	public static void render(int age, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RENDER_LAYER);
		float[] colors = colors(age);
		float rotation = (age + tickDelta) / 25.0F + 6.0F;
		matrices.push();
		matrices.scale(0.6F, 0.6F, 0.6F);
		matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(rotation));
		CORE.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, colors[0], colors[1], colors[2], colors[3]);
		SHARDS.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, colors[0], colors[1], colors[2], colors[3]);
		matrices.pop();
	}
	
	private static float[] colors(int age) {
		double delta = age * 0.01;
		int index = MHelper.floor(delta);
		int index2 = (index + 1) & 3;
		delta -= index;
		index &= 3;
		
		Vec3i color1 = AuroraCrystalBlock.COLORS[index];
		Vec3i color2 = AuroraCrystalBlock.COLORS[index2];
		
		int r = MHelper.floor(MathHelper.lerp(delta, color1.getX(), color2.getX()));
		int g = MHelper.floor(MathHelper.lerp(delta, color1.getY(), color2.getY()));
		int b = MHelper.floor(MathHelper.lerp(delta, color1.getZ(), color2.getZ()));
		
		return ColorUtil.toFloatArray(MHelper.color(r, g, b));
	}
	
	static {
		RENDER_LAYER = RenderLayer.getBeaconBeam(CRYSTAL_TEXTURE, true);
		SHARDS = new ModelPart(16, 16, 2, 4);
		SHARDS.addCuboid(-5.0F, 1.0F, -3.0F, 2.0F, 8.0F, 2.0F);
		SHARDS.addCuboid(3.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F);
		SHARDS.addCuboid(-1.0F, 0.0F, -5.0F, 2.0F, 4.0F, 2.0F);
		SHARDS.addCuboid(0.0F, 3.0F, 4.0F, 2.0F, 6.0F, 2.0F);
		CORE = new ModelPart(16, 16, 0, 0);
		CORE.addCuboid(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F);
	}
}
