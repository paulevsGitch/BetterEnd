package ru.betterend.blocks.entities.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import ru.betterend.blocks.EternalPedestal;
import ru.betterend.blocks.entities.EternalPedestalEntity;
import ru.betterend.client.render.BeamRenderer;

public class EternalPedestalBlockEntityRenderer extends PedestalItemRenderer<EternalPedestalEntity> {

	private static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/end_gateway_beam.png");
	
	public EternalPedestalBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}
	
	@Override
	public void render(EternalPedestalEntity blockEntity, float tickDelta, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light, int overlay) {
		super.render(blockEntity, tickDelta, matrices, vertexConsumers, light, overlay);
		BlockState state = blockEntity.getWorld().getBlockState(blockEntity.getPos());
		if (state.get(EternalPedestal.ACTIVATED)) {
			float[] colors = DyeColor.MAGENTA.getColorComponents();
			int y = blockEntity.getPos().getY();
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, true));
			BeamRenderer.renderLightBeam(matrices, vertexConsumer, tickDelta, -y, 1024 - y, colors, 0.25F, 0.15F, 0.2F);
		}
	}
}
