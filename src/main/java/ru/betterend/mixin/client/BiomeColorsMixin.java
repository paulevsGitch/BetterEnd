package ru.betterend.mixin.client;

import java.awt.Point;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import ru.betterend.client.ClientOptions;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;

@Mixin(BiomeColors.class)
public class BiomeColorsMixin {
	private static final int POISON_COLOR = MHelper.color(92, 160, 78);
	private static final int STREAM_COLOR = MHelper.color(105, 213, 244);
	private static final Point[] OUTER_POINTS;
	private static final Point[] INNER_POINTS;
	private static final boolean HAS_SODIUM;
	
	@Inject(method = "getWaterColor", at = @At("RETURN"), cancellable = true)
	private static void be_getWaterColor(BlockRenderView world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
		if (ClientOptions.useSulfurWaterColor()) {
			BlockRenderView view = HAS_SODIUM ? MinecraftClient.getInstance().world : world;
			Mutable mut = new Mutable();
			mut.setY(pos.getY());
			
			for (Point offset: INNER_POINTS) {
				mut.setX(pos.getX() + offset.x);
				mut.setZ(pos.getZ() + offset.y);
				if ((view.getBlockState(mut).isOf(EndBlocks.BRIMSTONE))) {
					info.setReturnValue(POISON_COLOR);
					info.cancel();
					return;
				}
			}
			
			for (Point offset: OUTER_POINTS) {
				mut.setX(pos.getX() + offset.x);
				mut.setZ(pos.getZ() + offset.y);
				if ((view.getBlockState(mut).isOf(EndBlocks.BRIMSTONE))) {
					info.setReturnValue(STREAM_COLOR);
					info.cancel();
					return;
				}
			}
		}
	}
	
	static {
		HAS_SODIUM = FabricLoader.getInstance().isModLoaded("sodium");
		
		OUTER_POINTS = new Point[16];
		for (int i = 0; i < 3; i++) {
			int p = i - 1;
			OUTER_POINTS[i] = new Point(p, -2);
			OUTER_POINTS[i + 3] = new Point(p, 2);
			OUTER_POINTS[i + 6] = new Point(-2, p);
			OUTER_POINTS[i + 9] = new Point(2, p);
		}
		
		INNER_POINTS = new Point[4];
		for (int i = 0; i < 4; i++) {
			Direction dir = BlocksHelper.HORIZONTAL[i];
			INNER_POINTS[i] = new Point(dir.getOffsetX(), dir.getOffsetZ());
			dir = BlocksHelper.HORIZONTAL[(i + 1) & 3];
			OUTER_POINTS[i + 12] = new Point(INNER_POINTS[i].x + dir.getOffsetX(), INNER_POINTS[i].y + dir.getOffsetZ());
		}
	}
}
