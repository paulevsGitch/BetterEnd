package ru.betterend.world.structures.piece;

import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import ru.bclib.world.structures.StructureWorld;
import ru.betterend.registry.EndStructures;

public class VoxelPiece extends BasePiece {
	private StructureWorld world;
	
	public VoxelPiece(Consumer<StructureWorld> function, int id) {
		super(EndStructures.VOXEL_PIECE, id, null);
		world = new StructureWorld();
		function.accept(world);
		this.boundingBox = world.getBounds();
	}

	public VoxelPiece(StructureManager manager, CompoundTag tag) {
		super(EndStructures.VOXEL_PIECE, tag);
		this.boundingBox = world.getBounds();
	}

	@Override
	protected void addAdditionalSaveData(ServerLevel serverLevel, CompoundTag compoundTag) {
		tag.put("world", world.toBNT());
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		world = new StructureWorld(tag.getCompound("world"));
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager arg, ChunkGenerator chunkGenerator, Random random, BoundingBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		this.world.placeChunk(world, chunkPos);
		return true;
	}
}
