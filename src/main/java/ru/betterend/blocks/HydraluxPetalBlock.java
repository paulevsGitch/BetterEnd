package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import ru.betterend.blocks.basis.BlockBase;

public class HydraluxPetalBlock extends BlockBase {
	public HydraluxPetalBlock() {
		this(FabricBlockSettings.of(Material.PLANT).materialColor(MaterialColor.SPRUCE).sounds(SoundType.WART_BLOCK)
				.breakByTool(FabricToolTags.AXES).hardness(1).resistance(1).breakByHand(true));
	}

	public HydraluxPetalBlock(FabricBlockSettings settings) {
		super(settings);
	}

	@Override
	public void onLandedUpon(Level world, BlockPos pos, Entity entity, float distance) {
	}
}
