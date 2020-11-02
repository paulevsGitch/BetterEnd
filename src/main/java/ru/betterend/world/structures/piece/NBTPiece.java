package ru.betterend.world.structures.piece;

import java.util.Random;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import ru.betterend.registry.EndStructures;
import ru.betterend.util.StructureHelper;

public class NBTPiece extends BasePiece {
	private Identifier structureID;
	private BlockRotation rotation;
	private BlockMirror mirror;
	private Structure structure;
	private BlockPos pos;
	
	public NBTPiece(Identifier structureID, Structure structure, BlockPos pos, Random random) {
		super(EndStructures.NBT_PIECE, random.nextInt());
		this.structureID = structureID;
		this.structure = structure;
		this.rotation = BlockRotation.random(random);
		this.mirror = BlockMirror.values()[random.nextInt(3)];
		this.pos = StructureHelper.offsetPos(pos, structure, rotation, mirror);
		makeBoundingBox();
	}

	public NBTPiece(StructureManager manager, CompoundTag tag) {
		super(EndStructures.NBT_PIECE, tag);
		makeBoundingBox();
	}

	@Override
	protected void toNbt(CompoundTag tag) {
		tag.putString("id", structureID.toString());
		tag.putInt("rotation", rotation.ordinal());
		tag.putInt("mirror", mirror.ordinal());
		tag.put("pos", NbtHelper.fromBlockPos(pos));
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		structureID = new Identifier(tag.getString("id"));
		rotation = BlockRotation.values()[tag.getInt("rotation")];
		mirror = BlockMirror.values()[tag.getInt("mirror")];
		pos = NbtHelper.toBlockPos(tag.getCompound("pos"));
		structure = StructureHelper.readStructure(structureID);
	}

	@Override
	public boolean generate(StructureWorldAccess world, StructureAccessor arg, ChunkGenerator chunkGenerator, Random random, BlockBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		StructurePlacementData placementData = new StructurePlacementData().setRotation(rotation).setMirror(mirror).setBoundingBox(blockBox);
		structure.place(world, pos, placementData, random);
		return true;
	}
	
	private void makeBoundingBox() {
		this.boundingBox = StructureHelper.getStructureBounds(pos, structure, rotation, mirror);
	}
}
