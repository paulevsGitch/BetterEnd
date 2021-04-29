package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import ru.betterend.blocks.basis.BlockBase;

public class HydraluxPetalBlock extends BlockBase {
	public HydraluxPetalBlock() {
		this(FabricBlockSettings.of(Material.PLANT)
				.breakByTool(FabricToolTags.AXES)
				.breakByHand(true)
				.hardness(1)
				.resistance(1)
				.materialColor(MaterialColor.PODZOL)
				.sound(SoundType.WART_BLOCK));
	}
	
	public HydraluxPetalBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public void fallOn(Level world, BlockPos pos, Entity entity, float distance) {}
}
