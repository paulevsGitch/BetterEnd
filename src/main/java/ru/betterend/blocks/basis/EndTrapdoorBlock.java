package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.TrapdoorBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndTrapdoorBlock extends TrapdoorBlock implements IRenderTypeable, BlockPatterned {
	public EndTrapdoorBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).strength(3.0F, 3.0F).nonOpaque());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}

	@Override
	public String getStatesPattern(Reader data) {
		String block = Registry.BLOCK.getKey(this).getPath();
		return Patterns.createJson(data, block, block);
	}

	@Override
	public String getModelPattern(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		String name = blockId.getPath();
		return Patterns.createJson(Patterns.BLOCK_TRAPDOOR, new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("%block%", name);
				put("%texture%", name.replace("trapdoor", "door_side"));
			}
		});
	}

	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_TRAPDOOR;
	}
}
