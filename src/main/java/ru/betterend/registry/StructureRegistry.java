package ru.betterend.registry;

import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep.Feature;
import ru.betterend.BetterEnd;
import ru.betterend.world.structures.EndStructureFeature;
import ru.betterend.world.structures.features.StructureGiantMossyGlowshroom;
import ru.betterend.world.structures.piece.CavePiece;
import ru.betterend.world.structures.piece.LakePiece;
import ru.betterend.world.structures.piece.MountainPiece;
import ru.betterend.world.structures.piece.VoxelPiece;

public class StructureRegistry {
	public static final StructurePieceType VOXEL_PIECE = register("voxel", VoxelPiece::new);
	public static final StructurePieceType MOUNTAIN_PIECE = register("mountain_piece", MountainPiece::new);
	public static final StructurePieceType CAVE_PIECE = register("cave_piece", CavePiece::new);
	public static final StructurePieceType LAKE_PIECE = register("lake_piece", LakePiece::new);
	
	public static final EndStructureFeature GIANT_MOSSY_GLOWSHROOM = new EndStructureFeature("giant_mossy_glowshroom", new StructureGiantMossyGlowshroom(), Feature.SURFACE_STRUCTURES, 16, 8);
	
	public static void register() {}
	
	private static StructurePieceType register(String id, StructurePieceType pieceType) {
		return Registry.register(Registry.STRUCTURE_PIECE, BetterEnd.makeID(id), pieceType);
	}
}
