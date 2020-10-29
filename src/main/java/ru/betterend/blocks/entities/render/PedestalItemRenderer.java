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
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import ru.betterend.blocks.BlockProperties.PedestalState;
import ru.betterend.blocks.EternalPedestal;
import ru.betterend.blocks.basis.BlockPedestal;
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
		if (state.get(BlockPedestal.STATE) == PedestalState.DEFAULT) {
			matrices.translate(0.5, 1.0, 0.5);
		} else {
			matrices.translate(0.5, 0.8, 0.5);
		}
		if (activeItem.getItem() instanceof BlockItem) {
			matrices.scale(1.5F, 1.5F, 1.5F);
		} else {
			matrices.scale(1.15F, 1.15F, 1.15F);
		}
		
		float rotation = (blockEntity.getAge() + tickDelta) / 25.0F + 6.0F;
		matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(rotation));
		MinecraftClient minecraft = MinecraftClient.getInstance();
		if (state.isOf(EndBlocks.ETERNAL_PEDESTAL) && state.get(EternalPedestal.ACTIVATED)) {
			float altitude = MathHelper.sin((blockEntity.getAge() + tickDelta) / 10.0F) * 0.1F + 0.1F;
			matrices.translate(0.0D, altitude, 0.0D);
			float[] colors = DyeColor.MAGENTA.getColorComponents();
			int y = blockEntity.getPos().getY();
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, true));
			BeamRenderer.renderLightBeam(matrices, vertexConsumer, tickDelta, -y, 1024 - y, colors, 0.25F, 0.15F, 0.2F);
		}
		minecraft.getItemRenderer().renderItem(activeItem, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
		matrices.pop();
	}
}
