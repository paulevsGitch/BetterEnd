package ru.betterend.blocks;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import ru.betterend.blocks.basis.BlockBaseNotFull;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class MengerSpongeWetBlock extends BlockBaseNotFull implements IRenderTypeable {
	public MengerSpongeWetBlock() {
		super(FabricBlockSettings.copyOf(Blocks.WET_SPONGE).nonOpaque());
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (world.getDimension().isUltrawarm()) {
			world.setBlockState(pos, EndBlocks.MENGER_SPONGE.getDefaultState(), 3);
			world.syncWorldEvent(2009, pos, 0);
			world.playSound((PlayerEntity) null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, (1.0F + world.getRandom().nextFloat() * 0.2F) * 0.7F);
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		Direction direction = Direction.random(random);
		if (direction != Direction.UP) {
			BlockPos blockPos = pos.offset(direction);
			BlockState blockState = world.getBlockState(blockPos);
			if (!state.isOpaque() || !blockState.isSideSolidFullSquare(world, blockPos, direction.getOpposite())) {
				double x = (double) pos.getX();
				double y = (double) pos.getY();
				double z = (double) pos.getZ();
				if (direction == Direction.DOWN) {
					y -= 0.05;
					x += random.nextDouble();
					z += random.nextDouble();
				}
				else {
					y += random.nextDouble() * 0.8;
					if (direction.getAxis() == Direction.Axis.X) {
						z += random.nextDouble();
						if (direction == Direction.EAST) {
							++x;
						}
						else {
							x += 0.05;
						}
					}
					else {
						x += random.nextDouble();
						if (direction == Direction.SOUTH) {
							++z;
						}
						else {
							z += 0.05;
						}
					}
				}

				world.addParticle(ParticleTypes.DRIPPING_WATER, x, y, z, 0, 0, 0);
			}
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlocksHelper.setWithUpdate(world, pos, Blocks.AIR);
		if (!world.isClient()) {
			world.syncWorldEvent(2001, pos, getRawIdFromState(state));
		}
		if (world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && (player == null || !player.isCreative())) {
			ItemEntity drop = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(this));
			world.spawnEntity(drop);
		}
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStill(false);
	}
}
