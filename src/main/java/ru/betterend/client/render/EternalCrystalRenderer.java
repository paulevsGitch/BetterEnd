package ru.betterend.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import ru.bclib.util.ColorUtil;
import ru.bclib.util.MHelper;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.AuroraCrystalBlock;

public class EternalCrystalRenderer {
	private static final RenderType RENDER_LAYER;
	private static final ModelPart[] SHARDS;
	private static final ModelPart CORE;
	
	public static void render(int age, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int light) {
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RENDER_LAYER);
		float[] colors = colors(age);
		float rotation = (age + tickDelta) / 25.0F + 6.0F;
		matrices.pushPose();
		matrices.scale(0.6F, 0.6F, 0.6F);
		matrices.mulPose(Vector3f.YP.rotation(rotation));
		CORE.render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, colors[0], colors[1], colors[2], colors[3]);
		
		for (int i = 0; i < 4; i++) {
			matrices.pushPose();
			float offset = Mth.sin(rotation * 2 + i) * 0.15F;
			matrices.translate(0, offset, 0);
			SHARDS[i].render(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, colors[0], colors[1], colors[2], colors[3]);
			matrices.popPose();
		}
		
		matrices.popPose();
	}
	
	public static float[] colors(int age) {
		double delta = age * 0.01;
		int index = MHelper.floor(delta);
		int index2 = (index + 1) & 3;
		delta -= index;
		index &= 3;
		
		Vec3i color1 = AuroraCrystalBlock.COLORS[index];
		Vec3i color2 = AuroraCrystalBlock.COLORS[index2];
		
		int r = MHelper.floor(Mth.lerp(delta, color1.getX(), color2.getX()));
		int g = MHelper.floor(Mth.lerp(delta, color1.getY(), color2.getY()));
		int b = MHelper.floor(Mth.lerp(delta, color1.getZ(), color2.getZ()));
		
		return ColorUtil.toFloatArray(ColorUtil.color(r, g, b));
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		modelPartData.addOrReplaceChild("SHARDS_0", CubeListBuilder.create().texOffs(2, 4).addBox(-5.0f, 1.0f, -3.0f, 2.0f, 8.0f, 2.0f), PartPose.ZERO);
		
		modelPartData.addOrReplaceChild("SHARDS_1", CubeListBuilder.create().texOffs(2, 4).addBox(3.0f, -1.0f, -1.0f, 2.0f, 8.0f, 2.0f), PartPose.ZERO);
		
		modelPartData.addOrReplaceChild("SHARDS_2", CubeListBuilder.create().texOffs(2, 4).addBox(-1.0f, 0.0f, -5.0f, 2.0f, 4.0f, 2.0f), PartPose.ZERO);
		
		modelPartData.addOrReplaceChild("SHARDS_3", CubeListBuilder.create().texOffs(2, 4).addBox(0.0f, 3.0f, 4.0f, 2.0f, 6.0f, 2.0f), PartPose.ZERO);
		
		modelPartData.addOrReplaceChild("CORE", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f), PartPose.ZERO);
		
		return LayerDefinition.create(modelData, 16, 16);
	}
	
	static {
		RENDER_LAYER = RenderType.beaconBeam(BetterEnd.makeID("textures/entity/eternal_crystal.png"), true);
		SHARDS = new ModelPart[4];
		
		ModelPart root = getTexturedModelData().bakeRoot();
		SHARDS[0] = root.getChild("SHARDS_0");
		SHARDS[1] = root.getChild("SHARDS_1");
		SHARDS[2] = root.getChild("SHARDS_2");
		SHARDS[3] = root.getChild("SHARDS_3");
		CORE = root.getChild("CORE");
	}
}
