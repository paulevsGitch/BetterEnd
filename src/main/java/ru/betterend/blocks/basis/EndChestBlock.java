package ru.betterend.blocks.basis;

import java.util.List;
import java.util.Optional;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;
import ru.betterend.registry.EndBlockEntities;

public class EndChestBlock extends ChestBlock implements BlockModelProvider {
	private final Block parent;
	
	public EndChestBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).noOcclusion(), () -> EndBlockEntities.CHEST);
		this.parent = source;
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockGetter world)
	{
		return EndBlockEntities.CHEST.create();
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
	{
		List<ItemStack> drop = super.getDrops(state, builder);
		drop.add(new ItemStack(this.asItem()));
		return drop;
	}

	@Override
	public Optional<String> getModelString(String path) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		if (path.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_CHEST, blockId.getPath());
		}
		return Patterns.createJson(Patterns.BLOCK_EMPTY, parentId.getPath());
	}

	@Override
	public BlockModel getModel(ResourceLocation blockId) {
		Optional<String> pattern = Patterns.createJson(Patterns.ITEM_CHEST, blockId.getPath());
		return pattern.map(BlockModel::fromString).orElse(null);
	}

	@Override
	public BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		return ModelsHelper.createBlockEmpty(parentId);
	}
}
