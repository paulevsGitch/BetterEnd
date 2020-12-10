package ru.betterend.blocks;

import java.util.Collections;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.sound.BlockSoundGroup;
import ru.betterend.blocks.basis.BlockBase;
import ru.betterend.registry.EndBlocks;

public class BlockHelixTreeLeaves extends BlockBase {
	public BlockHelixTreeLeaves() {
		super(FabricBlockSettings.of(Material.LEAVES)
				.strength(0.2F)
				.sounds(BlockSoundGroup.GRASS)
				.materialColor(MaterialColor.ORANGE));
	}
	
	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(EndBlocks.HELIX_TREE_SAPLING));
	}
}
