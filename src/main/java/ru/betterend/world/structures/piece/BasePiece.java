package ru.betterend.world.structures.piece;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public abstract class BasePiece extends StructurePiece {
	protected BasePiece(StructurePieceType type, int i, BoundingBox boundingBox) {
		super(type, i, boundingBox);
	}
	
	protected BasePiece(StructurePieceType type, CompoundTag tag) {
		super(type, tag);
		fromNbt(tag);
	}
	
	protected abstract void fromNbt(CompoundTag tag);
}