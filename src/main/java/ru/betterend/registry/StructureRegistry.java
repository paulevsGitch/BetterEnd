package ru.betterend.registry;

import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;
import ru.betterend.BetterEnd;
import ru.betterend.world.structures.piece.VoxelPiece;

public class StructureRegistry {
	public static final StructurePieceType VOXEL_PIECE = register("sdf", VoxelPiece::new);
	
	private static StructurePieceType register(String id, StructurePieceType pieceType) {
		return Registry.register(Registry.STRUCTURE_PIECE, BetterEnd.makeID(id), pieceType);
	}
}
