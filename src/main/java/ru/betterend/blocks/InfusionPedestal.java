package ru.betterend.blocks;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import ru.betterend.blocks.basis.BlockPedestal;
import ru.betterend.blocks.entities.InfusionPedestalEntity;

public class InfusionPedestal extends BlockPedestal {

	public InfusionPedestal() {
		super(Blocks.OBSIDIAN);
		this.height = 1.1F;
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new InfusionPedestalEntity();
	}

}
