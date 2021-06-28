package ru.betterend.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class EndCrystalRenderer {
	private static final ResourceLocation CRYSTAL_TEXTURE = new ResourceLocation("textures/entity/end_crystal/end_crystal.png");
	private static final ResourceLocation CRYSTAL_BEAM_TEXTURE = new ResourceLocation("textures/entity/end_crystal/end_crystal_beam.png");
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

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		modelPartData.addOrReplaceChild("FRAME", CubeListBuilder.create()
                        .texOffs(0, 0)
						.addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f),						
				PartPose.ZERO);

		modelPartData.addOrReplaceChild("CORE", CubeListBuilder.create()
                        .texOffs(32, 0)
						.addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f),
				PartPose.ZERO);

		return LayerDefinition.create(modelData, 64, 32);
	}
	
	static {
		END_CRYSTAL = RenderType.entityCutoutNoCull(CRYSTAL_TEXTURE);
		RenderType.entitySmoothCutout(CRYSTAL_BEAM_TEXTURE);
		SINE_45_DEGREES = (float) Math.sin(0.7853981633974483D);

		ModelPart root = getTexturedModelData().bakeRoot();
		FRAME = root.getChild("FRAME");
		CORE = root.getChild("CORE");
	}
}
