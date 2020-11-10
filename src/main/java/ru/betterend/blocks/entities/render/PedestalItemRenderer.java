package ru.betterend.blocks.entities.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import ru.betterend.blocks.basis.BlockPedestal;
import ru.betterend.blocks.entities.PedestalBlockEntity;

@Environment(EnvType.CLIENT)
public class PedestalItemRenderer<T extends PedestalBlockEntity> extends BlockEntityRenderer<T> {
	
	public PedestalItemRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T blockEntity, float tickDelta, MatrixStack matrices,
			VertexConsumerProvider vertexConsumers, int light, int overlay) {
		
		if (blockEntity.isEmpty()) return;
		
		BlockState state = blockEntity.getWorld().getBlockState(blockEntity.getPos());
		ItemStack activeItem = blockEntity.getStack(0);
		matrices.push();
		MinecraftClient minecraft = MinecraftClient.getInstance();
		BakedModel model = minecraft.getItemRenderer().getHeldItemModel(activeItem, blockEntity.getWorld(), null);
		Vector3f translate = model.getTransformation().ground.translation;
		BlockPedestal pedestal = (BlockPedestal) state.getBlock();
		matrices.translate(translate.getX(), translate.getY(), translate.getZ());
		matrices.translate(0.5, pedestal.getHeight(state), 0.5);
		if (activeItem.getItem() instanceof BlockItem) {
			matrices.scale(1.5F, 1.5F, 1.5F);
		} else {
			matrices.scale(1.25F, 1.25F, 1.25F);
		}
		
		float rotation = (blockEntity.getAge() + tickDelta) / 25.0F + 6.0F;
		matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(rotation));
		
		
		if (activeItem.getItem() == Items.END_CRYSTAL) {
			EndCrystalRenderer.render(blockEntity.getAge(), blockEntity.getMaxAge(), tickDelta, matrices, vertexConsumers, light);
		} else {
			minecraft.getItemRenderer().renderItem(activeItem, ModelTransformation.Mode.GROUND, false, matrices, vertexConsumers, light, overlay, model);
		}
		matrices.pop();
	}
}
