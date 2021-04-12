package ru.betterend.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.explosion.Explosion;

public class EternalRunedFlavolite extends RunedFlavolite {

	@Override
	public float calcBlockBreakingDelta(BlockState state, Player player, BlockView world, BlockPos pos) {
		return 0.0F;
	}

	@Override
	public float getBlastResistance() {
		return Blocks.BEDROCK.getExplosionResistance();
	}

	@Override
	public boolean shouldDropItemsOnExplosion(Explosion explosion) {
		return false;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList();
	}
}
