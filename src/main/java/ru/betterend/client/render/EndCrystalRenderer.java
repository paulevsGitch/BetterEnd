package ru.betterend.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EndCrystalRenderer {
	private static final ResourceLocation CRYSTAL_TEXTURE = new ResourceLocation("textures/entity/end_crystal/end_crystal.png");
	private static final ResourceLocation CRYSTAL_BEAM_TEXTURE = new ResourceLocation("textures/entity/end_crystal/end_crystal_beam.png");
	private static final RenderType CRYSTAL_BEAM_LAYER;
	private static final RenderType END_CRYSTAL;
	private static final ModelPart CORE;
	private static final ModelPart FRAME;
	private static final int AGE_CYCLE = 240;
	private static final float SINE_45_DEGREES;
	
	public static void render(int age, int maxAge, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int light) {
		float k = (float) AGE_CYCLE / maxAge;
		float rotation = (age * k + tickDelta) * 3.0F;
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(END_CRYSTAL);
		matrices.pushPose();
		matrices.scale(0.8F, 0.8F, 0.8F);
		matrices.translate(0.0D, -0.5D, 0.0D);
		matrices.mulPose(Vector3f.YP.rotationDegrees(rotation));
		matrices.translate(0.0D, 0.8F, 0.0D);
		matrices.mulPose(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
		FRAME.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
		matrices.scale(0.875F, 0.875F, 0.875F);
		matrices.mulPose(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
		matrices.mulPose(Vector3f.YP.rotationDegrees(rotation));
		FRAME.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
		matrices.scale(0.875F, 0.875F, 0.875F);
		matrices.mulPose(new Quaternion(new Vector3f(SINE_45_DEGREES, 0.0F, SINE_45_DEGREES), 60.0F, true));
		matrices.mulPose(Vector3f.YP.rotationDegrees(rotation));
		CORE.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
		matrices.popPose();
	}
	
	public static void renderBeam(BlockPos start, BlockPos end, float tickDelta, int age, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		float dx = start.getX() - end.getX() + 1.0F;
		float dy = start.getY() - end.getY() + 1.0F;
		float dz = start.getZ() - end.getZ() + 1.0F;
		float f = Mth.sqrt(dx * dx + dz * dz);
		float g = Mth.sqrt(dx * dx + dy * dy + dz * dz);
		matrices.pushPose();
		matrices.translate(0.0D, 2.0D, 0.0D);
		matrices.mulPose(Vector3f.YP.rotation((float)(-Math.atan2((double) dz, (double) dx)) - 1.5707964F));
		matrices.mulPose(Vector3f.XP.rotation((float)(-Math.atan2((double) f, (double) dy)) - 1.5707964F));
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(CRYSTAL_BEAM_LAYER);
		float h = 0.0F - ((float) age + tickDelta) * 0.01F;
		float i = Mth.sqrt(dx * dx + dy * dy + dz * dz) / 32.0F - ((float) age + tickDelta) * 0.01F;
		float k = 0.0F;
		float l = 0.75F;
		float m = 0.0F;
		PoseStack.Pose entry = matrices.last();
		Matrix4f matrix4f = entry.pose();
		Matrix3f matrix3f = entry.normal();

		for(int n = 1; n <= 8; ++n) {
		   float o = Mth.sin((float) n * 6.2831855F / 8.0F) * 0.75F;
		   float p = Mth.cos((float) n * 6.2831855F / 8.0F) * 0.75F;
		   float q = (float) n / 8.0F;
		   vertexConsumer.vertex(matrix4f, k * 0.2F, l * 0.2F, 0.0F).color(0, 0, 0, 255).uv(m, h).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		   vertexConsumer.vertex(matrix4f, k, l, g).color(255, 255, 255, 255).uv(m, i).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		   vertexConsumer.vertex(matrix4f, o, p, g).color(255, 255, 255, 255).uv(q, i).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		   vertexConsumer.vertex(matrix4f, o * 0.2F, p * 0.2F, 0.0F).color(0, 0, 0, 255).uv(q, h).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
		   k = o;
		   l = p;
		   m = q;
		}

		matrices.popPose();
	}
	
	static {
		END_CRYSTAL = RenderType.entityCutoutNoCull(CRYSTAL_TEXTURE);
		CRYSTAL_BEAM_LAYER = RenderType.entitySmoothCutout(CRYSTAL_BEAM_TEXTURE);
		SINE_45_DEGREES = (float) Math.sin(0.7853981633974483D);
		FRAME = new ModelPart(64, 32, 0, 0);
		FRAME.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
		CORE = new ModelPart(64, 32, 32, 0);
		CORE.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
	}
}
