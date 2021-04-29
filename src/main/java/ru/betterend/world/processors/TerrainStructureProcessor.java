package ru.betterend.world.processors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class TerrainStructureProcessor extends StructureProcessor {
	@Override
	public StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureBlockInfo structureBlockInfo, StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlacementData) {
		BlockPos bpos = structureBlockInfo2.pos;
		if (structureBlockInfo2.state.is(Blocks.END_STONE) && worldView.isEmptyBlock(bpos.above())) {
			BlockState top = worldView.getBiome(structureBlockInfo2.pos).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
			return new StructureBlockInfo(bpos, top, structureBlockInfo2.nbt);
		}
		return structureBlockInfo2;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return StructureProcessorType.RULE;
	}
}
