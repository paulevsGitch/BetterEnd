package ru.betterend.util;

import java.awt.Point;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.ConfiguredFeatures;

import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.EndPortalBlock;
import ru.betterend.blocks.RunedFlavolite;
import ru.betterend.blocks.entities.PedestalBlockEntity;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndTags;

public class EternalRitual {
	private final static Set<Point> structureMap = Sets.newHashSet(
			new Point(-4, -5), new Point(-4, 5), new Point(-6, 0),
			new Point(4, -5), new Point(4, 5), new Point(6, 0));
	private final static Set<Point> frameMap = Sets.newHashSet(
			new Point(0, 0), new Point(0, 6), new Point(1, 0),
			new Point(1, 6), new Point(2, 1), new Point(2, 5),
			new Point(3, 2), new Point(3, 3), new Point(3, 4));
	private final static Set<Point> portalMap = Sets.newHashSet(
			new Point(0, 0), new Point(0, 1), new Point(0, 2),
			new Point(0, 3), new Point(0, 4), new Point(1, 0),
			new Point(1, 1), new Point(1, 2), new Point(1, 3),
			new Point(1, 4), new Point(2, 1), new Point(2, 2),
			new Point(2, 3));
	private final static Set<Point> baseMap = Sets.newHashSet(
			new Point(3, 0), new Point(2, 0), new Point(2, 1), new Point(1, 1),
			new Point(1, 2), new Point(0, 1), new Point(0, 2));
	
	private final static Block BASE = EndBlocks.FLAVOLITE.tiles;
	private final static Block PEDESTAL = EndBlocks.ETERNAL_PEDESTAL;
	private final static Block FRAME = EndBlocks.FLAVOLITE_RUNED_ETERNAL;
	private final static Block PORTAL = EndBlocks.END_PORTAL_BLOCK;
	private final static BooleanProperty ACTIVE = BlockProperties.ACTIVATED;
	
	private World world;
	private Direction.Axis axis;
	private BlockPos center;
	private BlockPos exit;
	private boolean active = false;
	
	public EternalRitual(World world) {
		this.world = world;
	}
	
	public EternalRitual(World world, BlockPos initial) {
		this(world);
		this.configure(initial);
	}
	
