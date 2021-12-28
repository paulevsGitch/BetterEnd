package ru.betterend.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.blocks.EternalPedestal;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

@Environment(EnvType.CLIENT)
public class PedestalItemRenderer<T extends PedestalBlockEntity> implements BlockEntityRenderer<T> {
	public PedestalItemRenderer(BlockEntityRendererProvider.Context ctx) {
		super();
	}
	
	@Override
	public void render(T blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		Level world = blockEntity.getLevel();
		if (world == null || blockEntity.isEmpty()) return;
		
		BlockState state = world.getBlockState(blockEntity.getBlockPos());
		if (!(state.getBlock() instanceof PedestalBlock)) return;
		
		ItemStack activeItem = blockEntity.getItem(0);
		
		matrices.pushPose();
		Minecraft minecraft = Minecraft.getInstance();
		BakedModel model = minecraft.getItemRenderer().getModel(activeItem, world, null, 0);
		Vector3f translate = model.getTransforms().ground.translation;
		PedestalBlock pedestal = (PedestalBlock) state.getBlock();
		matrices.translate(translate.x() + 0.5, translate.y() + pedestal.getHeight(state), translate.z() + 0.5);
		if (activeItem.getItem() instanceof BlockItem) {
			matrices.scale(1.5F, 1.5F, 1.5F);
		}
		else {
			matrices.scale(1.25F, 1.25F, 1.25F);
		}
		int age = (int) (minecraft.level.getGameTime() % 314);
		if (state.is(EndBlocks.ETERNAL_PEDESTAL) && state.getValue(EternalPedestal.ACTIVATED)) {
			float[] colors = EternalCrystalRenderer.colors(age);
			int y = blockEntity.getBlockPos().getY();
			
			BeamRenderer.renderLightBeam(matrices, vertexConsumers, age, tickDelta, -y, 1024 - y, colors, 0.25F, 0.13F, 0.16F);
			float altitude = Mth.sin((age + tickDelta) / 10.0F) * 0.1F + 0.1F;
			matrices.translate(0.0D, altitude, 0.0D);
		}
		if (activeItem.getItem() == Items.END_CRYSTAL) {
			EndCrystalRenderer.render(age, 314, tickDelta, matrices, vertexConsumers, light);
		}
		else if (activeItem.getItem() == EndItems.ETERNAL_CRYSTAL) {
			EternalCrystalRenderer.render(age, tickDelta, matrices, vertexConsumers, light);
		}
		else {
			float rotation = (age + tickDelta) / 25.0F + 6.0F;
			matrices.mulPose(Vector3f.YP.rotation(rotation));
			minecraft.getItemRenderer().render(activeItem, ItemTransforms.TransformType.GROUND, false, matrices, vertexConsumers, light, overlay, model);
		}
		matrices.popPose();
	}
}
