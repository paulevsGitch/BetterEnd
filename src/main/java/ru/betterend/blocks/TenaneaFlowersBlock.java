package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import ru.bclib.blocks.BaseVineBlock;
import ru.bclib.interfaces.CustomColorProvider;
import ru.bclib.util.ColorUtil;
import ru.bclib.util.MHelper;
import ru.betterend.registry.EndParticles;

import java.util.Random;

public class TenaneaFlowersBlock extends BaseVineBlock implements CustomColorProvider {
	public static final Vec3i[] COLORS;
	
	public TenaneaFlowersBlock() {
		super(15);
	}
	
	@Override
	public BlockColor getProvider() {
		return (state, world, pos, tintIndex) -> {
			if (pos == null) {
				pos = BlockPos.ZERO;
			}
			;
			long i = (MHelper.getRandom(pos.getX(), pos.getZ()) & 63) + pos.getY();
			double delta = i * 0.1;
			int index = MHelper.floor(delta);
			int index2 = (index + 1) & 3;
			delta -= index;
			index &= 3;
			
			Vec3i color1 = COLORS[index];
			Vec3i color2 = COLORS[index2];
			
			int r = MHelper.floor(Mth.lerp(delta, color1.getX(), color2.getX()));
			int g = MHelper.floor(Mth.lerp(delta, color1.getY(), color2.getY()));
			int b = MHelper.floor(Mth.lerp(delta, color1.getZ(), color2.getZ()));
			float[] hsb = ColorUtil.RGBtoHSB(r, g, b, new float[3]);
			
			return ColorUtil.HSBtoRGB(hsb[0], MHelper.max(0.5F, hsb[1]), hsb[2]);
		};
	}
	
	@Override
	public ItemColor getItemProvider() {
		return (stack, tintIndex) -> ColorUtil.color(255, 255, 255);
	}
	
	@Override
	public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
		return false;
	}
	
	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
		super.animateTick(state, world, pos, random);
		if (random.nextInt(32) == 0) {
			double x = (double) pos.getX() + random.nextGaussian() + 0.5;
			double z = (double) pos.getZ() + random.nextGaussian() + 0.5;
			double y = (double) pos.getY() + random.nextDouble();
			world.addParticle(EndParticles.TENANEA_PETAL, x, y, z, 0, 0, 0);
		}
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
