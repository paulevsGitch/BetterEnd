package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockBase;

public class BlockTerrain extends BlockBase {
	private Block pathBlock;
	
	public BlockTerrain(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.END_STONE).materialColor(color).sounds(BlockSounds.TERRAIN_SOUND));
	}
	
	public void setPathBlock(Block roadBlock) {
		this.pathBlock = roadBlock;
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (pathBlock != null && player.getMainHandStack().getItem().isIn(FabricToolTags.SHOVELS)) {
			world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
			if (!world.isClient) {
				world.setBlockState(pos, pathBlock.getDefaultState());
				if (player != null && !player.isCreative()) {
					player.getMainHandStack().damage(1, world.random, (ServerPlayerEntity) player);
				}
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootContextParameters.TOOL);
		if (tool != null && EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Collections.singletonList(new ItemStack(this));
		}
		return Collections.singletonList(new ItemStack(Blocks.END_STONE));
	}
}
