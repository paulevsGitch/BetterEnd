package ru.betterend.rituals;

import java.awt.Point;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.Lists;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import ru.betterend.BetterEnd;
import ru.betterend.blocks.BlockProperties;
import ru.betterend.blocks.EndPortalBlock;
import ru.betterend.blocks.RunedFlavolite;
import ru.betterend.blocks.entities.EternalPedestalEntity;
import ru.betterend.registry.EndBlocks;
import ru.betterend.registry.EndFeatures;
import ru.betterend.registry.EndPortals;

public class EternalRitual {
	private final static Set<Point> STRUCTURE_MAP = Sets.newHashSet(
			new Point(-4, -5), new Point(-4, 5), new Point(-6, 0),
			new Point(4, -5), new Point(4, 5), new Point(6, 0));
	private final static Set<Point> FRAME_MAP = Sets.newHashSet(
			new Point(0, 0), new Point(0, 6), new Point(1, 0),
			new Point(1, 6), new Point(2, 1), new Point(2, 5),
			new Point(3, 2), new Point(3, 3), new Point(3, 4));
	private final static Set<Point> PORTAL_MAP = Sets.newHashSet(
			new Point(0, 0), new Point(0, 1), new Point(0, 2),
			new Point(0, 3), new Point(0, 4), new Point(1, 0),
			new Point(1, 1), new Point(1, 2), new Point(1, 3),
			new Point(1, 4), new Point(2, 1), new Point(2, 2),
			new Point(2, 3));
	private final static Set<Point> BASE_MAP = Sets.newHashSet(
			new Point(3, 0), new Point(2, 0), new Point(2, 1), new Point(1, 1),
			new Point(1, 2), new Point(0, 1), new Point(0, 2));

	private final static Block BASE = EndBlocks.FLAVOLITE.tiles;
	private final static Block PEDESTAL = EndBlocks.ETERNAL_PEDESTAL;
	private final static Block FRAME = EndBlocks.FLAVOLITE_RUNED_ETERNAL;
	private final static Block PORTAL = EndBlocks.END_PORTAL_BLOCK;
	private final static BooleanProperty ACTIVE = BlockProperties.ACTIVE;

	public final static int SEARCH_RADIUS = calculateSearchSteps(48);

	private World world;
	private Direction.Axis axis;
	private Identifier targetWorldId;
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

	public void setWorld(World world) {
		this.world = world;
	}

	@Nullable
	public Identifier getTargetWorldId() {
		return targetWorldId;
	}

	private boolean isInvalid() {
		return world == null || world.isClient() ||
				center == null || axis == null;
	}

	public void checkStructure() {
		if (isInvalid()) return;
		Direction moveX, moveY;
		if (Direction.Axis.X == axis) {
			moveX = Direction.EAST;
			moveY = Direction.NORTH;
		}
		else {
			moveX = Direction.SOUTH;
			moveY = Direction.EAST;
		}
		boolean valid = checkFrame(world, center.down());
		Item item = null;
		for (Point pos : STRUCTURE_MAP) {
			BlockPos.Mutable checkPos = center.mutableCopy();
			checkPos.move(moveX, pos.x).move(moveY, pos.y);
			valid &= isActive(checkPos);
			if (valid) {
				EternalPedestalEntity pedestal = (EternalPedestalEntity) world.getBlockEntity(checkPos);
				if (pedestal != null) {
					Item pItem = pedestal.getStack(0).getItem();
					if (item == null) {
						item = pItem;
					} else if (!item.equals(pItem)) {
						valid = false;
					}
				}
			}
		}
		if (valid && item != null) {
			activatePortal(item);
		}
	}

