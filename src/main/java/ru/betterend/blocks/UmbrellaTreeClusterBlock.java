package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import ru.bclib.blocks.BaseBlock;
import ru.bclib.blocks.BlockProperties;
import ru.bclib.util.BlocksHelper;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndItems;

public class UmbrellaTreeClusterBlock extends BaseBlock {
	public static final BooleanProperty NATURAL = BlockProperties.NATURAL;
	
	public UmbrellaTreeClusterBlock() {
		super(FabricBlockSettings.copyOf(Blocks.NETHER_WART_BLOCK)
				.materialColor(MaterialColor.COLOR_PURPLE)
				.luminance(15));
		registerDefaultState(stateDefinition.any().setValue(NATURAL, false));
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
		stateManager.add(NATURAL);
	}
	
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stack = player.getMainHandItem();
		if (stack.getItem() == Items.GLASS_BOTTLE) {
			if (!player.isCreative()) {
				stack.shrink(1);
			}
			stack = new ItemStack(EndItems.UMBRELLA_CLUSTER_JUICE);
			player.addItem(stack);
			world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1, 1, false);
			BlocksHelper.setWithUpdate(world, pos, EndBlocks.UMBRELLA_TREE_CLUSTER_EMPTY.defaultBlockState().setValue(NATURAL, state.getValue(NATURAL)));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}
}
