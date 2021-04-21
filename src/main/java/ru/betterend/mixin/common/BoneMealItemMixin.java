package ru.betterend.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.registry.EndBiomes;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.BonemealUtil;
import ru.betterend.util.MHelper;
import ru.betterend.world.biome.EndBiome;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
	private static final MutableBlockPos POS = new MutableBlockPos();
	private static final Vec3i[] DIR;

	@Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
	private void be_onUse(UseOnContext context, CallbackInfoReturnable<InteractionResult> info) {
		Level world = context.getLevel();
		BlockPos blockPos = context.getClickedPos();
		if (!world.isClientSide) {
			BlockPos offseted = blockPos.relative(context.getClickedFace());
			boolean endBiome = world.getBiome(offseted).getBiomeCategory() == BiomeCategory.THEEND;
			
			if (world.getBlockState(blockPos).is(EndTags.END_GROUND)) {
				boolean consume = false;
				if (world.getBlockState(blockPos).is(Blocks.END_STONE)) {
					BlockState nylium = be_getNylium(world, blockPos);
					if (nylium != null) {
						BlocksHelper.setWithoutUpdate(world, blockPos, nylium);
						consume = true;
					}
				}
				else {
					if (!world.getFluidState(offseted).isEmpty() && endBiome) {
						if (world.getBlockState(offseted).getBlock().equals(Blocks.WATER)) {
							consume = be_growWaterGrass(world, blockPos);
						}
					}
					else {
						consume = be_growGrass(world, blockPos);
					}
				}
				if (consume) {
					if (!context.getPlayer().isCreative()) {
						context.getItemInHand().shrink(1);
					}
					world.levelEvent(2005, blockPos, 0);
					info.setReturnValue(InteractionResult.SUCCESS);
					info.cancel();
				}
			}
			else if (!world.getFluidState(offseted).isEmpty() && endBiome) {
				if (world.getBlockState(offseted).getBlock().equals(Blocks.WATER)) {
					info.setReturnValue(InteractionResult.FAIL);
					info.cancel();
				}
			}
		}
	}
	
	private boolean be_growGrass(Level world, BlockPos pos) {
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
				if (world.isEmptyBlock(POS) && !world.isEmptyBlock(down)) {
					BlockState grass = be_getGrassState(world, down);
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
	
	private boolean be_growWaterGrass(Level world, BlockPos pos) {
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
				if (world.getBlockState(POS).is(Blocks.WATER) && world.getBlockState(down).is(EndTags.END_GROUND)) {
					BlockState grass = be_getWaterGrassState(world, down);
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
	
	private BlockState be_getGrassState(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		block = BonemealUtil.getGrass(EndBiomes.getBiomeID(world.getBiome(pos)), block, world.getRandom());
		return block == null ? null : block.defaultBlockState();
	}
	
	private BlockState be_getWaterGrassState(Level world, BlockPos pos) {
		EndBiome biome = EndBiomes.getFromBiome(world.getBiome(pos));
		if (world.random.nextInt(16) == 0) {
			return EndBlocks.CHARNIA_RED.defaultBlockState();
		}
		else if (biome == EndBiomes.FOGGY_MUSHROOMLAND || biome == EndBiomes.MEGALAKE || biome == EndBiomes.MEGALAKE_GROVE) {
			return world.random.nextBoolean() ? EndBlocks.CHARNIA_LIGHT_BLUE.defaultBlockState() : EndBlocks.CHARNIA_LIGHT_BLUE.defaultBlockState();
		}
		else if (biome == EndBiomes.AMBER_LAND) {
			return world.random.nextBoolean() ? EndBlocks.CHARNIA_ORANGE.defaultBlockState() : EndBlocks.CHARNIA_RED.defaultBlockState();
		}
		else if (biome == EndBiomes.CHORUS_FOREST || biome == EndBiomes.SHADOW_FOREST) {
			return EndBlocks.CHARNIA_PURPLE.defaultBlockState();
		}
		return null;
	}

	private BlockState be_getNylium(Level world, BlockPos pos) {
		MHelper.shuffle(DIR, world.getRandom());
		for (Vec3i dir : DIR) {
			BlockPos p = pos.offset(dir);
			BlockState state = world.getBlockState(p);
			if (BlocksHelper.isEndNylium(state) && !world.getBlockState(p.above()).is(EndTags.END_GROUND)) {
				return state;
			}
		}
		return null;
	}
	
	static {
		int index = 0;
		DIR = new Vec3i[3 * 3 * 3 - 1];
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					if (x != 0 || y != 0 || z != 0) {
						DIR[index++] = new Vec3i(x, y, z);
					}
				}
			}
		}
	}
}