	private boolean checkFrame(World world, BlockPos framePos) {
		Direction moveDir = Direction.Axis.X == axis ? Direction.NORTH : Direction.EAST;
		boolean valid = true;
		for (Point point : FRAME_MAP) {
			BlockPos pos = framePos.mutableCopy().move(moveDir, point.x).move(Direction.UP, point.y);
			BlockState state = world.getBlockState(pos);
			valid &= state.getBlock() instanceof RunedFlavolite;
			pos = framePos.mutableCopy().move(moveDir, -point.x).move(Direction.UP, point.y);
			state = world.getBlockState(pos);
			valid &= state.getBlock() instanceof RunedFlavolite;
		}
		return valid;
	}

	public boolean isActive() {
		return active;
	}

	private void activatePortal(Item keyItem) {
		if (active) return;
		Identifier itemId = Registry.ITEM.getId(keyItem);
		int portalId = EndPortals.getPortalIdByItem(itemId);
		World targetWorld = getTargetWorld(portalId);
		Identifier worldId = targetWorld.getRegistryKey().getValue();
		try {
			if (exit == null) {
				initPortal(worldId, portalId);
			} else {
				if (!worldId.equals(targetWorldId)) {
					initPortal(worldId, portalId);
				} else if (!checkFrame(targetWorld, exit.down())) {
					Direction.Axis portalAxis = (Direction.Axis.X == axis) ? Direction.Axis.Z : Direction.Axis.X;
					generatePortal(targetWorld, exit, portalAxis, portalId);
				}
				activatePortal(targetWorld, exit, portalId);
			}
			activatePortal(world, center, portalId);
			doEffects((ServerWorld) world, center);
			active = true;
		} catch (Exception ex) {
			BetterEnd.LOGGER.error("Create End portals error.", ex);
			removePortal(targetWorld, exit);
			removePortal(world, center);
			active = false;
		}
	}

	private void initPortal(Identifier worldId, int portalId) {
		targetWorldId = worldId;
		exit = findPortalPos(portalId);
	}

