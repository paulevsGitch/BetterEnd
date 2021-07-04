package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.ColorUtil;

public class EndstoneDustBlock extends FallingBlock {
	@Environment(EnvType.CLIENT)
	private static final int COLOR = ColorUtil.color(226, 239, 168);
	
	public EndstoneDustBlock() {
		super(BlocksHelper.copySettingsOf(Blocks.SAND)
				.breakByTool(FabricToolTags.SHOVELS)
				.materialColor(Blocks.END_STONE.defaultMaterialColor()));
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}

	@Environment(EnvType.CLIENT)
	public int getDustColor(BlockState state, BlockGetter world, BlockPos pos) {
		return COLOR;
	}
}
