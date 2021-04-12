package ru.betterend.world.structures.piece;

import java.util.Random;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import ru.betterend.registry.EndStructures;
import ru.betterend.util.MHelper;
import ru.betterend.util.StructureHelper;

public class NBTPiece extends BasePiece {
	private ResourceLocation structureID;
	private Rotation rotation;
	private Mirror mirror;
	private StructureTemplate structure;
	private BlockPos pos;
	private int erosion;
	private boolean cover;
	
	public NBTPiece(ResourceLocation structureID, StructureTemplate structure, BlockPos pos, int erosion, boolean cover, Random random) {
		super(EndStructures.NBT_PIECE, random.nextInt());
		this.structureID = structureID;
		this.structure = structure;
		this.rotation = Rotation.getRandom(random);
		this.mirror = Mirror.values()[random.nextInt(3)];
		this.pos = StructureHelper.offsetPos(pos, structure, rotation, mirror);
		this.erosion = erosion;
		this.cover = cover;
		makeBoundingBox();
	}

	public NBTPiece(StructureManager manager, CompoundTag tag) {
		super(EndStructures.NBT_PIECE, tag);
		makeBoundingBox();
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putString("id", structureID.toString());
		tag.putInt("rotation", rotation.ordinal());
		tag.putInt("mirror", mirror.ordinal());
		tag.putInt("erosion", erosion);
		tag.put("pos", NbtUtils.writeBlockPos(pos));
		tag.putBoolean("cover", cover);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		structureID = new ResourceLocation(tag.getString("id"));
		rotation = Rotation.values()[tag.getInt("rotation")];
		mirror = Mirror.values()[tag.getInt("mirror")];
		erosion = tag.getInt("erosion");
		pos = NbtUtils.readBlockPos(tag.getCompound("pos"));
		cover = tag.getBoolean("cover");
		structure = StructureHelper.readStructure(structureID);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager arg, ChunkGenerator chunkGenerator, Random random, BoundingBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		BoundingBox bounds = new BoundingBox(blockBox);
		bounds.y1 = this.boundingBox.y1;
		bounds.y0 = this.boundingBox.y0;
		StructurePlaceSettings placementData = new StructurePlaceSettings().setRotation(rotation).setMirror(mirror).setBoundingBox(bounds);
		structure.placeInWorldChunk(world, pos, placementData, random);
		if (erosion > 0) {
			bounds.x1 = MHelper.min(bounds.x1, boundingBox.x1);
			bounds.x0 = MHelper.max(bounds.x0, boundingBox.x0);
			bounds.z1 = MHelper.min(bounds.z1, boundingBox.z1);
			bounds.z0 = MHelper.max(bounds.z0, boundingBox.z0);
			StructureHelper.erode(world, bounds, erosion, random);
		}
		if (cover) {
			StructureHelper.cover(world, bounds, random);
		}
		return true;
	}
	
	private void makeBoundingBox() {
		this.boundingBox = StructureHelper.getStructureBounds(pos, structure, rotation, mirror);
	}
}
