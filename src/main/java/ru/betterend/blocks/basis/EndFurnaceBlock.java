package ru.betterend.blocks.basis;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.Nullable;
import ru.betterend.blocks.entities.EFurnaceBlockEntity;
import ru.betterend.client.models.ModelsHelper;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.client.models.BlockModelProvider;
import ru.betterend.client.models.Patterns;

public class EndFurnaceBlock extends FurnaceBlock implements BlockModelProvider, IRenderTypeable {
	public EndFurnaceBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).luminance(state -> state.getValue(LIT) ? 13 : 0));
	}

	@Override
	public BlockEntity newBlockEntity(BlockGetter world) {
		return new EFurnaceBlockEntity();
	}
	
	@Override
	protected void openContainer(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EFurnaceBlockEntity) {
			player.openMenu((MenuProvider) blockEntity);
			player.awardStat(Stats.INTERACT_WITH_FURNACE);
		}
	}

	@Override
	public @Nullable BlockModel getBlockModel(ResourceLocation blockId, BlockState blockState) {
		String blockName = blockId.getPath();
		Map<String, String> textures = Maps.newHashMap();
		textures.put("%top%", blockName + "_top");
		textures.put("%side%", blockName + "_side");
		Optional<String> pattern;
		if (blockState.getValue(LIT)) {
			textures.put("%front%", blockName + "_front_on");
			textures.put("%glow%", blockName + "_glow");
			pattern = Patterns.createJson(Patterns.BLOCK_FURNACE_LIT, textures);
		} else {
			textures.put("%front%", blockName + "_front");
			pattern = Patterns.createJson(Patterns.BLOCK_FURNACE, textures);
		}
		return ModelsHelper.fromPattern(pattern);
	}

	@Override
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return getBlockModel(resourceLocation, defaultBlockState());
	}

	@Override
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		String lit = blockState.getValue(LIT) ? "_lit" : "";
		ResourceLocation modelId = new ResourceLocation(stateId.getNamespace(),
				"block/" + stateId.getPath() + lit);
		registerBlockModel(stateId, modelId, blockState, modelCache);
		return ModelsHelper.createFacingModel(modelId, blockState.getValue(FACING), false, true);
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = Lists.newArrayList(new ItemStack(this));
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof EFurnaceBlockEntity) {
			EFurnaceBlockEntity entity = (EFurnaceBlockEntity) blockEntity;
			for (int i = 0; i < entity.getContainerSize(); i++) {
				drop.add(entity.getItem(i));
			}
		}
		return drop;
	}
}
