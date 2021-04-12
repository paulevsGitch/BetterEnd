package ru.betterend.blocks.entities.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import com.mojang.math.Vector3f;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import ru.betterend.blocks.EternalPedestal;
import ru.betterend.blocks.basis.PedestalBlock;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.client.render.BeamRenderer;
import ru.betterend.client.render.EndCrystalRenderer;
import ru.betterend.client.render.EternalCrystalRenderer;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

@Environment(EnvType.CLIENT)
public class PedestalItemRenderer<T extends PedestalBlockEntity> extends BlockEntityRenderer<T> {

	public PedestalItemRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
			int light, int overlay) {

		Level world = blockEntity.getLevel();
		if (world == null || blockEntity.isEmpty())
			return;

		BlockState state = world.getBlockState(blockEntity.getPos());
		if (!(state.getBlock() instanceof PedestalBlock))
			return;

		ItemStack activeItem = blockEntity.getStack(0);

		matrices.push();
		Minecraft minecraft = Minecraft.getInstance();
		BakedModel model = minecraft.getItemRenderer().getHeldItemModel(activeItem, world, null);
		Vector3f translate = model.getTransformation().ground.translation;
		PedestalBlock pedestal = (PedestalBlock) state.getBlock();
		matrices.translate(translate.getX(), translate.getY(), translate.getZ());
		matrices.translate(0.5, pedestal.getHeight(state), 0.5);
		if (activeItem.getItem() instanceof BlockItem) {
			matrices.scale(1.5F, 1.5F, 1.5F);
		} else {
			matrices.scale(1.25F, 1.25F, 1.25F);
		}
		int age = blockEntity.getAge();
		if (state.is(EndBlocks.ETERNAL_PEDESTAL) && state.getValue(EternalPedestal.ACTIVATED)) {
			float[] colors = EternalCrystalRenderer.colors(age);
			int y = blockEntity.getPos().getY();

			BeamRenderer.renderLightBeam(matrices, vertexConsumers, age, tickDelta, -y, 1024 - y, colors, 0.25F, 0.13F,
					0.16F);
			float altitude = Mth.sin((blockEntity.getAge() + tickDelta) / 10.0F) * 0.1F + 0.1F;
			matrices.translate(0.0D, altitude, 0.0D);
		}
		if (activeItem.getItem() == Items.END_CRYSTAL) {
			EndCrystalRenderer.render(age, blockEntity.getMaxAge(), tickDelta, matrices, vertexConsumers, light);
		} else if (activeItem.getItem() == EndItems.ETERNAL_CRYSTAL) {
			EternalCrystalRenderer.render(age, tickDelta, matrices, vertexConsumers, light);
		} else {
			float rotation = (age + tickDelta) / 25.0F + 6.0F;
			matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(rotation));
			minecraft.getItemRenderer().renderItem(activeItem, ModelTransformation.Mode.GROUND, false, matrices,
					vertexConsumers, light, overlay, model);
		}
		matrices.pop();
	}
}
