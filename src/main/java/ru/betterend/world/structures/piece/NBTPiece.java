package ru.betterend.world.structures.piece;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import ru.bclib.util.MHelper;
import ru.bclib.util.StructureHelper;
import ru.betterend.registry.EndStructures;
import ru.betterend.util.StructureErode;
import ru.betterend.world.biome.EndBiome;

import java.util.Random;

public class NBTPiece extends BasePiece {
	private ResourceLocation structureID;
	private Rotation rotation;
	private Mirror mirror;
	private StructureTemplate structure;
	private BlockPos pos;
	private int erosion;
	private boolean cover;
	
	public NBTPiece(ResourceLocation structureID, StructureTemplate structure, BlockPos pos, int erosion, boolean cover, Random random) {
		super(EndStructures.NBT_PIECE, random.nextInt(), null);
		this.structureID = structureID;
		this.structure = structure;
		this.rotation = Rotation.getRandom(random);
		this.mirror = Mirror.values()[random.nextInt(3)];
		this.pos = StructureHelper.offsetPos(pos, structure, rotation, mirror);
		this.erosion = erosion;
		this.cover = cover;
		makeBoundingBox();
	}
	
	public NBTPiece(StructurePieceSerializationContext type, CompoundTag tag) {
		super(EndStructures.NBT_PIECE, tag);
		makeBoundingBox();
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putString("structureID", structureID.toString());
		tag.putInt("rotation", rotation.ordinal());
		tag.putInt("mirror", mirror.ordinal());
		tag.putInt("erosion", erosion);
		tag.put("pos", NbtUtils.writeBlockPos(pos));
		tag.putBoolean("cover", cover);
	}
	
	@Override
	protected void fromNbt(CompoundTag tag) {
		structureID = new ResourceLocation(tag.getString("structureID"));
		rotation = Rotation.values()[tag.getInt("rotation")];
		mirror = Mirror.values()[tag.getInt("mirror")];
		erosion = tag.getInt("erosion");
		pos = NbtUtils.readBlockPos(tag.getCompound("pos"));
		cover = tag.getBoolean("cover");
		structure = StructureHelper.readStructure(structureID);
	}
	
	@Override
	public void postProcess(WorldGenLevel world, StructureFeatureManager arg, ChunkGenerator chunkGenerator, Random random, BoundingBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		BoundingBox bounds = BoundingBox.fromCorners(new Vec3i(
			blockBox.minX(),
			this.boundingBox.minY(),
			blockBox.minZ()
		), new Vec3i(blockBox.maxX(), this.boundingBox.maxX(), blockBox.maxZ()));
		StructurePlaceSettings placementData = new StructurePlaceSettings().setRotation(rotation).setMirror(mirror).setBoundingBox(bounds);
		structure.placeInWorld(world, pos, pos, placementData, random, 2);
		if (erosion > 0) {
			int x1 = MHelper.min(bounds.maxX(), boundingBox.maxX());
			int x0 = MHelper.max(bounds.minX(), boundingBox.minX());
			int z1 = MHelper.min(bounds.maxZ(), boundingBox.maxZ());
			int z0 = MHelper.max(bounds.minZ(), boundingBox.minZ());
			bounds = BoundingBox.fromCorners(new Vec3i(x0, bounds.minY(), z0), new Vec3i(x1, bounds.maxY(), z1));
			StructureErode.erode(world, bounds, erosion, random);
		}
		if (cover) {
			StructureErode.cover(world, bounds, random, EndBiome.Config.DEFAULT_MATERIAL.getTopMaterial());
		}
	}
	
	private void makeBoundingBox() {
		this.boundingBox = StructureHelper.getStructureBounds(pos, structure, rotation, mirror);
	}
}
