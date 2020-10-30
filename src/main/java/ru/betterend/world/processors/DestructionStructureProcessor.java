package ru.betterend.world.processors;

import net.minecraft.structure.Structure.StructureBlockInfo;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.MHelper;

public class DestructionStructureProcessor extends StructureProcessor {
	private int chance = 4;
	
	public void setChance(int chance) {
		this.chance = chance;
	}
	
	@Override
	public StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos, StructureBlockInfo structureBlockInfo, StructureBlockInfo structureBlockInfo2, StructurePlacementData structurePlacementData) {
		if (!structureBlockInfo2.state.isOf(EndBlocks.ETERNAL_PEDESTAL) && !structureBlockInfo2.state.isOf(EndBlocks.FLAVOLITE_RUNED_ETERNAL) && MHelper.RANDOM.nextInt(chance) == 0) {
			return null;
		}
		return structureBlockInfo2;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return StructureProcessorType.RULE;
	}
}
