package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEngine;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.util.BlocksHelper;

public class MossyObsidian extends BaseBlock {
	public MossyObsidian() {
		super(BlocksHelper.copySettingsOf(Blocks.OBSIDIAN).hardness(3).randomTicks());
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Collections.singletonList(new ItemStack(this));
		}
		return Collections.singletonList(new ItemStack(Blocks.OBSIDIAN));
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (random.nextInt(16) == 0 && !canSurvive(state, world, pos)) {
			world.setBlockAndUpdate(pos, Blocks.OBSIDIAN.defaultBlockState());
		}
	}
	
	public boolean canSurvive(BlockState state, LevelReader worldView, BlockPos pos) {
	      BlockPos blockPos = pos.above();
	      BlockState blockState = worldView.getBlockState(blockPos);
	      if (blockState.is(Blocks.SNOW) && (Integer)blockState.getValue(SnowLayerBlock.LAYERS) == 1) {
	         return true;
	      } else if (blockState.getFluidState().getAmount() == 8) {
	         return false;
	      } else {
	         int i = LayerLightEngine.getLightBlockInto(worldView, state, pos, blockState, blockPos, Direction.UP, blockState.getLightBlock(worldView, blockPos));
	         return i < 5;
	      }
	   }
}
