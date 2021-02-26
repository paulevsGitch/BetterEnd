package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import ru.betterend.blocks.basis.BlockBase;

public class MossyObsidian extends BlockBase {
	public MossyObsidian() {
		super(FabricBlockSettings.copyOf(Blocks.OBSIDIAN).hardness(3).ticksRandomly());
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		if (tool != null && EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Collections.singletonList(new ItemStack(this));
		}
		return Collections.singletonList(new ItemStack(Blocks.OBSIDIAN));
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (random.nextInt(16) == 0 && !canSurvive(state, world, pos)) {
			world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
		}
	}
	
	public static boolean canSurvive(BlockState state, WorldView worldView, BlockPos pos) {
	      BlockPos blockPos = pos.up();
	      BlockState blockState = worldView.getBlockState(blockPos);
	      if (blockState.isOf(Blocks.SNOW) && (Integer)blockState.get(SnowBlock.LAYERS) == 1) {
	         return true;
	      } else if (blockState.getFluidState().getLevel() == 8) {
	         return false;
	      } else {
	         int i = ChunkLightProvider.getRealisticOpacity(worldView, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(worldView, blockPos));
	         return i < 5;
	      }
	   }
}