	public boolean hasWorld() {
		return this.world != null;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	private boolean isValid() {
		return world != null && !world.isClient() &&
			   center != null && axis != null &&
			   world.getRegistryKey() != World.NETHER;
	}
	
	public void checkStructure() {
		if (!isValid()) return;
		Direction moveX, moveY;
		if (Direction.Axis.X == axis) {
			moveX = Direction.EAST;
			moveY = Direction.NORTH;
		} else {
			moveX = Direction.SOUTH;
			moveY = Direction.EAST;
		}
		boolean valid = this.checkFrame();
		for (Point pos : structureMap) {
			BlockPos.Mutable checkPos = center.mutableCopy();
			checkPos.move(moveX, pos.x).move(moveY, pos.y);
			valid &= this.isActive(checkPos);
		}
		if (valid) {
			this.activatePortal();
		}
	}
	
	private boolean checkFrame() {
		BlockPos framePos = center.down();
		Direction moveDir = Direction.Axis.X == axis ? Direction.NORTH: Direction.EAST;
		boolean valid = true;
		for (Point point : frameMap) {
			BlockPos pos = framePos.offset(moveDir, point.x).offset(Direction.UP, point.y);
			BlockState state = world.getBlockState(pos);
			valid &= state.getBlock() instanceof RunedFlavolite;
			pos = framePos.offset(moveDir, -point.x).offset(Direction.UP, point.y);
			state = world.getBlockState(pos);
			valid &= state.getBlock() instanceof RunedFlavolite;
		}
		return valid;
	}
	
	public boolean isActive() {
		return this.active;
	}
	
	private void activatePortal() {
		if (active) return;
		this.activatePortal(world, center);
		if (exit == null) {
			this.exit = this.findPortalPos();
		} else {
			World targetWorld = this.getTargetWorld();
			this.activatePortal(targetWorld, exit);
		}
		this.active = true;
	}
	
	private void activatePortal(World world, BlockPos center) {
		BlockPos framePos = center.down();
		Direction moveDir = Direction.Axis.X == axis ? Direction.NORTH: Direction.EAST;
		BlockState frame = FRAME.getDefaultState().with(ACTIVE, true);
		frameMap.forEach(point -> {
			BlockPos pos = framePos.offset(moveDir, point.x).offset(Direction.UP, point.y);
			BlockState state = world.getBlockState(pos);
			if (state.contains(ACTIVE) && !state.get(ACTIVE)) {
				world.setBlockState(pos, frame);
			}
			pos = framePos.offset(moveDir, -point.x).offset(Direction.UP, point.y);
			state = world.getBlockState(pos);
			if (state.contains(ACTIVE) && !state.get(ACTIVE)) {
				world.setBlockState(pos, frame);
			}
		});
		Direction.Axis portalAxis = Direction.Axis.X == axis ? Direction.Axis.Z : Direction.Axis.X;
		BlockState portal = PORTAL.getDefaultState().with(EndPortalBlock.AXIS, portalAxis);
		portalMap.forEach(point -> {
			BlockPos pos = center.offset(moveDir, point.x).offset(Direction.UP, point.y);
			if (!world.getBlockState(pos).isOf(PORTAL)) {
				world.setBlockState(pos, portal);
			}
			pos = center.offset(moveDir, -point.x).offset(Direction.UP, point.y);
			if (!world.getBlockState(pos).isOf(PORTAL)) {
				world.setBlockState(pos, portal);
			}
		});
	}
	
	public void removePortal() {
		if (!active || !isValid()) return;
		World targetWorld = this.getTargetWorld();
		this.removePortal(world, center);
		this.removePortal(targetWorld, exit);
	}
	
	private void removePortal(World world, BlockPos center) {
		BlockPos framePos = center.down();
		Direction moveDir = Direction.Axis.X == axis ? Direction.NORTH: Direction.EAST;
		frameMap.forEach(point -> {
			BlockPos pos = framePos.offset(moveDir, point.x).offset(Direction.UP, point.y);
			BlockState state = world.getBlockState(pos);
			if (state.isOf(FRAME) && state.get(ACTIVE)) {
				world.setBlockState(pos, state.with(ACTIVE, false));
			}
			pos = framePos.offset(moveDir, -point.x).offset(Direction.UP, point.y);
			state = world.getBlockState(pos);
			if (state.isOf(FRAME) && state.get(ACTIVE)) {
				world.setBlockState(pos, state.with(ACTIVE, false));
			}
		});
		portalMap.forEach(point -> {
			BlockPos pos = center.offset(moveDir, point.x).offset(Direction.UP, point.y);
			if (world.getBlockState(pos).isOf(PORTAL)) {
				world.removeBlock(pos, false);
			}
			pos = center.offset(moveDir, -point.x).offset(Direction.UP, point.y);
			if (world.getBlockState(pos).isOf(PORTAL)) {
				world.removeBlock(pos, false);
			}
		});
		this.active = false;
	}
	
	private BlockPos findPortalPos() {
		MinecraftServer server = world.getServer();
		ServerWorld targetWorld = (ServerWorld) this.getTargetWorld();
		Registry<DimensionType> registry = server.getRegistryManager().getDimensionTypes();
		double mult = registry.get(DimensionType.THE_END_ID).getCoordinateScale();
		BlockPos.Mutable basePos = center.mutableCopy().set(center.getX() / mult, center.getY(), center.getZ() / mult);
		Direction.Axis portalAxis = Direction.Axis.X == axis ? Direction.Axis.Z : Direction.Axis.X;
		if (checkIsAreaValid(targetWorld, basePos, portalAxis)) {
			EternalRitual.generatePortal(targetWorld, basePos, portalAxis);
			if (portalAxis.equals(Direction.Axis.X)) {
				return basePos.toImmutable();
			} else {
				return basePos.toImmutable();
			}
		} else {
			Direction direction = Direction.EAST;
			BlockPos.Mutable checkPos = basePos.mutableCopy();
			for (int step = 1; step < 64; step++) {
				for (int i = 0; i < step; i++) {
					checkPos.setY(5);
					while(checkPos.getY() < world.getHeight()) {
						if(checkIsAreaValid(targetWorld, checkPos, portalAxis)) {
							EternalRitual.generatePortal(targetWorld, checkPos, portalAxis);
							if (portalAxis.equals(Direction.Axis.X)) {
								return checkPos.toImmutable();
							} else {
								return checkPos.toImmutable();
							}
						}
						checkPos.move(Direction.UP);
					}
					checkPos.move(direction);
				}
				direction = direction.rotateYClockwise();
			}
		}
		if (targetWorld.getRegistryKey() == World.END) {
			ConfiguredFeatures.END_ISLAND.generate(targetWorld, targetWorld.getChunkManager().getChunkGenerator(), new Random(basePos.asLong()), basePos.down());
		} else {
			basePos.setY(targetWorld.getChunk(basePos).sampleHeightmap(Heightmap.Type.WORLD_SURFACE, basePos.getX(), basePos.getZ()));
		}
		EternalRitual.generatePortal(targetWorld, basePos, portalAxis);
		if (portalAxis.equals(Direction.Axis.X)) {
			return basePos.toImmutable();
		} else {
			return basePos.toImmutable();
		}
	}
	
	private World getTargetWorld() {
		RegistryKey<World> target = world.getRegistryKey() == World.END ? World.OVERWORLD : World.END;
		return world.getServer().getWorld(target);
	}
	
	private boolean checkIsAreaValid(World world, BlockPos pos, Direction.Axis axis) {
		if (!isBaseValid(world, pos, axis)) return false;
		return EternalRitual.checkArea(world, pos, axis);
	}
	
	private boolean isBaseValid(World world, BlockPos pos, Direction.Axis axis) {
		boolean solid = true;
		if (axis.equals(Direction.Axis.X)) {
			pos = pos.down().add(0, 0, -3);
			for (int i = 0; i < 7; i++) {
				BlockPos checkPos = pos.add(0, 0, i);
				BlockState state = world.getBlockState(checkPos);
				solid &= this.validBlock(world, checkPos, state);
			}
		} else {
			pos = pos.down().add(-3, 0, 0);
			for (int i = 0; i < 7; i++) {
				BlockPos checkPos = pos.add(i, 0, 0);
				BlockState state = world.getBlockState(checkPos);
				solid &= this.validBlock(world, checkPos, state);
			}
		}
		return solid;
	}
	
	private boolean validBlock(World world, BlockPos pos, BlockState state) {
		BlockState surfaceBlock = world.getBiome(pos).getGenerationSettings().getSurfaceConfig().getTopMaterial();
		return state.isSolidBlock(world, pos) &&
			   (EndTags.validGenBlock(state) ||
			   state.isOf(surfaceBlock.getBlock()) ||
			   state.isOf(Blocks.STONE) ||
			   state.isOf(Blocks.SAND) ||
			   state.isOf(Blocks.GRAVEL));
	}
	
	public static void generatePortal(World world, BlockPos center, Direction.Axis axis) {
		BlockPos framePos = center.down();
		Direction moveDir = Direction.Axis.X == axis ? Direction.EAST: Direction.NORTH;
		BlockState frame = FRAME.getDefaultState().with(ACTIVE, true);
		frameMap.forEach(point -> {
			BlockPos pos = framePos.offset(moveDir, point.x).offset(Direction.UP, point.y);
			world.setBlockState(pos, frame);
			pos = framePos.offset(moveDir, -point.x).offset(Direction.UP, point.y);
			world.setBlockState(pos, frame);
		});
		BlockState portal = PORTAL.getDefaultState().with(EndPortalBlock.AXIS, axis);
		portalMap.forEach(point -> {
			BlockPos pos = center.offset(moveDir, point.x).offset(Direction.UP, point.y);
			world.setBlockState(pos, portal);
			pos = center.offset(moveDir, -point.x).offset(Direction.UP, point.y);
			world.setBlockState(pos, portal);
		});
		generateBase(world, framePos, moveDir);
	}
	
	private static void generateBase(World world, BlockPos center, Direction moveX) {
		BlockState base = BASE.getDefaultState();
		Direction moveY = moveX.rotateYClockwise();
		baseMap.forEach(point -> {
			BlockPos pos = center.offset(moveX, point.x).offset(moveY, point.y);
			world.setBlockState(pos, base);
			pos = center.offset(moveX, -point.x).offset(moveY, point.y);
			world.setBlockState(pos, base);
			pos = center.offset(moveX, point.x).offset(moveY, -point.y);
			world.setBlockState(pos, base);
			pos = center.offset(moveX, -point.x).offset(moveY, -point.y);
			world.setBlockState(pos, base);
		});
	}
	
	public static boolean checkArea(World world, BlockPos center, Direction.Axis axis) {
		Direction moveDir = Direction.Axis.X == axis ? Direction.NORTH: Direction.EAST;
		for (BlockPos checkPos : BlockPos.iterate(center.offset(moveDir.rotateYClockwise()),
												  center.offset(moveDir.rotateYCounterclockwise()))) {
			for (Point point : portalMap) {
				BlockPos pos = checkPos.offset(moveDir, point.x).offset(Direction.UP, point.y);
				if (!world.getBlockState(pos).isAir()) return false;
				pos = checkPos.offset(moveDir, -point.x).offset(Direction.UP, point.y);
				if (!world.getBlockState(pos).isAir()) return false;
			}
		}
		return true;
	}
	
	public void configure(BlockPos initial) {
		BlockPos checkPos = initial.east(12);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.X;
			this.center = initial.east(6);
			return;
		}
		checkPos = initial.west(12);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.X;
			this.center = initial.west(6);
			return;
		}
		checkPos = initial.south(12);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.Z;
			this.center = initial.south(6);
			return;
		}
		checkPos = initial.north(12);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.Z;
			this.center = initial.north(6);
			return;
		}
		checkPos = initial.north(10);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.X;
			checkPos = checkPos.east(8);
			if (this.hasPedestal(checkPos)) {
				this.center = initial.north(5).east(4);
				return;
			} else {
				this.center = initial.north(5).west(4);
				return;
			}
		}
		checkPos = initial.south(10);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.X;
			checkPos = checkPos.east(8);
			if (this.hasPedestal(checkPos)) {
				this.center = initial.south(5).east(4);
				return;
			} else {
				this.center = initial.south(5).west(4);
				return;
			}
		}
		checkPos = initial.east(10);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.Z;
			checkPos = checkPos.south(8);
			if (this.hasPedestal(checkPos)) {
				this.center = initial.east(5).south(4);
				return;
			} else {
				this.center = initial.east(5).north(4);
				return;
			}
		}
		checkPos = initial.west(10);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.Z;
			checkPos = checkPos.south(8);
			if (this.hasPedestal(checkPos)) {
				this.center = initial.west(5).south(4);
				return;
			} else {
				this.center = initial.west(5).north(4);
				return;
			}
		}
	}
	
	private boolean hasPedestal(BlockPos pos) {
		return world.getBlockState(pos).isOf(PEDESTAL);
	}
	
	private boolean isActive(BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.isOf(PEDESTAL)) {
			PedestalBlockEntity pedestal = (PedestalBlockEntity) world.getBlockEntity(pos);
			if (!pedestal.hasRitual()) {
				pedestal.linkRitual(this);
			}
			return state.get(ACTIVE);
		}
		return false;
	}
	
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("center", NbtHelper.fromBlockPos(center));
		if (exit != null) {
			tag.put("exit", NbtHelper.fromBlockPos(exit));
		}
		tag.putString("axis", axis.getName());
		tag.putBoolean("active", active);
		return tag;
	}
	
	public void fromTag(CompoundTag tag) {
		this.axis = Direction.Axis.fromName(tag.getString("axis"));
		this.center = NbtHelper.toBlockPos(tag.getCompound("center"));
		this.active = tag.getBoolean("active");
		if (tag.contains("exit")) {
			this.exit = NbtHelper.toBlockPos(tag.getCompound("exit"));
		}
	}
}
