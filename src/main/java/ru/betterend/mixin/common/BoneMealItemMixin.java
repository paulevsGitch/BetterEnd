package ru.betterend.mixin.common;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.Category;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.BonemealUtil;
import ru.betterend.world.biome.EndBiome;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
	private static final Direction[] DIR = BlocksHelper.makeHorizontal();
	private static final MutableBlockPos POS = new MutableBlockPos();

	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void be_onUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
		Level world = context.getLevel();
		BlockPos blockPos = context.getBlockPos();
		if (!world.isClientSide) {
			BlockPos offseted = blockPos.offset(context.getSide());
			boolean endBiome = world.getBiome(offseted).getCategory() == Category.THEEND;

			if (world.getBlockState(blockPos).isIn(EndTags.END_GROUND)) {
				boolean consume = false;
				if (world.getBlockState(blockPos).is(Blocks.END_STONE)) {
					BlockState nylium = beGetNylium(world, blockPos);
					if (nylium != null) {
						BlocksHelper.setWithoutUpdate(world, blockPos, nylium);
						consume = true;
					}
				} else {
					if (!world.getFluidState(offseted).isEmpty() && endBiome) {
						if (world.getBlockState(offseted).getBlock().equals(Blocks.WATER)) {
							consume = beGrowWaterGrass(world, blockPos);
						}
					} else {
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
			} else if (!world.getFluidState(offseted).isEmpty() && endBiome) {
				if (world.getBlockState(offseted).getBlock().equals(Blocks.WATER)) {
					info.setReturnValue(ActionResult.FAIL);
					info.cancel();
				}
			}
		}
	}

	private boolean beGrowGrass(Level world, BlockPos pos) {
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
				BlockPos down = POS.below();
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

	private boolean beGrowWaterGrass(Level world, BlockPos pos) {
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
				BlockPos down = POS.below();
				if (world.getBlockState(POS).is(Blocks.WATER) && world.getBlockState(down).isIn(EndTags.END_GROUND)) {
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

	private BlockState beGetGrassState(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		block = BonemealUtil.getGrass(EndBiomes.getBiomeID(world.getBiome(pos)), block, world.getRandom());
		return block == null ? null : block.defaultBlockState();
	}

	private BlockState beGetWaterGrassState(Level world, BlockPos pos) {
		EndBiome biome = EndBiomes.getFromBiome(world.getBiome(pos));
		if (world.random.nextInt(16) == 0) {
			return EndBlocks.CHARNIA_RED.defaultBlockState();
		} else if (biome == EndBiomes.FOGGY_MUSHROOMLAND || biome == EndBiomes.MEGALAKE
				|| biome == EndBiomes.MEGALAKE_GROVE) {
			return world.random.nextBoolean() ? EndBlocks.CHARNIA_LIGHT_BLUE.defaultBlockState()
					: EndBlocks.CHARNIA_LIGHT_BLUE.defaultBlockState();
		} else if (biome == EndBiomes.AMBER_LAND) {
			return world.random.nextBoolean() ? EndBlocks.CHARNIA_ORANGE.defaultBlockState()
					: EndBlocks.CHARNIA_RED.defaultBlockState();
		} else if (biome == EndBiomes.CHORUS_FOREST || biome == EndBiomes.SHADOW_FOREST) {
			return EndBlocks.CHARNIA_PURPLE.defaultBlockState();
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

	private BlockState beGetNylium(Level world, BlockPos pos) {
		beShuffle(world.random);
		for (Direction dir : DIR) {
			BlockState state = world.getBlockState(pos.relative(dir));
			if (BlocksHelper.isEndNylium(state))
				return state;
		}
		return null;
	}
}