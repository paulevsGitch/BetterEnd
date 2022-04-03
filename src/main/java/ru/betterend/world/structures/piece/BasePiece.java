package ru.betterend.world.structures.piece;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public abstract class BasePiece extends StructurePiece {
	protected BasePiece(StructurePieceType type, int i, BoundingBox boundingBox) {
		super(type, i, boundingBox);
	}
	
	protected BasePiece(StructurePieceType type, CompoundTag tag) {
		super(type, tag);
		fromNbt(tag);
	}
	
	protected abstract void fromNbt(CompoundTag tag);
	
	protected void addAdditionalSaveData(CompoundTag tag) {}
	
	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
		addAdditionalSaveData(compoundTag);
	}
}