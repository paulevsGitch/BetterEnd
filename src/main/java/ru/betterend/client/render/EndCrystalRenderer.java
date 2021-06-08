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
