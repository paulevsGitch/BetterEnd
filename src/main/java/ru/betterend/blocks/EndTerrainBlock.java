package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.block.SnowBlock;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.world.level.Level;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.patterns.Patterns;

public class EndTerrainBlock extends BlockBase {
	private Block pathBlock;

	public EndTerrainBlock(MaterialColor color) {
		super(FabricBlockSettings.copyOf(Blocks.END_STONE).materialColor(color).sounds(BlockSounds.TERRAIN_SOUND)
				.ticksRandomly());
	}

	public void setPathBlock(Block roadBlock) {
		this.pathBlock = roadBlock;
	}

	@Override
	public ActionResult onUse(BlockState state, Level world, BlockPos pos, Player player, Hand hand,
			BlockHitResult hit) {
		if (pathBlock != null && player.getMainHandStack().getItem().isIn(FabricToolTags.SHOVELS)) {
			world.playLocalSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!world.isClientSide) {
				world.setBlockAndUpdate(pos, pathBlock.defaultBlockState());
				if (player != null && !player.isCreative()) {
					player.getMainHandStack().damage(1, world.random, (ServerPlayer) player);
				}
			}
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getParameter(LootContextParams.TOOL);
		if (tool != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Collections.singletonList(new ItemStack(this));
		}
		return Collections.singletonList(new ItemStack(Blocks.END_STONE));
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (random.nextInt(16) == 0 && !canSurvive(state, world, pos)) {
			world.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
		}
	}

	protected boolean canSurvive(BlockState state, WorldView worldView, BlockPos pos) {
		BlockPos blockPos = pos.up();
		BlockState blockState = worldView.getBlockState(blockPos);
		if (blockState.is(Blocks.SNOW) && (Integer) blockState.get(SnowBlock.LAYERS) == 1) {
			return true;
		} else if (blockState.getFluidState().getLevel() == 8) {
			return false;
		} else {
			int i = ChunkLightProvider.getRealisticOpacity(worldView, state, pos, blockState, blockPos, Direction.UP,
					blockState.getOpacity(worldView, blockPos));
			return i < 5;
		}
	}

	@Override
	public String getModelPattern(String block) {
		String name = Registry.BLOCK.getKey(this).getPath();
		Map<String, String> map = Maps.newHashMap();
		map.put("%top%", "betterend:block/" + name + "_top");
		map.put("%side%", "betterend:block/" + name + "_side");
		map.put("%bottom%", "minecraft:block/end_stone");
		return Patterns.createJson(Patterns.BLOCK_TOP_SIDE_BOTTOM, map);
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_ROTATED_TOP;
	}
}
