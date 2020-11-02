package ru.betterend.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;

public class StructureHelper {
	public static Structure readStructure(Identifier resource) {
		String ns = resource.getNamespace();
		String nm = resource.getPath();

		try {
			InputStream inputstream = MinecraftServer.class.getResourceAsStream("/data/" + ns + "/structures/" + nm + ".nbt");
			return readStructureFromStream(inputstream);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private static Structure readStructureFromStream(InputStream stream) throws IOException {
		CompoundTag nbttagcompound = NbtIo.readCompressed(stream);

		Structure template = new Structure();
		template.fromTag(nbttagcompound);

		return template;
	}
	
	public static BlockPos offsetPos(BlockPos pos, Structure structure, BlockRotation rotation, BlockMirror mirror) {
		BlockPos offset = Structure.transformAround(structure.getSize(), mirror, rotation, BlockPos.ORIGIN);
		return pos.add(-offset.getX() * 0.5, 0, -offset.getZ() * 0.5);
	}
	
	public static void placeCenteredBottom(StructureWorldAccess world, BlockPos pos, Structure structure, BlockRotation rotation, BlockMirror mirror, Random random) {
		placeCenteredBottom(world, pos, structure, rotation, mirror, makeBox(pos), random);
	}
	
	public static void placeCenteredBottom(StructureWorldAccess world, BlockPos pos, Structure structure, BlockRotation rotation, BlockMirror mirror, BlockBox bounds, Random random) {
		BlockPos offset = offsetPos(pos, structure, rotation, mirror);
		StructurePlacementData placementData = new StructurePlacementData().setRotation(rotation).setMirror(mirror).setBoundingBox(bounds);
		structure.place(world, offset, placementData, random);
	}
	
	private static BlockBox makeBox(BlockPos pos) {
		int sx = ((pos.getX() >> 4) << 4) - 16;
		int sz = ((pos.getZ() >> 4) << 4) - 16;
		int ex = sx + 47;
		int ez = sz + 47;
		return BlockBox.create(sx, 0, sz, ex, 255, ez);
	}
	
	public static BlockBox getStructureBounds(BlockPos pos, Structure structure, BlockRotation rotation, BlockMirror mirror) {
		BlockPos max = structure.getSize();
		BlockPos min = Structure.transformAround(structure.getSize(), mirror, rotation, BlockPos.ORIGIN);
		max = max.subtract(min);
		return new BlockBox(min.add(pos), max.add(pos));
	}
}
