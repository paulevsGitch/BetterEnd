package ru.betterend.world.structures;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;

public class StructureWorld {
	private Map<ChunkPos, Part> parts = Maps.newHashMap();
	private int minX = Integer.MAX_VALUE;
	private int minZ = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int maxZ = Integer.MIN_VALUE;
	
	public StructureWorld() {}
	
	public StructureWorld(CompoundTag tag) {
		minX = tag.getInt("minX");
		maxX = tag.getInt("maxX");
		minZ = tag.getInt("minZ");
		maxZ = tag.getInt("maxZ");
		ListTag map = tag.getList("parts", 10);
		map.forEach((element) -> {
			CompoundTag compound = (CompoundTag) element;
			Part part = new Part(compound);
			int x = compound.getInt("x");
			int z = compound.getInt("z");
			parts.put(new ChunkPos(x, z), part);
		});
	}
	
	public void setBlock(BlockPos pos, BlockState state) {
		ChunkPos cPos = new ChunkPos(pos);
		Part part = parts.get(cPos);
		if (part == null) {
			part = new Part();
			parts.put(cPos, part);
			
			if (cPos.x < minX) minX = cPos.x;
			if (cPos.x > maxX) maxX = cPos.x;
			if (cPos.z < minZ) minZ = cPos.z;
			if (cPos.z > maxZ) maxZ = cPos.z;
		}
		part.addBlock(pos, state);
	}
	
	public boolean placeChunk(StructureWorldAccess world, ChunkPos chunkPos) {
		Part part = parts.get(chunkPos);
		if (part != null) {
			Chunk chunk = world.getChunk(chunkPos.x, chunkPos.z);
			part.placeChunk(chunk);
			return true;
		}
		return false;
	}
	
	public CompoundTag toBNT() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("minX", minX);
		tag.putInt("maxX", maxX);
		tag.putInt("minZ", minZ);
		tag.putInt("maxZ", maxZ);
		ListTag map = new ListTag();
		tag.put("parts", map);
		parts.forEach((pos, part) -> {
			map.add(part.toNBT(pos.x, pos.z));
		});
		return tag;
	}
	
	public BlockBox getBounds() {
		return new BlockBox(minX << 4, minZ << 4, (maxX << 4) | 15, (maxZ << 4) | 15);
	}
	
	private static final class Part {
		Map<BlockPos, BlockState> blocks = Maps.newHashMap();
		
		public Part() {}
		
		public Part(CompoundTag tag) {
			ListTag map = tag.getList("blocks", 10);
			map.forEach((element) -> {
				CompoundTag block = (CompoundTag) element;
				BlockPos pos = NbtHelper.toBlockPos(block.getCompound("pos"));
				BlockState state = Block.getStateFromRawId(block.getInt("state"));
				blocks.put(pos, state);
			});
		}
		
		void addBlock(BlockPos pos, BlockState state) {
			BlockPos inner = new BlockPos(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
			blocks.put(inner, state);
		}
		
		void placeChunk(Chunk chunk) {
			blocks.forEach((pos, state) -> {
				chunk.setBlockState(pos, state, false);
			});
		}
		
		CompoundTag toNBT(int x, int z) {
			CompoundTag tag = new CompoundTag();
			tag.putInt("x", x);
			tag.putInt("z", z);
			ListTag map = new ListTag();
			tag.put("blocks", map);
			blocks.forEach((pos, state) -> {
				CompoundTag block = new CompoundTag();
				block.put("pos", NbtHelper.fromBlockPos(pos));
				block.putInt("state", Block.getRawIdFromState(state));
				map.add(block);
			});
			return tag;
		}
	}
}
