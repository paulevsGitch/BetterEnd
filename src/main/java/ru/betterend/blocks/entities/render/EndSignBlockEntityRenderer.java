package ru.betterend.blocks.entities.render;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Maps;

import net.minecraft.world.level.block.AbstractSignBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer.SignModel;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import com.mojang.math.Vector3f;
import net.minecraft.world.item.BlockItem;
import net.minecraft.text.OrderedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.SignType;
import net.minecraft.core.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.basis.EndSignBlock;
import ru.betterend.blocks.entities.ESignBlockEntity;
import ru.betterend.registry.EndItems;

public class EndSignBlockEntityRenderer extends BlockEntityRenderer<ESignBlockEntity> {
	private static final HashMap<Block, RenderLayer> LAYERS = Maps.newHashMap();
	private static RenderLayer defaultLayer;
	private final SignModel model = new SignBlockEntityRenderer.SignModel();

	public EndSignBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	public void render(ESignBlockEntity signBlockEntity, float tickDelta, MatrixStack matrixStack,
			VertexConsumerProvider provider, int light, int overlay) {
		BlockState state = signBlockEntity.getCachedState();
		matrixStack.push();

		matrixStack.translate(0.5D, 0.5D, 0.5D);
		float angle = -((float) ((Integer) state.getValue(SignBlock.ROTATION) * 360) / 16.0F);

		BlockState blockState = signBlockEntity.getCachedState();
		if (blockState.get(EndSignBlock.FLOOR)) {
			matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(angle));
			this.model.foot.visible = true;
		} else {
			matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(angle + 180));
			matrixStack.translate(0.0D, -0.3125D, -0.4375D);
			this.model.foot.visible = false;
		}

		matrixStack.push();
		matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
		VertexConsumer vertexConsumer = getConsumer(provider, state.getBlock());
		model.field.render(matrixStack, vertexConsumer, light, overlay);
		model.foot.render(matrixStack, vertexConsumer, light, overlay);
		matrixStack.pop();
		TextRenderer textRenderer = dispatcher.getTextRenderer();
		matrixStack.translate(0.0D, 0.3333333432674408D, 0.046666666865348816D);
		matrixStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
		int m = signBlockEntity.getTextColor().getSignColor();
		int n = (int) (NativeImage.getRed(m) * 0.4D);
		int o = (int) (NativeImage.getGreen(m) * 0.4D);
		int p = (int) (NativeImage.getBlue(m) * 0.4D);
		int q = NativeImage.getAbgrColor(0, p, o, n);

		for (int s = 0; s < 4; ++s) {
			OrderedText orderedText = signBlockEntity.getTextBeingEditedOnRow(s, (text) -> {
				List<OrderedText> list = textRenderer.wrapLines(text, 90);
				return list.isEmpty() ? OrderedText.EMPTY : (OrderedText) list.get(0);
			});
			if (orderedText != null) {
				float t = (float) (-textRenderer.getWidth(orderedText) / 2);
				textRenderer.draw((OrderedText) orderedText, t, (float) (s * 10 - 20), q, false,
						matrixStack.peek().getModel(), provider, false, 0, light);
			}
		}

		matrixStack.pop();
	}

	public static SpriteIdentifier getModelTexture(Block block) {
		SignType signType2;
		if (block instanceof AbstractSignBlock) {
			signType2 = ((AbstractSignBlock) block).getSignType();
		} else {
			signType2 = SignType.OAK;
		}

		return TexturedRenderLayers.getSignTextureId(signType2);
	}

	public static VertexConsumer getConsumer(VertexConsumerProvider provider, Block block) {
		return provider.getBuffer(LAYERS.getOrDefault(block, defaultLayer));
	}

	static {
		defaultLayer = RenderLayer.getEntitySolid(new ResourceLocation("textures/entity/sign/oak.png"));

		EndItems.getModBlocks().forEach((item) -> {
			if (item instanceof BlockItem) {
				Block block = ((BlockItem) item).getBlock();
				if (block instanceof EndSignBlock) {
					String name = Registry.BLOCK.getKey(block).getPath();
					RenderLayer layer = RenderLayer
							.getEntitySolid(BetterEnd.makeID("textures/entity/sign/" + name + ".png"));
					LAYERS.put(block, layer);
				}
			}
		});
	}

}
