package ru.betterend.blocks.entities.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.SlabType;
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
import ru.betterend.blocks.entities.EternalPedestalBlockEntity;
import ru.betterend.client.render.BeamRenderer;

@Environment(EnvType.CLIENT)
public class EternalPedestalItemRenderer extends BlockEntityRenderer<EternalPedestalBlockEntity> {
	private static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/end_gateway_beam.png");
	
	public EternalPedestalItemRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(EternalPedestalBlockEntity entity, float tickDelta, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light, int overlay) {
		
		if (entity.isEmpty()) return;
		
		BlockState state = entity.getWorld().getBlockState(entity.getPos());
		SlabType type = state.get(EternalPedestal.TYPE);
		
		ItemStack activeItem = entity.getStack(0);
		matrices.push();
		if (type.equals(SlabType.DOUBLE)) {
			matrices.translate(0.5, 1.5, 0.5);
		} else {
			matrices.translate(0.5, 1.0, 0.5);
		}
		float altitude = MathHelper.sin((entity.getAge() + tickDelta) / 10.0F) * 0.1F;
		float rotation = (entity.getAge() + tickDelta) / 25.0F + 6.0F;
		matrices.translate(0.0D, altitude, 0.0D);
		matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(rotation));
		MinecraftClient minecraft = MinecraftClient.getInstance();
		minecraft.getItemRenderer().renderItem(activeItem, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers);
		float[] colors = DyeColor.MAGENTA.getColorComponents();
		int y = entity.getPos().getY();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(BEAM_TEXTURE, true));
		BeamRenderer.renderLightBeam(matrices, vertexConsumer, tickDelta, -y, 1024 - y, colors, 0.25F, 0.15F, 0.2F);
		matrices.pop();
	}
}
