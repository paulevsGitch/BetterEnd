package ru.betterend.mixin.client;

import java.awt.Point;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.BlockRenderView;
import ru.betterend.client.ClientOptions;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
	private static final int POISON_COLOR = MHelper.color(92, 160, 78);
	private static final int STREAM_COLOR = MHelper.color(105, 213, 244);
	private static final Point[] OFFSETS;
	private static final boolean HAS_SODIUM;

	@Inject(method = "getWaterColor", at = @At("RETURN"), cancellable = true)
	private static void be_getWaterColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
		if (ClientOptions.useSulfurWaterColor()) {
			BlockRenderView view = HAS_SODIUM ? Minecraft.getInstance().world : world;
			MutableBlockPos mut = new MutableBlockPos();
			mut.setY(pos.getY());
			for (int i = 0; i < OFFSETS.length; i++) {
				mut.setX(pos.getX() + OFFSETS[i].x);
				mut.setZ(pos.getZ() + OFFSETS[i].y);
				if ((view.getBlockState(mut).is(EndBlocks.BRIMSTONE))) {
					info.setReturnValue(i < 16 ? STREAM_COLOR : POISON_COLOR);
					info.cancel();
					return;
				}
			}
		}
	}

	static {
		HAS_SODIUM = FabricLoader.getInstance().isModLoaded("sodium");

		OFFSETS = new Point[20];
		for (int i = 0; i < 3; i++) {
			int p = i - 1;
			OFFSETS[i] = new Point(p, -2);
			OFFSETS[i + 3] = new Point(p, 2);
			OFFSETS[i + 6] = new Point(-2, p);
			OFFSETS[i + 9] = new Point(2, p);
		}

		for (int i = 0; i < 4; i++) {
			int inner = i + 16;
			Direction dir = BlocksHelper.HORIZONTAL[i];
			OFFSETS[inner] = new Point(dir.getOffsetX(), dir.getOffsetZ());
			dir = BlocksHelper.HORIZONTAL[(i + 1) & 3];
			OFFSETS[i + 12] = new Point(OFFSETS[inner].x + dir.getOffsetX(), OFFSETS[inner].y + dir.getOffsetZ());
		}
	}
}
