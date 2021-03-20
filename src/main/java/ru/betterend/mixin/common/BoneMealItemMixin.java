package ru.betterend.mixin.common;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.Category;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.BonemealUtil;
import ru.betterend.world.biome.EndBiome;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
	private static final Direction[] DIR = BlocksHelper.makeHorizontal();
	private static final Mutable POS = new Mutable();

	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void be_onUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		if (!world.isClient) {
			BlockPos offseted = blockPos.offset(context.getSide());
			boolean endBiome = world.getBiome(offseted).getCategory() == Category.THEEND;
			
			if (world.getBlockState(blockPos).isIn(EndTags.END_GROUND)) {
				boolean consume = false;
				if (world.getBlockState(blockPos).isOf(Blocks.END_STONE)) {
					BlockState nylium = beGetNylium(world, blockPos);
					if (nylium != null) {
						BlocksHelper.setWithoutUpdate(world, blockPos, nylium);
						consume = true;
					}
				}
				else {
					if (!world.getFluidState(offseted).isEmpty() && endBiome) {
						if (world.getBlockState(offseted).getBlock().equals(Blocks.WATER)) {
							consume = beGrowWaterGrass(world, blockPos);
						}
					}
					else {
						consume = beGrowGrass(world, blockPos);
					}
				}
				if (consume) {
					if (!context.getPlayer().isCreative()) {
						context.getStack().decrement(1);
					}
					world.syncWorldEvent(2005, blockPos, 0);
					info.setReturnValue(ActionResult.SUCCESS);
					info.cancel();
				}
			}
			else if (!world.getFluidState(offseted).isEmpty() && endBiome) {
				if (world.getBlockState(offseted).getBlock().equals(Blocks.WATER)) {
					info.setReturnValue(ActionResult.FAIL);
					info.cancel();
				}
			}
		}
	}
	
	private boolean beGrowGrass(World world, BlockPos pos) {
		int y1 = pos.getY() + 3;
		int y2 = pos.getY() - 3;
		boolean result = false;
		for (int i = 0; i < 64; i++) {
			int x = (int) (pos.getX() + world.random.nextGaussian() * 2);
			int z = (int) (pos.getZ() + world.random.nextGaussian() * 2);
			POS.setX(x);
			POS.setZ(z);
			for (int y = y1; y >= y2; y--) {
				POS.setY(y);
				BlockPos down = POS.down();
				if (world.isAir(POS) && !world.isAir(down)) {
					BlockState grass = beGetGrassState(world, down);
					if (grass != null) {
						BlocksHelper.setWithoutUpdate(world, POS, grass);
						result = true;
					}
					break;
				}
			}
		}
		return result;
	}
	
	private boolean beGrowWaterGrass(World world, BlockPos pos) {
		int y1 = pos.getY() + 3;
		int y2 = pos.getY() - 3;
		boolean result = false;
		for (int i = 0; i < 64; i++) {
			int x = (int) (pos.getX() + world.random.nextGaussian() * 2);
			int z = (int) (pos.getZ() + world.random.nextGaussian() * 2);
			POS.setX(x);
			POS.setZ(z);
			for (int y = y1; y >= y2; y--) {
				POS.setY(y);
				BlockPos down = POS.down();
				if (world.getBlockState(POS).isOf(Blocks.WATER) && world.getBlockState(down).isIn(EndTags.END_GROUND)) {
					BlockState grass = beGetWaterGrassState(world, down);
					if (grass != null) {
						BlocksHelper.setWithoutUpdate(world, POS, grass);
						result = true;
					}
					break;
				}
			}
		}
		return result;
	}
	
	private BlockState beGetGrassState(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		block = BonemealUtil.getGrass(EndBiomes.getBiomeID(world.getBiome(pos)), block, world.getRandom());
		return block == null ? null : block.getDefaultState();
	}
	
	private BlockState beGetWaterGrassState(World world, BlockPos pos) {
		EndBiome biome = EndBiomes.getFromBiome(world.getBiome(pos));
		if (world.random.nextInt(16) == 0) {
			return EndBlocks.CHARNIA_RED.getDefaultState();
		}
		else if (biome == EndBiomes.FOGGY_MUSHROOMLAND || biome == EndBiomes.MEGALAKE || biome == EndBiomes.MEGALAKE_GROVE) {
			return world.random.nextBoolean() ? EndBlocks.CHARNIA_LIGHT_BLUE.getDefaultState() : EndBlocks.CHARNIA_LIGHT_BLUE.getDefaultState();
		}
		else if (biome == EndBiomes.AMBER_LAND) {
			return world.random.nextBoolean() ? EndBlocks.CHARNIA_ORANGE.getDefaultState() : EndBlocks.CHARNIA_RED.getDefaultState();
		}
		else if (biome == EndBiomes.CHORUS_FOREST || biome == EndBiomes.SHADOW_FOREST) {
			return EndBlocks.CHARNIA_PURPLE.getDefaultState();
		}
		return null;
	}

	private void beShuffle(Random random) {
		for (int i = 0; i < 4; i++) {
			int j = random.nextInt(4);
			Direction d = DIR[i];
			DIR[i] = DIR[j];
			DIR[j] = d;
		}
	}

	private BlockState beGetNylium(World world, BlockPos pos) {
		beShuffle(world.random);
		for (Direction dir : DIR) {
			BlockState state = world.getBlockState(pos.offset(dir));
			if (BlocksHelper.isEndNylium(state))
				return state;
		}
		return null;
	}
}