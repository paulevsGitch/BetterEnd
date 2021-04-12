package ru.betterend.world.structures.piece;

import java.util.Random;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import ru.betterend.registry.EndStructures;
import ru.betterend.util.MHelper;
import ru.betterend.util.StructureHelper;

public class NBTPiece extends BasePiece {
	private ResourceLocation structureID;
	private Rotation rotation;
	private BlockMirror mirror;
	private Structure structure;
	private BlockPos pos;
	private int erosion;
	private boolean cover;

	public NBTPiece(ResourceLocation structureID, Structure structure, BlockPos pos, int erosion, boolean cover,
			Random random) {
		super(EndStructures.NBT_PIECE, random.nextInt());
		this.structureID = structureID;
		this.structure = structure;
		this.rotation = Rotation.random(random);
		this.mirror = BlockMirror.values()[random.nextInt(3)];
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
	protected void toNbt(CompoundTag tag) {
		tag.putString("id", structureID.toString());
		tag.putInt("rotation", rotation.ordinal());
		tag.putInt("mirror", mirror.ordinal());
		tag.putInt("erosion", erosion);
		tag.put("pos", NbtHelper.fromBlockPos(pos));
		tag.putBoolean("cover", cover);
	}

	@Override
	protected void fromNbt(CompoundTag tag) {
		structureID = new ResourceLocation(tag.getString("id"));
		rotation = Rotation.values()[tag.getInt("rotation")];
		mirror = BlockMirror.values()[tag.getInt("mirror")];
		erosion = tag.getInt("erosion");
		pos = NbtHelper.toBlockPos(tag.getCompound("pos"));
		cover = tag.getBoolean("cover");
		structure = StructureHelper.readStructure(structureID);
	}

	@Override
	public boolean place(WorldGenLevel world, StructureAccessor arg, ChunkGenerator chunkGenerator, Random random,
			BlockBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		BlockBox bounds = new BlockBox(blockBox);
		bounds.maxY = this.boundingBox.maxY;
		bounds.minY = this.boundingBox.minY;
		StructurePlacementData placementData = new StructurePlacementData().setRotation(rotation).setMirror(mirror)
				.setBoundingBox(bounds);
		structure.place(world, pos, placementData, random);
		if (erosion > 0) {
			bounds.maxX = MHelper.min(bounds.maxX, boundingBox.maxX);
			bounds.minX = MHelper.max(bounds.minX, boundingBox.minX);
			bounds.maxZ = MHelper.min(bounds.maxZ, boundingBox.maxZ);
			bounds.minZ = MHelper.max(bounds.minZ, boundingBox.minZ);
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
