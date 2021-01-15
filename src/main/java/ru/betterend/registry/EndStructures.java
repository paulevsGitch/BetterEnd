package ru.betterend.registry;

import java.util.Collection;
import java.util.function.Supplier;

import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep.Feature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import ru.betterend.BetterEnd;
import ru.betterend.world.structures.EndStructureFeature;
import ru.betterend.world.structures.features.StructureEternalPortal;
import ru.betterend.world.structures.features.StructureGiantIceStar;
import ru.betterend.world.structures.features.StructureGiantMossyGlowshroom;
import ru.betterend.world.structures.features.StructureMegaLake;
import ru.betterend.world.structures.features.StructureMegaLakeSmall;
import ru.betterend.world.structures.features.StructureMountain;
import ru.betterend.world.structures.features.StructurePaintedMountain;
import ru.betterend.world.structures.piece.CavePiece;
import ru.betterend.world.structures.piece.LakePiece;
import ru.betterend.world.structures.piece.MountainPiece;
import ru.betterend.world.structures.piece.NBTPiece;
import ru.betterend.world.structures.piece.PaintedMountainPiece;
import ru.betterend.world.structures.piece.VoxelPiece;

public class EndStructures {
	public static final StructurePieceType VOXEL_PIECE = register("voxel", VoxelPiece::new);
	public static final StructurePieceType MOUNTAIN_PIECE = register("mountain_piece", MountainPiece::new);
	public static final StructurePieceType CAVE_PIECE = register("cave_piece", CavePiece::new);
	public static final StructurePieceType LAKE_PIECE = register("lake_piece", LakePiece::new);
	public static final StructurePieceType PAINTED_MOUNTAIN_PIECE = register("painted_mountain_piece", PaintedMountainPiece::new);
	public static final StructurePieceType NBT_PIECE = register("nbt_piece", NBTPiece::new);
	
	public static final EndStructureFeature GIANT_MOSSY_GLOWSHROOM = new EndStructureFeature("giant_mossy_glowshroom", new StructureGiantMossyGlowshroom(), Feature.SURFACE_STRUCTURES, 16, 8);
	public static final EndStructureFeature MEGALAKE = new EndStructureFeature("megalake", new StructureMegaLake(), Feature.RAW_GENERATION, 4, 1);
	public static final EndStructureFeature MEGALAKE_SMALL = new EndStructureFeature("megalake_small", new StructureMegaLakeSmall(), Feature.RAW_GENERATION, 4, 1);
	public static final EndStructureFeature MOUNTAIN = new EndStructureFeature("mountain", new StructureMountain(), Feature.RAW_GENERATION, 3, 2);
	public static final EndStructureFeature PAINTED_MOUNTAIN = new EndStructureFeature("painted_mountain", new StructurePaintedMountain(), Feature.RAW_GENERATION, 3, 2);
	public static final EndStructureFeature ETERNAL_PORTAL = new EndStructureFeature("eternal_portal", new StructureEternalPortal(), Feature.SURFACE_STRUCTURES, 16, 6);
	public static final EndStructureFeature GIANT_ICE_STAR = new EndStructureFeature("giant_ice_star", new StructureGiantIceStar(), Feature.SURFACE_STRUCTURES, 16, 8);
	
	public static void register() {}
	
	private static StructurePieceType register(String id, StructurePieceType pieceType) {
		return Registry.register(Registry.STRUCTURE_PIECE, BetterEnd.makeID(id), pieceType);
	}
	
	public static void registerBiomeStructures(Identifier id, Biome biome, Collection<Supplier<ConfiguredStructureFeature<?, ?>>> structures) {
		if (!id.getPath().contains("mountain") && !id.getPath().contains("lake")) {
			addStructure(ETERNAL_PORTAL, structures);
		}
	}
	
	private static void addStructure(EndStructureFeature feature, Collection<Supplier<ConfiguredStructureFeature<?, ?>>> structures) {
		structures.add(() -> { return feature.getFeatureConfigured(); });
	}
}
