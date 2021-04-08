package ru.betterend.world.features;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraft.structure.Structure;
import net.minecraft.structure.Structure.StructureBlockInfo;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.BlockMirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import ru.betterend.registry.EndTags;
import ru.betterend.util.BlocksHelper;
import ru.betterend.util.MHelper;
import ru.betterend.util.StructureHelper;

public class CrashedShipFeature extends NBTStructureFeature {
	private static final StructureProcessor REPLACER;
	private static final String STRUCTURE_PATH = "/data/minecraft/structures/end_city/ship.nbt";
	private Structure structure;

	@Override
	protected Structure getStructure(StructureWorldAccess world, BlockPos pos, Random random) {
		if (structure == null) {
			structure = world.toServerWorld().getStructureManager()
					.getStructureOrBlank(new ResourceLocation("end_city/ship"));
			if (structure == null) {
				structure = StructureHelper.readStructure(STRUCTURE_PATH);
			}
		}
		return structure;
	}

	@Override
	protected boolean canSpawn(StructureWorldAccess world, BlockPos pos, Random random) {
		long x = pos.getX() >> 4;
		long z = pos.getX() >> 4;
		if (x * x + z * z < 3600) {
			return false;
		}
		return pos.getY() > 5 && world.getBlockState(pos.below()).isIn(EndTags.GEN_TERRAIN);
	}

	@Override
	protected Rotation getRotation(StructureWorldAccess world, BlockPos pos, Random random) {
		return Rotation.random(random);
	}

	@Override
	protected BlockMirror getMirror(StructureWorldAccess world, BlockPos pos, Random random) {
		return BlockMirror.values()[random.nextInt(3)];
	}

	@Override
	protected int getYOffset(Structure structure, StructureWorldAccess world, BlockPos pos, Random random) {
		int min = structure.getSize().getY() >> 3;
		int max = structure.getSize().getY() >> 2;
		return -MHelper.randRange(min, max, random);
	}

	@Override
	protected TerrainMerge getTerrainMerge(StructureWorldAccess world, BlockPos pos, Random random) {
		return TerrainMerge.NONE;
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos center,
			DefaultFeatureConfig featureConfig) {
		center = new BlockPos(((center.getX() >> 4) << 4) | 8, 128, ((center.getZ() >> 4) << 4) | 8);
		center = getGround(world, center);
		BlockBox bounds = makeBox(center);

		if (!canSpawn(world, center, random)) {
			return false;
		}

		Structure structure = getStructure(world, center, random);
		Rotation rotation = getRotation(world, center, random);
		BlockMirror mirror = getMirror(world, center, random);
		BlockPos offset = Structure.transformAround(structure.getSize(), mirror, rotation, BlockPos.ORIGIN);
		center = center.add(0, getYOffset(structure, world, center, random) + 0.5, 0);
		StructurePlacementData placementData = new StructurePlacementData().setRotation(rotation).setMirror(mirror);
		center = center.add(-offset.getX() * 0.5, 0, -offset.getZ() * 0.5);

		BlockBox structB = structure.calculateBoundingBox(placementData, center);
		bounds = StructureHelper.intersectBoxes(bounds, structB);

		addStructureData(placementData);
		structure.place(world, center, placementData.setBoundingBox(bounds), random);

		StructureHelper.erodeIntense(world, bounds, random);
		BlocksHelper.fixBlocks(world, new BlockPos(bounds.minX, bounds.minY, bounds.minZ),
				new BlockPos(bounds.maxX, bounds.maxY, bounds.maxZ));

		return true;
	}

	@Override
	protected void addStructureData(StructurePlacementData data) {
		data.addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS).addProcessor(REPLACER)
				.setIgnoreEntities(true);
	}

	static {
		REPLACER = new StructureProcessor() {
			@Override
			public StructureBlockInfo process(WorldView worldView, BlockPos pos, BlockPos blockPos,
					StructureBlockInfo structureBlockInfo, StructureBlockInfo structureBlockInfo2,
					StructurePlacementData structurePlacementData) {
				BlockState state = structureBlockInfo2.state;
				if (state.is(Blocks.SPAWNER) || state.getMaterial().equals(Material.WOOL)) {
					return new StructureBlockInfo(structureBlockInfo2.pos, AIR, null);
				}
				return structureBlockInfo2;
			}

			@Override
			protected StructureProcessorType<?> getType() {
				return StructureProcessorType.NOP;
			}
		};
	}
}
