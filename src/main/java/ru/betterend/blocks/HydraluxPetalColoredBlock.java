package ru.betterend.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.interfaces.IColorProvider;
import ru.bclib.util.BlocksHelper;
import ru.betterend.client.models.Patterns;

import java.util.Optional;

public class HydraluxPetalColoredBlock extends HydraluxPetalBlock implements IColorProvider {
	public HydraluxPetalColoredBlock(FabricBlockSettings settings) {
		super(settings);
	}
	
	@Override
	public BlockColor getProvider() {
		return (state, world, pos, tintIndex) -> BlocksHelper.getBlockColor(this);
	}
	
	@Override
	public ItemColor getItemProvider() {
		return (stack, tintIndex) -> BlocksHelper.getBlockColor(this);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		String path = "betterend:block/block_petal_colored";
		Optional<String> pattern = Patterns.createJson(Patterns.BLOCK_PETAL_COLORED, path, path);
		return ModelsHelper.fromPattern(pattern);
	}
}
