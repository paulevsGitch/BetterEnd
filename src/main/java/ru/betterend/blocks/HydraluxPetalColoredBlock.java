package ru.betterend.blocks;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.models.Patterns;
import ru.betterend.interfaces.IColorProvider;
import ru.betterend.util.BlocksHelper;

public class HydraluxPetalColoredBlock extends HydraluxPetalBlock implements IColorProvider {
	public HydraluxPetalColoredBlock(FabricBlockSettings settings) {
		super(settings);
	}
	
	@Override
	public BlockColor getProvider() {
		return (state, world, pos, tintIndex) -> {
			return BlocksHelper.getBlockColor(this);
		};
	}

	@Override
	public ItemColor getItemProvider() {
		return (stack, tintIndex) -> {
			return BlocksHelper.getBlockColor(this);
		};
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		String path = "betterend:block/block_petal_colored";
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_PETAL_COLORED, path, path);
		return ModelsHelper.fromPattern(pattern);
	}
}
