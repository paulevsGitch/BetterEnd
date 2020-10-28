package ru.betterend.blocks.entities.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ru.betterend.blocks.EternalPedestal;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.client.render.BeamRenderer;
import ru.betterend.registry.EndBlocks;

@Environment(EnvType.CLIENT)
public class PedestalItemRenderer extends BlockEntityRenderer<PedestalBlockEntity> {
	private static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/end_gateway_beam.png");
	
	public PedestalItemRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(PedestalBlockEntity blockEntity, float tickDelta, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light, int overlay) {
		
		if (blockEntity.isEmpty()) return;
		
		BlockState state = blockEntity.getWorld().getBlockState(blockEntity.getPos());
		ItemStack activeItem = blockEntity.getStack(0);
		matrices.push();
		matrices.translate(0.5, 1.1, 0.5);
		float rotation = (blockEntity.getAge() + tickDelta) / 25.0F + 6.0F;
		float altitude = MathHelper.sin((blockEntity.getAge() + tickDelta) / 10.0F) * 0.1F;
		matrices.translate(0.0D, altitude, 0.0D);
		matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(rotation));
		MinecraftClient minecraft = MinecraftClient.getInstance();
		minecraft.getItemRenderer().renderItem(activeItem, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
		if (state.isOf(EndBlocks.ETERNAL_PEDESTAL) && state.get(EternalPedestal.ACTIVATED)) {
			float[] colors = DyeColor.MAGENTA.getColorComponents();
			int y = blockEntity.getPos().getY();
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, true));
			BeamRenderer.renderLightBeam(matrices, vertexConsumer, tickDelta, -y, 1024 - y, colors, 0.25F, 0.15F, 0.2F);
		}
		matrices.pop();
	}
}
