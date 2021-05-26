package ru.betterend.blocks.basis;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

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
	public BlockModel getItemModel(ResourceLocation blockId) {
		Optional<String> pattern = Patterns.createJson(Patterns.ITEM_CHEST, blockId.getPath());
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		ResourceLocation parentId = Registry.BLOCK.getKey(parent);
		return ModelsHelper.createBlockEmpty(parentId);
	}
}
