package ru.betterend.world.processors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import ru.bclib.util.MHelper;
import ru.betterend.registry.EndBlocks;

public class DestructionStructureProcessor extends StructureProcessor {
	private int chance = 4;
	
	public void setChance(int chance) {
		this.chance = chance;
	}
	
	@Override
	public StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureBlockInfo structureBlockInfo, StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlacementData) {
		if (!structureBlockInfo2.state.is(EndBlocks.ETERNAL_PEDESTAL) && !structureBlockInfo2.state.is(EndBlocks.FLAVOLITE_RUNED_ETERNAL) && MHelper.RANDOM.nextInt(chance) == 0) {
			return null;
		}
		return structureBlockInfo2;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return StructureProcessorType.RULE;
	}
}
