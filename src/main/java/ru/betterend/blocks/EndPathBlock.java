package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.client.models.Patterns;

public class EndPathBlock extends BlockBaseNotFull {
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 15, 16);
	
	public EndPathBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).isValidSpawn((state, world, pos, type) -> { return false; }));
		if (source instanceof EndTerrainBlock) {
			EndTerrainBlock terrain = (EndTerrainBlock) source;
			terrain.setPathBlock(this);
		}
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
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE;
	}
	
	@Override
	public Optional<String> getModelString(String block) {
		String name = Registry.BLOCK.getKey(this).getPath();
		Map<String, String> map = Maps.newHashMap();
		map.put("%top%", name + "_top");
		map.put("%side%", name.replace("_path", "") + "_side");
		return Patterns.createJson(Patterns.BLOCK_PATH, map);
	}

}
