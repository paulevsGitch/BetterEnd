package ru.betterend.world.structures.piece;

import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import ru.betterend.registry.EndStructures;
import ru.betterend.world.structures.StructureWorld;

public class VoxelPiece extends BasePiece {
	private StructureWorld world;

	public VoxelPiece(Consumer<StructureWorld> function, int id) {
		super(EndStructures.VOXEL_PIECE, id);
		world = new StructureWorld();
		function.accept(world);
		this.boundingBox = world.getBounds();
	}

	public VoxelPiece(StructureManager manager, CompoundTag tag) {
		super(EndStructures.VOXEL_PIECE, tag);
		this.boundingBox = world.getBounds();
	}

	@Override
	protected void toNbt(CompoundTag tag) {
		tag.put("world", world.toBNT());
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		world = new StructureWorld(tag.getCompound("world"));
	}

	@Override
	public boolean place(WorldGenLevel world, StructureAccessor arg, ChunkGenerator chunkGenerator, Random random,
			BlockBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		this.world.placeChunk(world, chunkPos);
		return true;
	}
}
