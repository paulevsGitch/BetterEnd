package ru.betterend.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;

public class StructureHelper {
	private static final Direction[] DIR = BlocksHelper.makeHorizontal();
	
	public static Structure readStructure(Identifier resource) {
		String ns = resource.getNamespace();
		String nm = resource.getPath();
		return readStructure("/data/" + ns + "/structures/" + nm + ".nbt");
	}
	
	public static Structure readStructure(File datapack, String path) {
		if (datapack.isDirectory()) {
			return readStructure(datapack.toString() + "/" + path);
		}
		else if (datapack.isFile() && datapack.getName().endsWith(".zip")) {
			try {
				ZipFile zipFile = new ZipFile(datapack);
				Enumeration<? extends ZipEntry> entries = zipFile.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = entries.nextElement();
					String name = entry.getName();
					long compressedSize = entry.getCompressedSize();
					long normalSize = entry.getSize();
					String type = entry.isDirectory() ? "DIR" : "FILE";

					System.out.println(name);
					System.out.format("\t %s - %d - %d\n", type, compressedSize, normalSize);
				}
				zipFile.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static Structure readStructure(String path) {
		try {
			InputStream inputstream = StructureHelper.class.getResourceAsStream(path);
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
	
	public static BlockBox intersectBoxes(BlockBox box1, BlockBox box2) {
		int x1 = MHelper.max(box1.minX, box2.minX);
		int y1 = MHelper.max(box1.minY, box2.minY);
		int z1 = MHelper.max(box1.minZ, box2.minZ);
		
		int x2 = MHelper.min(box1.maxX, box2.maxX);
		int y2 = MHelper.min(box1.maxY, box2.maxY);
		int z2 = MHelper.min(box1.maxZ, box2.maxZ);
		
		return BlockBox.create(x1, y1, z1, x2, y2, z2);
	}
	
	public static void erode(StructureWorldAccess world, BlockBox bounds, int iterations, Random random) {
		Mutable mut = new Mutable();
		boolean canDestruct = true;
		for (int i = 0; i < iterations; i++) {
			for (int x = bounds.minX; x <= bounds.maxX; x++) {
				mut.setX(x);
				for (int z = bounds.minZ; z <= bounds.maxZ; z++) {
					mut.setZ(z);
					for (int y = bounds.maxY; y >= bounds.minY; y--) {
						mut.setY(y);
						BlockState state = world.getBlockState(mut);
						if (canDestruct && state.isOf(EndBlocks.FLAVOLITE_RUNED_ETERNAL) && random.nextInt(8) == 0 && world.isAir(mut.down(2))) {
							int r = MHelper.randRange(1, 4, random);
							int cx = mut.getX();
							int cy = mut.getY();
							int cz = mut.getZ();
							int x1 = cx - r;
							int y1 = cy - r;
							int z1 = cz - r;
							int x2 = cx + r;
							int y2 = cy + r;
							int z2 = cz + r;
							for (int px = x1; px <= x2; px++) {
								int dx = px - cx;
								dx *= dx;
								mut.setX(px);
								for (int py = y1; py <= y2; py++) {
									int dy = py - cy;
									dy *= dy;
									mut.setY(py);
									for (int pz = z1; pz <= z2; pz++) {
										int dz = pz - cz;
										dz *= dz;
										mut.setZ(pz);
										if (dx + dy + dz <= r && world.getBlockState(mut).isOf(EndBlocks.FLAVOLITE_RUNED_ETERNAL)) {
											BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
										}
									}
								}
							}
							mut.setX(cx);
							mut.setY(cy);
							mut.setZ(cz);
							canDestruct = false;
							continue;
						}
						else if (ignore(state)) {
							continue;
						}
						if (!state.isAir() && random.nextBoolean()) {
							shuffle(random);
							for (Direction dir: DIR) {
								if (world.isAir(mut.offset(dir)) && world.isAir(mut.down().offset(dir))) {
									BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
									mut.move(dir).move(Direction.DOWN);
									for (int py = mut.getY(); y >= bounds.minY - 10; y--) {
										mut.setY(py - 1);
										if (!world.isAir(mut)) {
											mut.setY(py);
											BlocksHelper.setWithoutUpdate(world, mut, state);
											break;
										}
									}
								}
							}
							break;
						}
						else if (random.nextInt(8) == 0 && !world.getBlockState(mut.up()).isOf(EndBlocks.ETERNAL_PEDESTAL)) {
							BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
						}
					}
				}
			}
		}
		for (int x = bounds.minX; x <= bounds.maxX; x++) {
			mut.setX(x);
			for (int z = bounds.minZ; z <= bounds.maxZ; z++) {
				mut.setZ(z);
				for (int y = bounds.maxY; y >= bounds.minY; y--) {
					mut.setY(y);
					BlockState state = world.getBlockState(mut);
					if (!ignore(state) && world.isAir(mut.down())) {
						BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
						for (int py = mut.getY(); py >= bounds.minY - 10; py--) {
							mut.setY(py - 1);
							if (!world.isAir(mut)) {
								mut.setY(py);
								BlocksHelper.setWithoutUpdate(world, mut, state);
								break;
							}
						}
					}
				}
			}
		}
	}

	public static void erodeIntense(StructureWorldAccess world, BlockBox bounds, Random random) {
		Mutable mut = new Mutable();
		Mutable mut2 = new Mutable();
		int minY = bounds.minY - 10;
		for (int x = bounds.minX; x <= bounds.maxX; x++) {
			mut.setX(x);
			for (int z = bounds.minZ; z <= bounds.maxZ; z++) {
				mut.setZ(z);
				for (int y = bounds.maxY; y >= bounds.minY; y--) {
					mut.setY(y);
					BlockState state = world.getBlockState(mut);
					if (!ignore(state)) {
						if (random.nextInt(6) == 0) {
							BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
							if (random.nextBoolean()) {
								int px = MHelper.floor(random.nextGaussian() * 2 + x + 0.5);
								int pz = MHelper.floor(random.nextGaussian() * 2 + z + 0.5);
								mut2.set(px, y, pz);
								while (world.getBlockState(mut2).getMaterial().isReplaceable() && mut2.getY() > minY) {
									mut2.setY(mut2.getY() - 1);
								}
								if (y > 50 && state.canPlaceAt(world, mut2)) {
									mut2.setY(mut2.getY() + 1);
									BlocksHelper.setWithoutUpdate(world, mut2, state);
								}
							}
						}
						else if (random.nextInt(8) == 0) {
							BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
						}
					}
				}
			}
		}

		drop(world, bounds);
	}
	
	private static boolean isTerrainNear(StructureWorldAccess world, BlockPos pos) {
		for (Direction dir: BlocksHelper.DIRECTIONS) {
			if (world.getBlockState(pos.offset(dir)).isIn(EndTags.GEN_TERRAIN)) {
				return true;
			}
		}
		return false;
	}
	
	private static void drop(StructureWorldAccess world, BlockBox bounds) {
		Mutable mut = new Mutable();
		
		Set<BlockPos> blocks = Sets.newHashSet();
		Set<BlockPos> edge = Sets.newHashSet();
		Set<BlockPos> add = Sets.newHashSet();
		
		for (int x = bounds.minX; x <= bounds.maxX; x++) {
			mut.setX(x);
			for (int z = bounds.minZ; z <= bounds.maxZ; z++) {
				mut.setZ(z);
				for (int y = bounds.minY; y <= bounds.maxY; y++) {
					mut.setY(y);
					BlockState state = world.getBlockState(mut);
					if (!ignore(state) && isTerrainNear(world, mut)) {
						edge.add(mut.toImmutable());
					}
				}
			}
		}
		
		if (edge.isEmpty()) {
			return;
		}
		
		while (!edge.isEmpty()) {
			for (BlockPos center: edge) {
				for (Direction dir: BlocksHelper.DIRECTIONS) {
					BlockState state = world.getBlockState(center);
					if (state.isFullCube(world, center)) {
						mut.set(center).move(dir);
						if (bounds.contains(mut)) {
							state = world.getBlockState(mut);
							if (!ignore(state) && !blocks.contains(mut)) {
								add.add(mut.toImmutable());
							}
						}
					}
				}
			}
			
			blocks.addAll(edge);
			edge.clear();
			edge.addAll(add);
			add.clear();
		}
		
		int minY = bounds.minY - 10;
		for (int x = bounds.minX; x <= bounds.maxX; x++) {
			mut.setX(x);
			for (int z = bounds.minZ; z <= bounds.maxZ; z++) {
				mut.setZ(z);
				for (int y = bounds.minY; y <= bounds.maxY; y++) {
					mut.setY(y);
					BlockState state = world.getBlockState(mut);
					if (!ignore(state) && !blocks.contains(mut)) {
						BlocksHelper.setWithoutUpdate(world, mut, Blocks.AIR);
						while (world.getBlockState(mut).getMaterial().isReplaceable() && mut.getY() > minY) {
							mut.setY(mut.getY() - 1);
						}
						if (mut.getY() > minY) {
							mut.setY(mut.getY() + 1);
							BlocksHelper.setWithoutUpdate(world, mut, state);
						}
					}
				}
			}
		}
	}

	private static boolean ignore(BlockState state) {
		return state.getMaterial().isReplaceable()
				|| !state.getFluidState().isEmpty()
				|| state.isIn(EndTags.END_GROUND)
				|| state.isOf(EndBlocks.ETERNAL_PEDESTAL)
				|| state.isOf(EndBlocks.FLAVOLITE_RUNED_ETERNAL)
				|| state.isIn(BlockTags.LOGS)
				|| state.isIn(BlockTags.LEAVES)
				|| state.getMaterial().equals(Material.PLANT)
				|| state.getMaterial().equals(Material.LEAVES);
	}
	
	private static void shuffle(Random random) {
		for (int i = 0; i < 4; i++) {
			int j = random.nextInt(4);
			Direction d = DIR[i];
			DIR[i] = DIR[j];
			DIR[j] = d;
		}
	}
	
	public static void cover(StructureWorldAccess world, BlockBox bounds, Random random) {
		Mutable mut = new Mutable();
		for (int x = bounds.minX; x <= bounds.maxX; x++) {
			mut.setX(x);
			for (int z = bounds.minZ; z <= bounds.maxZ; z++) {
				mut.setZ(z);
				BlockState top = world.getBiome(mut).getGenerationSettings().getSurfaceConfig().getTopMaterial();
				for (int y = bounds.maxY; y >= bounds.minY; y--) {
					mut.setY(y);
					BlockState state = world.getBlockState(mut);
					if (state.isIn(EndTags.END_GROUND) && !world.getBlockState(mut.up()).getMaterial().blocksLight()) {
						BlocksHelper.setWithoutUpdate(world, mut, top);
					}
				}
			}
		}
	}
}