	private void doEffects(ServerWorld serverWorld, BlockPos center) {
		Direction moveX, moveY;
		if (Direction.Axis.X == axis) {
			moveX = Direction.EAST;
			moveY = Direction.NORTH;
		}
		else {
			moveX = Direction.SOUTH;
			moveY = Direction.EAST;
		}
		for (Point pos : STRUCTURE_MAP) {
			BlockPos.Mutable p = center.mutableCopy();
			p.move(moveX, pos.x).move(moveY, pos.y);
			serverWorld.spawnParticles(ParticleTypes.PORTAL, p.getX() + 0.5, p.getY() + 1.5, p.getZ() + 0.5, 20, 0, 0, 0, 1);
			serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL, p.getX() + 0.5, p.getY() + 1.5, p.getZ() + 0.5, 20, 0, 0, 0, 0.3);
		}
		serverWorld.playSound(null, center, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.NEUTRAL, 16, 1);
	}

	private void activatePortal(World world, BlockPos center, int portalId) {
		BlockPos framePos = center.down();
		Direction moveDir = Direction.Axis.X == axis ? Direction.NORTH : Direction.EAST;
		BlockState frame = FRAME.getDefaultState().with(ACTIVE, true);
		FRAME_MAP.forEach(point -> {
			BlockPos pos = framePos.mutableCopy().move(moveDir, point.x).move(Direction.UP, point.y);
			BlockState state = world.getBlockState(pos);
			if (state.contains(ACTIVE) && !state.get(ACTIVE)) {
				world.setBlockState(pos, frame);
			}
			pos = framePos.mutableCopy().move(moveDir, -point.x).move(Direction.UP, point.y);
			state = world.getBlockState(pos);
			if (state.contains(ACTIVE) && !state.get(ACTIVE)) {
				world.setBlockState(pos, frame);
			}
		});
		Direction.Axis portalAxis = Direction.Axis.X == axis ? Direction.Axis.Z : Direction.Axis.X;
		BlockState portal = PORTAL.getDefaultState().with(EndPortalBlock.AXIS, portalAxis).with(EndPortalBlock.PORTAL, portalId);
		ParticleEffect effect = new BlockStateParticleEffect(ParticleTypes.BLOCK, portal);
		ServerWorld serverWorld = (ServerWorld) world;

		PORTAL_MAP.forEach(point -> {
			BlockPos pos = center.mutableCopy().move(moveDir, point.x).move(Direction.UP, point.y);
			if (!world.getBlockState(pos).isOf(PORTAL)) {
				world.setBlockState(pos, portal);
				serverWorld.spawnParticles(effect, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
				serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.3);
			}
			pos = center.mutableCopy().move(moveDir, -point.x).move(Direction.UP, point.y);
			if (!world.getBlockState(pos).isOf(PORTAL)) {
				world.setBlockState(pos, portal);
				serverWorld.spawnParticles(effect, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
				serverWorld.spawnParticles(ParticleTypes.REVERSE_PORTAL, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.3);
			}
		});
	}

	public void disablePortal(int state) {
		if (!active || isInvalid()) return;
		removePortal(getTargetWorld(state), exit);
		removePortal(world, center);
	}

	private void removePortal(World world, BlockPos center) {
		BlockPos framePos = center.down();
		Direction moveDir = Direction.Axis.X == axis ? Direction.NORTH : Direction.EAST;
		FRAME_MAP.forEach(point -> {
			BlockPos pos = framePos.mutableCopy().move(moveDir, point.x).move(Direction.UP, point.y);
			BlockState state = world.getBlockState(pos);
			if (state.isOf(FRAME) && state.get(ACTIVE)) {
				world.setBlockState(pos, state.with(ACTIVE, false));
			}
			pos = framePos.mutableCopy().move(moveDir, -point.x).move(Direction.UP, point.y);
			state = world.getBlockState(pos);
			if (state.isOf(FRAME) && state.get(ACTIVE)) {
				world.setBlockState(pos, state.with(ACTIVE, false));
			}
		});
		PORTAL_MAP.forEach(point -> {
			BlockPos pos = center.mutableCopy().move(moveDir, point.x).move(Direction.UP, point.y);
			if (world.getBlockState(pos).isOf(PORTAL)) {
				world.removeBlock(pos, false);
			}
			pos = center.mutableCopy().move(moveDir, -point.x).move(Direction.UP, point.y);
			if (world.getBlockState(pos).isOf(PORTAL)) {
				world.removeBlock(pos, false);
			}
		});
		this.active = false;
	}

	@Nullable
	private BlockPos findFrame(World world, BlockPos.Mutable startPos) {
		List<BlockPos.Mutable> foundPos = findAllBlockPos(world, startPos, (SEARCH_RADIUS >> 4) + 1, FRAME,
				blockState -> blockState.isOf(FRAME) && !blockState.get(ACTIVE));
		for(BlockPos.Mutable testPos : foundPos) {
			if (checkFrame(world, testPos)) {
				return testPos;
			}
		}
		return null;
	}

	private BlockPos findPortalPos(int portalId) {
		MinecraftServer server = world.getServer();
		ServerWorld targetWorld = (ServerWorld) getTargetWorld(portalId);
		Registry<DimensionType> registry = Objects.requireNonNull(server).getRegistryManager().getDimensionTypes();
		double multiplier = Objects.requireNonNull(registry.get(targetWorldId)).getCoordinateScale();
		BlockPos.Mutable basePos = center.mutableCopy().set(center.getX() / multiplier, center.getY(), center.getZ() / multiplier);
		BlockPos framePos = findFrame(targetWorld, basePos.mutableCopy());
		if (framePos != null) {
			return framePos.up();
		}
		Direction.Axis portalAxis = (Direction.Axis.X == axis) ? Direction.Axis.Z : Direction.Axis.X;
		int worldCeil = targetWorld.getDimensionHeight() - 1;
		if (checkIsAreaValid(targetWorld, basePos, portalAxis)) {
			generatePortal(targetWorld, basePos, portalAxis, portalId);
			return basePos.toImmutable();
		} else {
			Direction direction = Direction.EAST;
			BlockPos.Mutable checkPos = basePos.mutableCopy();
			int radius = (int) ((SEARCH_RADIUS / multiplier) + 1);
			for (int step = 1; step < radius; step++) {
				for (int i = 0; i < (step >> 1); i++) {
					Chunk chunk = targetWorld.getChunk(checkPos);
					if (chunk != null) {
						int surfaceY = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, checkPos.getX() & 15, checkPos.getZ() & 15);
						int motionY = chunk.sampleHeightmap(Heightmap.Type.MOTION_BLOCKING, checkPos.getX() & 15, checkPos.getZ() & 15);
						int ceil = Math.min(Math.max(surfaceY, motionY) + 1, worldCeil);
						if (ceil < 5) continue;
						checkPos.setY(ceil);
						while (checkPos.getY() >= 5) {
							if(checkIsAreaValid(targetWorld, checkPos, portalAxis)) {
								generatePortal(targetWorld, checkPos, portalAxis, portalId);
								return checkPos.toImmutable();
							}
							checkPos.move(Direction.DOWN);
						}
					}
					checkPos.move(direction);
				}
				direction = direction.rotateYClockwise();
			}
		}
		if (targetWorld.getRegistryKey() == World.END) {
			ConfiguredFeatures.END_ISLAND.generate(targetWorld, targetWorld.getChunkManager().getChunkGenerator(), new Random(basePos.asLong()), basePos.down());
		} else if (targetWorld.getRegistryKey() == World.OVERWORLD) {
			basePos.setY(targetWorld.getChunk(basePos).sampleHeightmap(Heightmap.Type.WORLD_SURFACE, basePos.getX(), basePos.getZ()) + 1);
		}
		EndFeatures.BIOME_ISLAND.getFeatureConfigured().generate(targetWorld, targetWorld.getChunkManager().getChunkGenerator(), new Random(basePos.asLong()), basePos.down());
		generatePortal(targetWorld, basePos, portalAxis, portalId);
		return basePos.toImmutable();
	}

	private World getTargetWorld(int state) {
		if (world.getRegistryKey() == World.END) {
			return EndPortals.getWorld(world.getServer(), state);
		}
		return Objects.requireNonNull(world.getServer()).getWorld(World.END);
	}

	private boolean checkIsAreaValid(World world, BlockPos pos, Direction.Axis axis) {
		if (pos.getY() >= world.getDimensionHeight() - 1) return false;
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
				solid &= validBlock(world, checkPos, state);
			}
		}
		else {
			pos = pos.down().add(-3, 0, 0);
			for (int i = 0; i < 7; i++) {
				BlockPos checkPos = pos.add(i, 0, 0);
				BlockState state = world.getBlockState(checkPos);
				solid &= validBlock(world, checkPos, state);
			}
		}
		return solid;
	}

	private boolean validBlock(World world, BlockPos pos, BlockState state) {
		return state.isSolidBlock(world, pos) && state.isFullCube(world, pos);
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
			} else {
				this.center = initial.north(5).west(4);
			}
			return;
		}
		checkPos = initial.south(10);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.X;
			checkPos = checkPos.east(8);
			if (this.hasPedestal(checkPos)) {
				this.center = initial.south(5).east(4);
			} else {
				this.center = initial.south(5).west(4);
			}
			return;
		}
		checkPos = initial.east(10);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.Z;
			checkPos = checkPos.south(8);
			if (this.hasPedestal(checkPos)) {
				this.center = initial.east(5).south(4);
			} else {
				this.center = initial.east(5).north(4);
			}
			return;
		}
		checkPos = initial.west(10);
		if (this.hasPedestal(checkPos)) {
			this.axis = Direction.Axis.Z;
			checkPos = checkPos.south(8);
			if (this.hasPedestal(checkPos)) {
				this.center = initial.west(5).south(4);
			} else {
				this.center = initial.west(5).north(4);
			}
		}
	}

	private boolean hasPedestal(BlockPos pos) {
		return world.getBlockState(pos).isOf(PEDESTAL);
	}

	private boolean isActive(BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.isOf(PEDESTAL)) {
			EternalPedestalEntity pedestal = (EternalPedestalEntity) world.getBlockEntity(pos);
			if (pedestal != null) {
				if (!pedestal.hasRitual()) {
					pedestal.linkRitual(this);
				} else {
					EternalRitual ritual = pedestal.getRitual();
					if (!ritual.equals(this)) {
						pedestal.linkRitual(this);
					}
				}
			}
			return state.get(ACTIVE);
		}
		return false;
	}

	public CompoundTag toTag(CompoundTag tag) {
		tag.put("center", NbtHelper.fromBlockPos(center));
		tag.putString("axis", axis.getName());
		tag.putBoolean("active", active);
		if (targetWorldId != null) {
			tag.putString("key_item", targetWorldId.toString());
		}
		if (exit != null) {
			tag.put("exit", NbtHelper.fromBlockPos(exit));
		}
		return tag;
	}

	public void fromTag(CompoundTag tag) {
		axis = Direction.Axis.fromName(tag.getString("axis"));
		center = NbtHelper.toBlockPos(tag.getCompound("center"));
		active = tag.getBoolean("active");
		if (tag.contains("exit")) {
			exit = NbtHelper.toBlockPos(tag.getCompound("exit"));
		}
		if (tag.contains("key_item")) {
			targetWorldId = new Identifier(tag.getString("key_item"));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		EternalRitual ritual = (EternalRitual) o;
		return world.equals(ritual.world) &&
				Objects.equals(center, ritual.center) &&
				Objects.equals(exit, ritual.exit);
	}

	public static void generatePortal(World world, BlockPos center, Direction.Axis axis, int portalId) {
		BlockPos framePos = center.down();
		Direction moveDir = Direction.Axis.X == axis ? Direction.EAST : Direction.NORTH;
		BlockState frame = FRAME.getDefaultState().with(ACTIVE, true);
		FRAME_MAP.forEach(point -> {
			BlockPos pos = framePos.mutableCopy().move(moveDir, point.x).move(Direction.UP, point.y);
			world.setBlockState(pos, frame);
			pos = framePos.mutableCopy().move(moveDir, -point.x).move(Direction.UP, point.y);
			world.setBlockState(pos, frame);
		});
		BlockState portal = PORTAL.getDefaultState().with(EndPortalBlock.AXIS, axis).with(EndPortalBlock.PORTAL, portalId);
		PORTAL_MAP.forEach(point -> {
			BlockPos pos = center.mutableCopy().move(moveDir, point.x).move(Direction.UP, point.y);
			world.setBlockState(pos, portal);
			pos = center.mutableCopy().move(moveDir, -point.x).move(Direction.UP, point.y);
			world.setBlockState(pos, portal);
		});
		generateBase(world, framePos, moveDir);
	}

	private static void generateBase(World world, BlockPos center, Direction moveX) {
		BlockState base = BASE.getDefaultState();
		Direction moveY = moveX.rotateYClockwise();
		BASE_MAP.forEach(point -> {
			BlockPos pos = center.mutableCopy().move(moveX, point.x).move(moveY, point.y);
			world.setBlockState(pos, base);
			pos = center.mutableCopy().move(moveX, -point.x).move(moveY, point.y);
			world.setBlockState(pos, base);
			pos = center.mutableCopy().move(moveX, point.x).move(moveY, -point.y);
			world.setBlockState(pos, base);
			pos = center.mutableCopy().move(moveX, -point.x).move(moveY, -point.y);
			world.setBlockState(pos, base);
		});
	}

	public static boolean checkArea(World world, BlockPos center, Direction.Axis axis) {
		Direction moveDir = Direction.Axis.X == axis ? Direction.NORTH : Direction.EAST;
		for (BlockPos checkPos : BlockPos.iterate(center.offset(moveDir.rotateYClockwise()), center.offset(moveDir.rotateYCounterclockwise()))) {
			for (Point point : PORTAL_MAP) {
				BlockPos pos = checkPos.mutableCopy().move(moveDir, point.x).move(Direction.UP, point.y);
				BlockState state = world.getBlockState(pos);
				if (isStateInvalid(state)) return false;
				pos = checkPos.mutableCopy().move(moveDir, -point.x).move(Direction.UP, point.y);
				state = world.getBlockState(pos);
				if (isStateInvalid(state)) return false;
			}
		}
		return true;
	}

	private static boolean isStateInvalid(BlockState state) {
		if (!state.getFluidState().isEmpty()) return true;
		Material material = state.getMaterial();
		return !material.isReplaceable() && !material.equals(Material.PLANT);
	}

	/**
	 * @param world World for search
	 * @param checkPos Start search position
	 * @param radius Search radius
	 * @param searchBlock Target block
	 * @param condition Predicate for test block states in the chunk section
	 *
	 * @return Position of the first found block or null.
	 */
	@Nullable
	public static BlockPos.Mutable findBlockPos(World world, BlockPos.Mutable checkPos, int radius, Block searchBlock, Predicate<BlockState> condition) {
		Direction moveDirection = Direction.EAST;
		for (int step = 1; step < radius; step++) {
			for (int i = 0; i < (step >> 1); i++) {
				Chunk chunk = world.getChunk(checkPos);
				if (!(chunk instanceof WorldChunk) || ((WorldChunk) chunk).isEmpty()) continue;
				for (ChunkSection section : chunk.getSectionArray()) {
					if (section == null || !section.getContainer().hasAny(condition)) continue;
					for (int x = 0; x < 16; x++) {
						for (int y = 0; y < 16; y++) {
							for(int z = 0; z < 16; z++) {
								BlockState checkState = section.getBlockState(x, y, z);
								if (checkState.isOf(searchBlock)) {
									int worldX = (chunk.getPos().x << 4) + x;
									int worldY = section.getYOffset() + y;
									int worldZ = (chunk.getPos().z << 4) + z;
									checkPos.set(worldX, worldY, worldZ);
									return checkPos;
								}
							}
						}
					}
				}
				checkPos.move(moveDirection, 16);
			}
			moveDirection = moveDirection.rotateYClockwise();
		}
		return null;
	}

	/**
	 * @param world World for search
	 * @param checkPos Start search position
	 * @param radius Search radius
	 * @param searchBlock Target block
	 * @param condition Predicate for test block states in the chunk section
	 *
	 * @return List of positions of the all found blocks or empty list.
	 */
	public static List<BlockPos.Mutable> findAllBlockPos(World world, BlockPos.Mutable checkPos, int radius, Block searchBlock, Predicate<BlockState> condition) {
		List<BlockPos.Mutable> posFound = Lists.newArrayList();
		Direction moveDirection = Direction.EAST;
		for (int step = 1; step < radius; step++) {
			for (int i = 0; i < (step >> 1); i++) {
				Chunk chunk = world.getChunk(checkPos);
				if (!(chunk instanceof WorldChunk) || ((WorldChunk) chunk).isEmpty()) continue;
				for (ChunkSection section : chunk.getSectionArray()) {
					if (section == null || !section.getContainer().hasAny(condition)) continue;
					for (int x = 0; x < 16; x++) {
						for (int y = 0; y < 16; y++) {
							for(int z = 0; z < 16; z++) {
								BlockState checkState = section.getBlockState(x, y, z);
								if (checkState.isOf(searchBlock)) {
									int worldX = (chunk.getPos().x << 4) + x;
									int worldY = section.getYOffset() + y;
									int worldZ = (chunk.getPos().z << 4) + z;
									checkPos.set(worldX, worldY, worldZ);
									posFound.add(checkPos.mutableCopy());
								}
							}
						}
					}
				}
				checkPos.move(moveDirection, 16);
			}
			moveDirection = moveDirection.rotateYClockwise();
		}
		return posFound;
	}

	public static int calculateSearchSteps(int radius) {
		return radius * 4 - 1;
	}
}
