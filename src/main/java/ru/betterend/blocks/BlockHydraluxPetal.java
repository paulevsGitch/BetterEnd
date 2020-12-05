package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockBase;

public class BlockHydraluxPetal extends BlockBase {
	public BlockHydraluxPetal() {
		this(FabricBlockSettings.of(Material.PLANT)
				.materialColor(MaterialColor.SPRUCE)
				.sounds(BlockSoundGroup.WART_BLOCK)
				.breakByTool(FabricToolTags.AXES)
				.hardness(1)
				.resistance(1)
				.breakByHand(true));
	}
	
	public BlockHydraluxPetal(FabricBlockSettings settings) {
		super(settings);
	}
	
	@Override
	public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance) {}
}
