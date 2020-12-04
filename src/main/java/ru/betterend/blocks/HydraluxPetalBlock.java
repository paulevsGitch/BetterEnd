package ru.betterend.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockBase;

public class HydraluxPetalBlock extends BlockBase {
	public HydraluxPetalBlock() {
		super(FabricBlockSettings.of(Material.PLANT).hardness(1).resistance(1).breakByHand(true));
	}
	
	@Override
	public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance) {}
}
