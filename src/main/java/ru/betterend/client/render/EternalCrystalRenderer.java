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
import ru.betterend.BetterEnd;

public class EternalCrystalRenderer {
	private static final Identifier CRYSTAL_TEXTURE = BetterEnd.makeID("textures/entity/eternal_crystal.png");
	private static final RenderLayer RENDER_LAYER;
	private static final ModelPart CORE;
	
	public static void render(int age, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RENDER_LAYER);
		float rotation = (age + tickDelta) / 25.0F + 6.0F;
		float altitude = MathHelper.sin((age + tickDelta) / 10.0F) * 0.1F + 0.1F;
		matrices.push();
		matrices.scale(0.6F, 0.6F, 0.6F);
		matrices.translate(0.0D, altitude / 2.0D, 0.0D);
		matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(rotation));
		matrices.push();
		matrices.translate(-0.125D, -0.1D, -0.125D);
		CORE.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
		matrices.pop();
	}
	
	static {
		RENDER_LAYER = RenderLayer.getEntityTranslucent(CRYSTAL_TEXTURE);
		CORE = new ModelPart(16, 16, 0, 0);
		CORE.addCuboid(0.0F, 0.0F, 0.0F, 4.0F, 12.0F, 4.0F);
	}
}
