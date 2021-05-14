package ru.betterend.blocks;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockModelProvider;
import ru.betterend.patterns.Patterns;

public class EmeraldIceBlock extends HalfTransparentBlock implements IRenderTypeable, BlockModelProvider {
	public EmeraldIceBlock() {
		super(FabricBlockSettings.copyOf(Blocks.ICE));
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.TRANSLUCENT;
	}

	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
		super.playerDestroy(world, player, pos, state, blockEntity, stack);
		if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
			if (world.dimensionType().ultraWarm()) {
				world.removeBlock(pos, false);
				return;
			}

			Material material = world.getBlockState(pos.below()).getMaterial();
			if (material.blocksMotion() || material.isLiquid()) {
				world.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
			}
		}

	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (world.getBrightness(LightLayer.BLOCK, pos) > 11 - state.getLightBlock(world, pos)) {
			this.melt(state, world, pos);
		}

	}

	protected void melt(BlockState state, Level world, BlockPos pos) {
		if (world.dimensionType().ultraWarm()) {
			world.removeBlock(pos, false);
		}
		else {
			world.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
			world.neighborChanged(pos, Blocks.WATER, pos);
		}
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getOptionalParameter(LootContextParams.TOOL);
		if (tool != null && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) != 0) {
			return Collections.singletonList(new ItemStack(this));
		}
		else {
			return Collections.emptyList();
		}
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, block, block);
	}
	
	@Override
	public String getModelString(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(Patterns.BLOCK_BASE, blockId.getPath(), block);
	}
	
	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_SIMPLE;
	}
}
