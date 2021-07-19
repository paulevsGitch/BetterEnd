package ru.betterend.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import ru.bclib.world.structures.BCLStructureFeature;
import ru.betterend.BetterEnd;
import ru.betterend.world.structures.features.EternalPortalStructure;
import ru.betterend.world.structures.features.GiantIceStarStructure;
import ru.betterend.world.structures.features.GiantMossyGlowshroomStructure;
import ru.betterend.world.structures.features.MegaLakeSmallStructure;
import ru.betterend.world.structures.features.MegaLakeStructure;
import ru.betterend.world.structures.features.MountainStructure;
import ru.betterend.world.structures.features.PaintedMountainStructure;
import ru.betterend.world.structures.piece.CavePiece;
import ru.betterend.world.structures.piece.CrystalMountainPiece;
import ru.betterend.world.structures.piece.LakePiece;
import ru.betterend.world.structures.piece.NBTPiece;
import ru.betterend.world.structures.piece.PaintedMountainPiece;
import ru.betterend.world.structures.piece.VoxelPiece;

import java.util.Collection;
import java.util.function.Supplier;

public class EndStructures {
	public static final StructurePieceType VOXEL_PIECE = register("voxel", VoxelPiece::new);
	public static final StructurePieceType MOUNTAIN_PIECE = register("mountain_piece", CrystalMountainPiece::new);
	public static final StructurePieceType CAVE_PIECE = register("cave_piece", CavePiece::new);
	public static final StructurePieceType LAKE_PIECE = register("lake_piece", LakePiece::new);
	public static final StructurePieceType PAINTED_MOUNTAIN_PIECE = register(
		"painted_mountain_piece",
		PaintedMountainPiece::new
	);
	public static final StructurePieceType NBT_PIECE = register("nbt_piece", NBTPiece::new);
	
	public static final BCLStructureFeature GIANT_MOSSY_GLOWSHROOM = new BCLStructureFeature(BetterEnd.makeID(
		"giant_mossy_glowshroom"), new GiantMossyGlowshroomStructure(), Decoration.SURFACE_STRUCTURES, 16, 8);
	public static final BCLStructureFeature MEGALAKE = new BCLStructureFeature(
		BetterEnd.makeID("megalake"),
		new MegaLakeStructure(),
		Decoration.RAW_GENERATION,
		4,
		1
	);
	public static final BCLStructureFeature MEGALAKE_SMALL = new BCLStructureFeature(
		BetterEnd.makeID("megalake_small"),
		new MegaLakeSmallStructure(),
		Decoration.RAW_GENERATION,
		4,
		1
	);
	public static final BCLStructureFeature MOUNTAIN = new BCLStructureFeature(
		BetterEnd.makeID("mountain"),
		new MountainStructure(),
		Decoration.RAW_GENERATION,
		3,
		2
	);
	public static final BCLStructureFeature PAINTED_MOUNTAIN = new BCLStructureFeature(BetterEnd.makeID(
		"painted_mountain"), new PaintedMountainStructure(), Decoration.RAW_GENERATION, 3, 2);
	public static final BCLStructureFeature ETERNAL_PORTAL = new BCLStructureFeature(
		BetterEnd.makeID("eternal_portal"),
		new EternalPortalStructure(),
		Decoration.SURFACE_STRUCTURES,
		16,
		6
	);
	public static final BCLStructureFeature GIANT_ICE_STAR = new BCLStructureFeature(
		BetterEnd.makeID("giant_ice_star"),
		new GiantIceStarStructure(),
		Decoration.SURFACE_STRUCTURES,
		16,
		8
	);
	
	public static void register() {
	}
	
	private static StructurePieceType register(String id, StructurePieceType pieceType) {
		return Registry.register(Registry.STRUCTURE_PIECE, BetterEnd.makeID(id), pieceType);
	}
	
	public static void registerBiomeStructures(ResourceLocation id, Biome biome, Collection<Supplier<ConfiguredStructureFeature<?, ?>>> structures) {
		if (!id.getPath().contains("mountain") && !id.getPath().contains("lake")) {
			addStructure(ETERNAL_PORTAL, structures);
		}
	}
	
	private static void addStructure(BCLStructureFeature feature, Collection<Supplier<ConfiguredStructureFeature<?, ?>>> structures) {
		structures.add(() -> {
			return feature.getFeatureConfigured();
		});
	}
}
