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
import ru.betterend.registry.BlockRegistry;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.util.BlocksHelper;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {
	private static final Direction[] DIR = BlocksHelper.makeHorizontal();
	private static final Mutable POS = new Mutable();

	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void onUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
		World world = context.getWorld();
		BlockPos blockPos = context.getBlockPos();
		if (!world.isClient) {
			if (world.getBlockState(blockPos).isIn(BlockTagRegistry.END_GROUND)) {
				boolean consume = false;
				if (world.getBlockState(blockPos).getBlock() == Blocks.END_STONE) {
					BlockState nylium = getNylium(world, blockPos);
					if (nylium != null) {
						BlocksHelper.setWithoutUpdate(world, blockPos, nylium);
						consume = true;
					}
				}
				else {
					consume = growGrass(world, blockPos);
				}
				if (consume) {
					if (!context.getPlayer().isCreative())
						context.getStack().decrement(1);
					world.syncWorldEvent(2005, blockPos, 0);
					info.setReturnValue(ActionResult.SUCCESS);
					info.cancel();
				}
			}
		}
	}
	
	private boolean growGrass(World world, BlockPos pos) {
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
					BlockState grass = getGrassState(world, down);
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
	
	private BlockState getGrassState(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block == BlockRegistry.END_MOSS || block == BlockRegistry.END_MYCELIUM) {
			return world.random.nextBoolean() ? BlockRegistry.CREEPING_MOSS.getDefaultState() : BlockRegistry.UMBRELLA_MOSS.getDefaultState();
		}
		else if (block == BlockRegistry.CHORUS_NYLIUM) {
			return BlockRegistry.CHORUS_GRASS.getDefaultState();
		}
		return null;
	}

	private void shuffle(Random random) {
		for (int i = 0; i < 4; i++) {
			int j = random.nextInt(4);
			Direction d = DIR[i];
			DIR[i] = DIR[j];
			DIR[j] = d;
		}
	}

	private BlockState getNylium(World world, BlockPos pos) {
		shuffle(world.random);
		for (Direction dir : DIR) {
			BlockState state = world.getBlockState(pos.offset(dir));
			if (BlocksHelper.isEndNylium(state))
				return state;
		}
		return null;
	}
}