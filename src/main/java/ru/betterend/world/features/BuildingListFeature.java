package ru.betterend.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import org.jetbrains.annotations.Nullable;
import ru.bclib.world.features.ListFeature;
import ru.betterend.util.LootTableUtil;

import java.util.List;
import java.util.Random;

public class BuildingListFeature extends ListFeature {
	public BuildingListFeature(List<StructureInfo> list, BlockState defaultBlock) {
		super(list, defaultBlock);
	}
	
	@Override
	protected void addStructureData(StructurePlaceSettings data) {
		super.addStructureData(data);
		data.addProcessor(new ChestProcessor());
	}
	
	class ChestProcessor extends StructureProcessor {
		@Nullable
		@Override
		public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos2, StructureBlockInfo structureBlockInfo, StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlaceSettings) {
			BlockState blockState = structureBlockInfo2.state;
			if (blockState.getBlock() instanceof ChestBlock) {
				Random random = structurePlaceSettings.getRandom(structureBlockInfo2.pos);
				BlockPos chestPos = structureBlockInfo2.pos;
				ChestBlock chestBlock = ChestBlock.class.cast(blockState.getBlock());
				BlockEntity entity = chestBlock.newBlockEntity(chestPos, blockState);
				levelReader.getChunk(chestPos).setBlockEntity(entity);
				RandomizableContainerBlockEntity chestEntity = RandomizableContainerBlockEntity.class.cast(entity);
				Biome biome = levelReader.getNoiseBiome(
					chestPos.getX() >> 2,
					chestPos.getY() >> 2,
					chestPos.getZ() >> 2
				);
				chestEntity.setLootTable(LootTableUtil.getTable(biome), random.nextLong());
				chestEntity.setChanged();
			}
			return structureBlockInfo2;
		}
		
		@Override
		protected StructureProcessorType<?> getType() {
			return StructureProcessorType.NOP;
		}
	}
}
