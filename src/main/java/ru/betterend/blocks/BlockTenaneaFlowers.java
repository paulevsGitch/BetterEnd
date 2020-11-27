package ru.betterend.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.basis.BlockVine;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.util.MHelper;

public class BlockTenaneaFlowers extends BlockVine implements IColorProvider {
	public static final Vec3i[] COLORS;
	
	public BlockTenaneaFlowers() {
		super(15);
	}

	@Override
	public BlockColorProvider getProvider() {
		return (state, world, pos, tintIndex) -> {
			long i = (MHelper.getRandom(pos.getX(), pos.getZ()) & 63) + pos.getY();
			double delta = i * 0.1;
			int index = MHelper.floor(delta);
			int index2 = (index + 1) & 3;
			delta -= index;
			index &= 3;
			
			Vec3i color1 = COLORS[index];
			Vec3i color2 = COLORS[index2];
			
			int r = MHelper.floor(MathHelper.lerp(delta, color1.getX(), color2.getX()));
			int g = MHelper.floor(MathHelper.lerp(delta, color1.getY(), color2.getY()));
			int b = MHelper.floor(MathHelper.lerp(delta, color1.getZ(), color2.getZ()));
			float[] hsb = MHelper.fromRGBtoHSB(r, g, b);
			
			return MHelper.fromHSBtoRGB(hsb[0], MHelper.max(0.5F, hsb[1]), hsb[2]);
		};
	}

	@Override
	public ItemColorProvider getItemProvider() {
		return (stack, tintIndex) -> {
			return MHelper.color(COLORS[0].getX(), COLORS[0].getY(), COLORS[0].getZ());
		};
	}
	
	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}
	
	static {
		COLORS = new Vec3i[] {
			new Vec3i(250, 111, 222),
			new Vec3i(167, 89, 255),
			new Vec3i(120, 207, 239),
			new Vec3i(255, 87, 182)
		};
	}
}
