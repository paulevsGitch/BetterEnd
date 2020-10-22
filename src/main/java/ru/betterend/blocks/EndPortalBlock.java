package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import ru.betterend.client.ERenderLayer;
import ru.betterend.client.IRenderTypeable;
import ru.betterend.interfaces.TeleportingEntity;
import ru.betterend.registry.BlockTagRegistry;
import ru.betterend.registry.FeatureRegistry;
import ru.betterend.registry.ParticleRegistry;

public class EndPortalBlock extends NetherPortalBlock implements IRenderTypeable {
	public EndPortalBlock() {
		super(FabricBlockSettings.copyOf(Blocks.NETHER_PORTAL).luminance(state -> {
			return 12;
		}));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(100) == 0) {
			world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
		}

		double x = pos.getX() + random.nextDouble();
		double y = pos.getY() + random.nextDouble();
		double z = pos.getZ() + random.nextDouble();
		int k = random.nextInt(2) * 2 - 1;
		if (!world.getBlockState(pos.west()).isOf(this) && !world.getBlockState(pos.east()).isOf(this)) {
			x = pos.getX() + 0.5D + 0.25D * k;
		} else {
			z = pos.getZ() + 0.5D + 0.25D * k;
		}

		world.addParticle(ParticleRegistry.PORTAL_SPHERE, x, y, z, 0, 0, 0);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {}
	
	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return state;
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (world instanceof ServerWorld && entity instanceof LivingEntity && !entity.hasVehicle() && !entity.hasPassengers() && entity.canUsePortals()) {
			TeleportingEntity teleEntity = TeleportingEntity.class.cast(entity);
			if (teleEntity.hasCooldown()) return;
			teleEntity.beSetCooldown(500);
			boolean isOverworld = world.getRegistryKey().equals(World.OVERWORLD);
			ServerWorld destination = ((ServerWorld) world).getServer().getWorld(isOverworld ? World.END : World.OVERWORLD);
			BlockPos exitPos = this.findExitPos(destination, pos, entity);
			teleEntity.beSetExitPos(exitPos);
			entity.moveToWorld(destination);
			teleEntity.beSetExitPos(null);
		}
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.TRANSLUCENT;
	}
	
	private BlockPos findExitPos(ServerWorld world, BlockPos pos, Entity entity) {
		Registry<DimensionType> registry = world.getRegistryManager().getDimensionTypes();
		double mult = registry.get(DimensionType.THE_END_ID).getCoordinateScale();
		int topY;
		BlockPos.Mutable basePos;
		if (world.getRegistryKey().equals(World.OVERWORLD)) {
			basePos = pos.mutableCopy().set(pos.getX() / mult, pos.getY(), pos.getZ() / mult);
			topY = world.getTopY(Heightmap.Type.WORLD_SURFACE, basePos.getX(), basePos.getZ());
		} else {
			basePos = pos.mutableCopy().set(pos.getX() * mult, pos.getY(), pos.getZ() * mult);
			topY = world.getHeight();
		}
		BlockPos top = basePos.mutableCopy().set(basePos.getX() + 32, topY, basePos.getZ() + 32);
		BlockPos.Mutable bottom = basePos.mutableCopy().set(basePos.getX() - 32, 5, basePos.getZ() - 32);
		for(BlockPos position : BlockPos.iterate(bottom, top)) {
			BlockState state = world.getBlockState(position);
			if(state.isOf(this)) {
				if (state.get(AXIS).equals(Direction.Axis.X)) {
					return position.add(1, 0, 0);
				} else {
					return position.add(0, 0, 1);
				}
			}
		}
		bottom.setY(basePos.getY());
		Direction.Axis axis = entity.getMovementDirection().getAxis();
		if (checkIsAreaValid(world, bottom)) {
			generatePortalFrame(world, bottom, axis, true);
			if (axis.equals(Direction.Axis.X)) {
				return bottom.add(1, 1, 0);
			} else {
				return bottom.add(0, 1, 1);
			}
		} else {
			if (bottom.getY() > top.getY()) {
				BlockPos buff = bottom;
				bottom = top.mutableCopy();
				top = buff;
			}
			for(BlockPos position : BlockPos.iterate(bottom, top)) {
				if (checkIsAreaValid(world, position)) {
					generatePortalFrame(world, position, axis, true);
					if (axis.equals(Direction.Axis.X)) {
						return position.add(1, 1, 0);
					} else {
						return position.add(0, 1, 1);
					}
				}
			}
		}
		if (world.getRegistryKey().equals(World.END)) {
			ConfiguredFeatures.END_ISLAND.generate(world, world.getChunkManager().getChunkGenerator(), new Random(basePos.asLong()), basePos);
		} else {
			basePos.setY(topY);
		}
		generatePortalFrame(world, basePos, axis, true);
		if (axis.equals(Direction.Axis.X)) {
			return basePos.add(1, 1, 0);
		} else {
			return basePos.add(0, 1, 1);
		}
	}
	
	private boolean checkIsAreaValid(World world, BlockPos pos) {
		BlockPos bottomCorner = pos.add(-1, 0, -1);
		BlockPos topCorner = bottomCorner.add(4, 4, 1);
		int airBlocks = 0;
		boolean free = true;
		for (BlockPos position : BlockPos.iterate(bottomCorner, topCorner)) {
			BlockState state = world.getBlockState(position);
			if (state.isAir()) airBlocks++;
			if (world.getRegistryKey().equals(World.END)) {
				free &= state.isAir() || BlockTagRegistry.validGenBlock(state);
			} else {
				BlockState surfaceBlock = world.getBiome(pos).getGenerationSettings().getSurfaceConfig().getTopMaterial();
				free &= this.validBlock(state, surfaceBlock.getBlock());
			}
		}
		return free && airBlocks == 48;
	}
	
	private boolean validBlock(BlockState state, Block surfaceBlock) {
		return state.isAir() ||
			   state.isOf(surfaceBlock) ||
			   state.isOf(Blocks.STONE) ||
			   state.isOf(Blocks.SAND) ||
			   state.isOf(Blocks.GRAVEL);
	}
	
	public static void generatePortalFrame(ServerWorld world, BlockPos pos, Direction.Axis axis, boolean active) {
		FeatureRegistry.END_PORTAL.configure(axis, active).getFeatureConfigured().generate(world, world.getChunkManager().getChunkGenerator(), new Random(), pos);
	}
}
