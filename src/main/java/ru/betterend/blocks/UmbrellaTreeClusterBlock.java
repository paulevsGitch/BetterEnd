package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;
import ru.betterend.util.BlocksHelper;

public class UmbrellaTreeClusterBlock extends BlockBase {
	public static final BooleanProperty NATURAL = BlockProperties.NATURAL;
	
	public UmbrellaTreeClusterBlock() {
		super(FabricBlockSettings.copyOf(Blocks.NETHER_WART_BLOCK)
				.materialColor(MaterialColor.PURPLE)
				.luminance(15));
		setDefaultState(stateManager.getDefaultState().with(NATURAL, false));
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		stateManager.add(NATURAL);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getMainHandStack();
		if (stack.getItem() == Items.GLASS_BOTTLE) {
			if (!player.isCreative()) {
				stack.decrement(1);
			}
			stack = new ItemStack(EndItems.UMBRELLA_CLUSTER_JUICE);
			player.giveItemStack(stack);
			world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1, 1, false);
			BlocksHelper.setWithUpdate(world, pos, EndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY.getDefaultState().with(NATURAL, state.get(NATURAL)));
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}
}
