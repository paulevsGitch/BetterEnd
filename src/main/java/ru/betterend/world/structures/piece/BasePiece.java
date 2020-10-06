package ru.betterend.world.structures.piece;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;

public abstract class BasePiece extends StructurePiece {
	protected BasePiece(StructurePieceType type, int i) {
		super(type, i);
	}

	protected BasePiece(StructurePieceType type, CompoundTag tag) {
		super(type, tag);
		fromNbt(tag);
	}
	
	protected abstract void fromNbt(CompoundTag tag);
}