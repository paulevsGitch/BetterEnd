package ru.betterend.blocks;

import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import ru.betterend.blocks.basis.BlockVine;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.util.MHelper;

public class BlockTenaneaFlowers extends BlockVine implements IColorProvider {
	private static final BlockColorProvider BLOCK_PROVIDER;
	private static final ItemColorProvider ITEM_PROVIDER;
	public static final Vec3i[] COLORS;
	
	public BlockTenaneaFlowers() {
		super(15);
	}

	@Override
	public BlockColorProvider getProvider() {
		return BLOCK_PROVIDER;
	}

	@Override
	public ItemColorProvider getItemProvider() {
		return ITEM_PROVIDER;
	}
	
	static {
		COLORS = new Vec3i[] {
			new Vec3i(250, 111, 222),
			new Vec3i(167, 89, 255),
			new Vec3i(120, 207, 239),
			new Vec3i(255, 87, 182)
		};
		
		BLOCK_PROVIDER = (state, world, pos, tintIndex) -> {
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
			
			return MHelper.color(r, g, b);
		};
		
		ITEM_PROVIDER = (stack, tintIndex) -> {
			return MHelper.color(COLORS[0].getX(), COLORS[0].getY(), COLORS[0].getZ());
		};
	}
}
