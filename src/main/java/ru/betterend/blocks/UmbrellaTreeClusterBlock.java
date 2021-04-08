package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.player.PlayerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.util.BlocksHelper;

public class UmbrellaTreeClusterBlock extends BlockBase {
	public static final BooleanProperty NATURAL = BlockProperties.NATURAL;

	public UmbrellaTreeClusterBlock() {
		super(FabricBlockSettings.copyOf(Blocks.NETHER_WART_BLOCK).materialColor(MaterialColor.COLOR_PURPLE)
				.luminance(15));
		setDefaultState(stateManager.defaultBlockState().with(NATURAL, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(NATURAL);
	}

	@Override
	public ActionResult onUse(BlockState state, Level world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		ItemStack stack = player.getMainHandStack();
		if (stack.getItem() == Items.GLASS_BOTTLE) {
			if (!player.isCreative()) {
				stack.decrement(1);
			}
			stack = new ItemStack(EndItems.UMBRELLA_CLUSTER_JUICE);
			player.giveItemStack(stack);
			world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ITEM_BOTTLE_FILL,
					SoundSource.BLOCKS, 1, 1, false);
			BlocksHelper.setWithUpdate(world, pos,
					EndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY.defaultBlockState().with(NATURAL, state.getValue(NATURAL)));
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}
}
