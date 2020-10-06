package ru.betterend.world.structures.piece;

import java.util.Random;
import java.util.function.Consumer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import ru.betterend.registry.StructureRegistry;

public class VoxelPiece extends BasePiece {
	private StructureWorld world;
	
	public VoxelPiece(Consumer<StructureWorld> function, int id) {
		super(StructureRegistry.VOXEL_PIECE, id);
		world = new StructureWorld();
		function.accept(world);
		this.boundingBox = world.getBounds();
	}

	public VoxelPiece(StructureManager manager, CompoundTag tag) {
		super(StructureRegistry.VOXEL_PIECE, tag);
	}

	@Override
	protected void toNbt(CompoundTag tag) {
		tag.put("world", world.toBNT());
	}

	protected void fromNbt(CompoundTag tag) {
		world = new StructureWorld(tag.getCompound("world"));
	}

	@Override
	public boolean generate(StructureWorldAccess world, StructureAccessor arg, ChunkGenerator chunkGenerator, Random random, BlockBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		return true;
	}
}
