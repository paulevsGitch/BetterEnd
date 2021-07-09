package ru.betterend.mixin.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.ColorUtil;
import ru.bclib.util.MHelper;
import ru.betterend.client.ClientOptions;
import ru.betterend.registry.EndBlocks;

import java.awt.Point;
import java.util.Arrays;
import java.util.Comparator;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
	private static final int POISON_COLOR = ColorUtil.color(92, 160, 78);
	private static final int STREAM_COLOR = ColorUtil.color(105, 213, 244);
	private static final Point[] OFFSETS;
	private static final boolean HAS_SODIUM;

	@Inject(method = "getAverageWaterColor", at = @At("RETURN"), cancellable = true)
	private static void be_getWaterColor(BlockAndTintGetter world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
		if (ClientOptions.useSulfurWaterColor()) {
			BlockAndTintGetter view = HAS_SODIUM ? Minecraft.getInstance().level : world;
			MutableBlockPos mut = new MutableBlockPos();
			mut.setY(pos.getY());
			for (int i = 0; i < OFFSETS.length; i++) {
				mut.setX(pos.getX() + OFFSETS[i].x);
				mut.setZ(pos.getZ() + OFFSETS[i].y);
				if ((view.getBlockState(mut).is(EndBlocks.BRIMSTONE))) {
					info.setReturnValue(i < 4 ? POISON_COLOR : STREAM_COLOR);
					return;
				}
			}
		}
	}

	static {
		HAS_SODIUM = FabricLoader.getInstance().isModLoaded("sodium");

		int index = 0;
		OFFSETS = new Point[20];
		for (int x = -2; x < 3; x++) {
			for (int z = -2; z < 3; z++) {
				if ((x != 0 || z != 0) && (Math.abs(x) != 2 || Math.abs(z) != 2)) {
					OFFSETS[index++] = new Point(x, z);
				}
			}
		}
		Arrays.sort(OFFSETS, Comparator.comparingInt(pos -> MHelper.sqr(pos.x) + MHelper.sqr(pos.y)));
	}
}
